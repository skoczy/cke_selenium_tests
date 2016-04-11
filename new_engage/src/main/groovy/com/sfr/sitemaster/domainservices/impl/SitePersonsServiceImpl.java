/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.persist.Transactional;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.commons.SiteMergeUtil;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.dao.SitePersonsDao;
import com.sfr.sitemaster.dao.SitePersonsRoleDao;
import com.sfr.sitemaster.domainservices.SitePersonsService;
import com.sfr.sitemaster.dto.SitePersonDTO;
import com.sfr.sitemaster.dto.SitePersonRoleDTO;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.entities.SitePerson;
import com.sfr.sitemaster.entities.SitePersonRole;
import com.sfr.sitemaster.enums.Owner;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import com.sfr.sitemaster.integration.JdeWsClient;
import com.sfr.sitemaster.integration.impl.JDEResponse;

import javax.security.auth.login.LoginException;
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
public class SitePersonsServiceImpl implements SitePersonsService {

    private final SitePersonsDao sitePersonsDao;
    private final SitePersonsRoleDao sitePersonsRoleDao;
    private final SiteDao siteDao;
    private final JdeWsClient jdeWsClient;

    @Inject
    public SitePersonsServiceImpl(final SiteDao siteDao, final SitePersonsDao sitePersonsDao, final SitePersonsRoleDao sitePersonsRoleDao, final JdeWsClient jdeWsClient) {
        this.sitePersonsDao = sitePersonsDao;
        this.sitePersonsRoleDao = sitePersonsRoleDao;
        this.siteDao = siteDao;
        this.jdeWsClient = jdeWsClient;
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
    public SitePersonDTO get(final Long siteId, final Long id) {
        final Map dataSources = new HashMap<>();
        dataSources.put(Owner.SITEMASTER, sitePersonsDao.getSitePerson(siteId, id));
        dataSources.put(Owner.SITEMASTER, sitePersonsDao.getSitePerson(siteId, id));
        return SiteMergeUtil.from(dataSources).to(SitePersonDTO.class);
    }

    /**
     * Retrieves all Site entities.
     *
     * @return
     * @throws LoginException
     * @throws NoResultsException
     */
    @Transactional
    public List<SitePersonDTO> findAll(final Long siteId) {
        final List<SitePerson> sitePersons = sitePersonsDao.getSitePersonsForSite(siteId);
        final JDEResponse jdeResponse = jdeWsClient.getSiteById(siteId).toBlocking().first();
        return mergeSitePerson(sitePersons, jdeResponse).stream().sorted((p1, p2) -> {
            return p1.getRole().getFromJDE() ? -1 : 1;
        }).collect(Collectors.toList());
    }

    public List<SitePerson> filterOutJdePersons(final List<SitePersonDTO> sitePersonDTOs) {
        sitePersonDTOs.stream().filter(p -> !p.getRole().getFromJDE()).forEach(p -> {
            p.setExternalId(null);
        });
        sitePersonDTOs.stream().filter(p -> p.getRole().getFromJDE()).forEach(p -> {
            p.setName(null);
        });
        return Lists.newArrayList(sitePersonDTOs.stream().map(p -> {
            return SiteMergeUtil.from(p).create(SitePerson.class);
        }).iterator());
    }

    public List<SitePersonDTO> mergeSitePerson(final List<SitePerson> sitePersonEntities, final JDEResponse jdeResponse) {
        final List<SitePersonDTO> sitePersonDTOs = Lists.newArrayList(sitePersonEntities.stream().
                filter(p -> !p.getRole().getFromJDE()).
                map(p -> {
                    SitePersonDTO sitePersonDTO = null;
                    sitePersonDTO = new SitePersonDTO();
                    final SitePersonRoleDTO roleDTO = new SitePersonRoleDTO();
                    roleDTO.setFromJDE(p.getRole().getFromJDE());
                    roleDTO.setLabel(p.getRole().getLabel());
                    roleDTO.setName(p.getRole().getName());
                    //TODO: @piotr replace with to merge tool
                    sitePersonDTO.setRole(roleDTO);
                    sitePersonDTO.setPhone(p.getPhone());
                    sitePersonDTO.setPhonecc(p.getPhonecc());
                    sitePersonDTO.setEmail(p.getEmail());
                    sitePersonDTO.setName(p.getName());
                    return sitePersonDTO;
                }).iterator());

        final SitePersonDTO asm = getSitePersonFromJde(
                sitePersonsRoleDao.getByName("ASM"),
                jdeResponse.getAreaSalesManagerName(),
                jdeResponse.getAreaSalesManager(),
                sitePersonEntities.stream().filter(p -> "ASM".equals(p.getRole().getName())).findFirst());
        final SitePersonDTO sm = getSitePersonFromJde(
                sitePersonsRoleDao.getByName("SM"),
                jdeResponse.getDealer_StationmanName(),
                jdeResponse.getDealer_Stationman(),
                sitePersonEntities.stream().filter(p -> "SM".equals(p.getRole().getName())).findFirst());

        sitePersonDTOs.add(asm);
        sitePersonDTOs.add(sm);
        return sitePersonDTOs;
    }

    private SitePersonDTO getSitePersonFromJde(final SitePersonRole role, final String namePath, final String idPath, final Optional<SitePerson> sitePersonEntity) {
        final SitePersonDTO sitePersonDTO = new SitePersonDTO();
        sitePersonDTO.setName(namePath);
        sitePersonDTO.setExternalId(idPath);
        final SitePersonRoleDTO roleDTO = new SitePersonRoleDTO();
        roleDTO.setFromJDE(role.getFromJDE());
        roleDTO.setLabel(role.getLabel());
        roleDTO.setName(role.getName());
        sitePersonDTO.setRole(roleDTO);
        sitePersonEntity.ifPresent(p -> {
            sitePersonDTO.setPhone(p.getPhone());
            sitePersonDTO.setPhonecc(p.getPhonecc());
            sitePersonDTO.setEmail(p.getEmail());
        });
        return sitePersonDTO;
    }

    /**
     * Saves a Site entity.
     *
     * @param siteId
     * @throws DBException
     */
    @Transactional
    public void save(final Long siteId, final List<SitePersonDTO> sitePersonDTOs) throws DBException, EntityNotFoundException {
        final Site site = siteDao.getById(siteId);
        if (site.getSitePersons() == null) {
            site.setSitePersons(new ArrayList<>());
        } else {
            site.getSitePersons().clear();
        }
        for (final SitePerson sitePerson : filterOutJdePersons(sitePersonDTOs)) {
            sitePerson.setSite(site);
            sitePersonsDao.save(sitePerson);
        }
    }

    public SitePersonRole getRole(final String name) {
        return sitePersonsRoleDao.getByName(name);
    }

    public List<SitePersonRole> getAllRoles() throws DBException {
        return sitePersonsRoleDao.findAll();
    }

}
