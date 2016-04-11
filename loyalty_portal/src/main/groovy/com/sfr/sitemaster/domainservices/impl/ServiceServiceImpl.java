/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.ServiceDao;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.domainservices.ServiceService;
import com.sfr.sitemaster.dto.ServiceDTO;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.Service;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;

import javax.security.auth.login.LoginException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Site Service responsible for dealing with Sites.
 *
 * @author piotr
 */
public class ServiceServiceImpl implements ServiceService {

    private final ServiceDao serviceDao;
    private final SiteDao siteDao;

    @Inject
    public ServiceServiceImpl(final SiteDao siteDao, final ServiceDao serviceDao) {
        this.serviceDao = serviceDao;
        this.siteDao = siteDao;
    }

    /**
     * Retrieves all Service entites.
     *
     * @return
     * @throws LoginException
     * @throws NoResultsException
     */
    @Transactional
    public List<ServiceDTO> findAll() throws DBException {
        return serviceDao.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }


    /**
     * Retrieves a single Service.
     *
     * @return
     * @throws LoginException
     * @throws NoResultsException
     */
    @Transactional
    public ServiceDTO get(final Long serviceId) throws DBException {
        return toDTO(serviceDao.find(serviceId));
    }


    /**
     * Retrieves all Services assigned to a site.
     *
     * @return
     * @throws LoginException
     * @throws NoResultsException
     */
    @Transactional
    public List<ServiceDTO> getServicesForSite(final Long siteId) {
        return serviceDao.getServicesForSite(siteId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private ServiceDTO toDTO(final Service service) {
        final ServiceDTO serviceDTO = new ServiceDTO();
        serviceDTO.setId(service.getId());
        serviceDTO.setType(service.getType());
        serviceDTO.setName(service.getDisplayName());
        serviceDTO.setIcon(service.getIcon());
        serviceDTO.setLang(service.getLang());
        return serviceDTO;
    }

    /**
     * Saves a Site entity.
     *
     * @param siteId
     * @throws DBException
     */
    @Transactional
    public void save(final PersistentSession persistentSession, final Long siteId, final List<ServiceDTO> serviceDTOs) throws DBException, EntityNotFoundException {
        final Site site = siteDao.getById(siteId);
        site.setModifiedBy(persistentSession.getUserId());
        if (site.getServices() == null) {
            site.setServices(new ArrayList<>());
        } else {
            site.getServices().clear();
        }

        for (final ServiceDTO serviceDTO : serviceDTOs) {
            site.getServices().add(serviceDao.find(serviceDTO.getId()));
        }
        siteDao.save(site);
    }
}
