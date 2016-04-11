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
import com.sfr.sitemaster.dao.FuelDao;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.domainservices.FuelService;
import com.sfr.sitemaster.dto.ServiceDTO;
import com.sfr.sitemaster.entities.Fuel;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Site Service responsible for dealing with Sites.
 *
 * @author piotr
 */
public class FuelServiceImpl implements FuelService {

    private final FuelDao fuelDao;
    private final SiteDao siteDao;

    @Inject
    public FuelServiceImpl(final SiteDao siteDao, final FuelDao fuelDao) {
        this.fuelDao = fuelDao;
        this.siteDao = siteDao;
    }

    /**
     * Retrieves all Fuels entites and converts them to DTOs.
     *
     * @return List of Fuel DTO objects
     * @throws DBException
     */
    @Transactional
    public List<ServiceDTO> findAll() throws DBException {
        return fuelDao.findAll().stream().map(this::toDTO).collect(Collectors.toList());
    }


    /**
     * Retrieves a single Fuel entity and converts it to DTO object.
     *
     * @return Single Fuel DTO object
     * @throws DBException
     */
    @Transactional
    public ServiceDTO get(final Long serviceId) throws DBException {
        return toDTO(fuelDao.find(serviceId));
    }


    /**
     * Retrieves all Fuel entities for a particular site and converts them to DTO objects.
     *
     * @return List of fuel DTO objects
     */
    @Transactional
    public List<ServiceDTO> getFuelsForSite(final Long siteId) {
        return fuelDao.getFuelsForSite(siteId).stream()
                .map(this::toDTO).collect(Collectors.toList());
    }

    private ServiceDTO toDTO(final Fuel fuel) {
        final ServiceDTO fuelDTO = new ServiceDTO();
        fuelDTO.setId(fuel.getId());
        fuelDTO.setType(fuel.getType());
        fuelDTO.setName(fuel.getDisplayName());
        fuelDTO.setIcon(fuel.getIcon());
        fuelDTO.setLang(fuel.getLang());
        return fuelDTO;
    }

    /**
     * Updates fuels list for a particular site.
     *
     * @param siteId
     * @param fuelDTOList List of fuel DTO objects
     * @throws DBException
     * @throws EntityNotFoundException
     */
    @Transactional
    public void save(final Long siteId, final List<ServiceDTO> fuelDTOList) throws DBException, EntityNotFoundException {
        final Site site = siteDao.getById(siteId);
        if (site.getFuels() == null) {
            site.setFuels(new ArrayList<>());
        } else {
            site.getFuels().clear();
        }
        for (final ServiceDTO serviceDTO : fuelDTOList) {
            site.getFuels().add(fuelDao.find(serviceDTO.getId()));
        }
        siteDao.save(site);
    }
}
