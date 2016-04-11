/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.commons.DiffUtil;
import com.sfr.sitemaster.commons.SiteMergeUtil;
import com.sfr.sitemaster.dao.FuelDao;
import com.sfr.sitemaster.dao.ServiceDao;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.domainservices.SiteService;
import com.sfr.sitemaster.dto.OpeningInfoDTO;
import com.sfr.sitemaster.dto.ServiceDTO;
import com.sfr.sitemaster.dto.SiteDTO;
import com.sfr.sitemaster.entities.ChangeSetDTO;
import com.sfr.sitemaster.entities.Fuel;
import com.sfr.sitemaster.entities.OpeningInfo;
import com.sfr.sitemaster.entities.OpeningTime;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.entities.Service;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.entities.TemporarilyClosed;
import com.sfr.sitemaster.enums.Days;
import com.sfr.sitemaster.enums.Owner;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import com.sfr.sitemaster.exceptions.SearchFilterException;
import com.sfr.sitemaster.integration.JdeWsClient;
import com.sfr.sitemaster.rev.RevisionEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rx.Observable;

import javax.security.auth.login.LoginException;
import javax.validation.ValidationException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Site Service responsible for dealing with Sites.
 *
 * @author piotr
 */
public class SiteServiceImpl implements SiteService {

    private static final Logger log = LogManager.getLogger("HelloWorld");

    private final SiteDao siteDao;
    private final ServiceDao serviceDao;
    private final FuelDao fuelDao;
    private final JdeWsClient jdeWsClient;

    @Inject
    public SiteServiceImpl(final SiteDao siteDao, final JdeWsClient jdeWsClient, final ServiceDao serviceDao, final FuelDao fuelDao) {
        this.siteDao = siteDao;
        this.jdeWsClient = jdeWsClient;
        this.serviceDao = serviceDao;
        this.fuelDao = fuelDao;
    }

    /**
     * Retrieves Site entity for a given id.
     *
     * @param id
     * @return
     * @throws LoginException
     * @throws DBException
     * @throws EntityNotFoundException
     */
    @Override
    @Transactional
    public SiteDTO get(final Long id) throws DBException, EntityNotFoundException {
        final SiteDTO site = Observable.zip(siteDao.getObservableById(id), jdeWsClient.getSiteById(id), (s, j) -> {
            final Map dataSources = new HashMap();
            if (s == null) {
                s = new Site();
            }
            dataSources.put(Owner.SITEMASTER, s);
            dataSources.put(Owner.JDE, j);
            return SiteMergeUtil.from(dataSources).to(SiteDTO.class);
        }).onErrorReturn(e -> {
            //TODO: @piotr improve error handling in Observables
            if (log.isDebugEnabled()) {
                log.debug("No site with id: {}", id, e);
            }
            return null;
        }).toBlocking().first();
        if (site == null) {
            throw new EntityNotFoundException(Site.class, id);
        }
        return site;
    }

    /**
     * Retrieves all Site entities.
     *
     * @return
     * @throws LoginException
     * @throws DBException
     * @throws NoResultsException
     */
    @Override
    @Transactional
    public List<SiteDTO> findAll() throws DBException, NoResultsException {
        final List<SiteDTO> sites = siteDao.findAll().stream().map(s -> {
            s.getSitePersons().size();
            final Map map = new HashMap();
            map.put(Owner.SITEMASTER, s);
            return SiteMergeUtil.from(map).to(SiteDTO.class);
        }).collect(Collectors.toList());
        if (sites == null) {
            throw new NoResultsException(Site.class);
        }
        return sites;
    }

    /**
     * Saves a Site entity.
     *
     * @param siteDTO
     * @throws DBException
     */
    @Override
    @Transactional
    public void save(final PersistentSession persistentSession, final SiteDTO siteDTO) throws DBException, EntityNotFoundException, JsonProcessingException, ValidationException {
        final Site siteEntity = updateSite(Optional.ofNullable(siteDao.getById(siteDTO.getSiteId())).orElse(new Site()), siteDTO);
        siteEntity.setModifiedBy(persistentSession.getUserId());
        siteDao.save(siteEntity);
    }

    private Site updateSite(final Site site, final SiteDTO siteDTO) throws DBException {
        site.setId(siteDTO.getSiteId());
        site.setxCoord(siteDTO.getxCoord());
        site.setyCoord(siteDTO.getyCoord());
        Optional.ofNullable(siteDTO.getOpeningInfo()).ifPresent(openingInfoDTO -> site.setOpeningInfo(updateOpeningInfo(site, openingInfoDTO)));
        Optional.ofNullable(siteDTO.getServices()).ifPresent(serviceDTOs -> {
            try {
                site.setServices(updateServices(site, serviceDTOs));
            } catch (DBException e) {
                log.error("Error while updating services for a Site: {}", siteDTO.getSiteId());
                throw new RuntimeException(e);
            }
        });
        Optional.ofNullable(siteDTO.getFuels()).ifPresent(fuelDTOs -> {
            try {
                site.setFuels(updateFuels(site, fuelDTOs));
            } catch (DBException e) {
                log.error("Error while updating fuels for a Site: {}", siteDTO.getSiteId());
                throw new RuntimeException(e);
            }
        });
        return site;
    }

    private OpeningInfo updateOpeningInfo(final Site site, final OpeningInfoDTO openingInfoDTO) {
        if (site.getOpeningInfo() == null) {
            site.setOpeningInfo(new OpeningInfo());
        }
        final OpeningInfo openingInfo = site.getOpeningInfo();
        openingInfo.setAlwaysOpen(openingInfoDTO.getAlwaysOpen());
        Optional.ofNullable(openingInfoDTO.getOpeningTimes()).ifPresent(openingTimeDTOMap -> {
            final Map<Days, OpeningTime> openingTimes = Optional.ofNullable(site.getOpeningInfo().getOpeningTimes()).orElse(new HashMap<Days, OpeningTime>());
            openingInfo.setOpeningTimes(updateOpeningTimes(site, openingTimes, openingTimeDTOMap));
        });
        Optional.ofNullable(openingInfoDTO.getTemporarilyClosed()).ifPresent(temporarilyClosedDTOList -> {
            final List<TemporarilyClosed> temporarilyClosed = Optional.ofNullable(site.getOpeningInfo().getTemporarilyClosed()).orElse(new ArrayList<TemporarilyClosed>());
            openingInfo.setTemporarilyClosed(updateTemporarilyClosed(temporarilyClosed, temporarilyClosedDTOList));
        });
        return openingInfo;
    }

    private Map<Days, OpeningTime> updateOpeningTimes(final Site site, final Map<Days, OpeningTime> openingTimeMap, final Map<Days, OpeningInfoDTO.OpeningTimeDTO> openingTimeDTOMap) {
        final Map<Days, OpeningTime> _openingTimes = new HashMap<>();
        openingTimeDTOMap.forEach((k, v) -> {
            OpeningTime openingTime = null;
            if (openingTimeMap.containsKey(k)) {
                openingTime = openingTimeMap.get(k);
            } else {
                openingTime = new OpeningTime();
                openingTime.setDays(k);
                openingTime.setSite(site);
            }
            openingTime.setOpen(v.getOpen());
            openingTime.setClose(v.getClose());
            _openingTimes.put(k, openingTime);
        });
        openingTimeMap.clear();
        openingTimeMap.putAll(_openingTimes);
        return openingTimeMap;
    }

    private List<TemporarilyClosed> updateTemporarilyClosed(final List<TemporarilyClosed> temporarilyClosedList, final List<OpeningInfoDTO.TemporarilyClosedDTO> temporarilyClosedDTOList) {
        final List<TemporarilyClosed> _tempClosed = temporarilyClosedDTOList.stream().map(tDTO -> {
            return temporarilyClosedList.stream().filter(t -> {
                return t.getFrom() == tDTO.getFrom() && t.getTo() == tDTO.getTo();
            }).findFirst().orElseGet(() -> {
                final TemporarilyClosed _temp = new TemporarilyClosed();
                _temp.setFrom(tDTO.getFrom());
                _temp.setTo(tDTO.getTo());
                return _temp;
            });
        }).collect(Collectors.toList());
        temporarilyClosedList.clear();
        temporarilyClosedList.addAll(_tempClosed);
        return temporarilyClosedList;
    }

    private List<Service> updateServices(final Site site, final List<ServiceDTO> serviceDTOList) throws DBException {
        if (!serviceDTOList.isEmpty()) {
            final Long[] ids = serviceDTOList.stream().map(sDTO -> {
                return sDTO.getId();
            }).toArray(size -> new Long[size]);
            site.setServices(serviceDao.find(ids));
        }
        return site.getServices();
    }

    private List<Fuel> updateFuels(final Site site, final List<ServiceDTO> fuelDTOList) throws DBException {
        if (!fuelDTOList.isEmpty()) {
            final Long[] ids = fuelDTOList.stream().map(sDTO -> {
                return sDTO.getId();
            }).toArray(size -> new Long[size]);
            site.setFuels(fuelDao.find(ids));
        }
        return site.getFuels();
    }

    @Override
    @Transactional
    public List<Site> search(final List<SearchFilter> searchFilters) throws SearchFilterException {
        final List<Site> sites = siteDao.search(searchFilters);
        sites.forEach(s -> s.getSitePersons().size());
        return sites;
    }

    /**
     * Retrieves Site's changeSet list.
     *
     * @param id
     * @return
     * @throws LoginException
     * @throws DBException
     * @throws EntityNotFoundException
     */
    @Transactional
    public List<ChangeSetDTO> getChangeSets(final Long id) throws DBException, EntityNotFoundException {
        final List<ChangeSetDTO> changes = new ArrayList<>();
        final List<Object[]> siteList = siteDao.getRevisions(id);
        final Observable<Object[]> a = Observable.from(siteList);
        final Observable<Object[]> b = Observable.from(siteList).skip(1);
        Observable.zip(a, b, (_a, _b) -> {
            final ChangeSetDTO changeSet = new ChangeSetDTO();
            if (_b[1] instanceof RevisionEntity) {
                final Map dataSources_a = new HashMap<>();
                final Map dataSources_b = new HashMap<>();
                dataSources_a.put(Owner.SITEMASTER, _a[0]);
                dataSources_b.put(Owner.SITEMASTER, _b[0]);
                final SiteDTO siteDTO_a = SiteMergeUtil.from(dataSources_a).to(SiteDTO.class);
                final SiteDTO siteDTO_b = SiteMergeUtil.from(dataSources_b).to(SiteDTO.class);
                changeSet.setModifiedBy(((Site) _b[0]).getModifiedBy());
                changeSet.setChanges(DiffUtil.compare(siteDTO_a, siteDTO_b));
                changeSet.setDate(LocalDateTime.ofInstant(Instant.ofEpochMilli(((RevisionEntity) _b[1]).getTimestamp()), ZoneId.systemDefault()));
            }
            return changeSet;
        }).subscribe(c -> {
            changes.add(c);
        });
        return changes;
    }
}
