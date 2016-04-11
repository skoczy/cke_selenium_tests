/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dto.SiteDTO;
import com.sfr.sitemaster.entities.ChangeSetDTO;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import com.sfr.sitemaster.exceptions.SearchFilterException;

import javax.validation.ValidationException;
import java.util.List;

/**
 * Facade for Site entity
 *
 * @author yves
 */
public interface SiteService {

    SiteDTO get(Long id) throws DBException, EntityNotFoundException;

    List<SiteDTO> findAll() throws DBException, NoResultsException;

    void save(PersistentSession persistentSession, SiteDTO site) throws DBException, EntityNotFoundException, JsonProcessingException, ValidationException;

    List<Site> search(List<SearchFilter> searchFilters) throws SearchFilterException;

    List<ChangeSetDTO> getChangeSets(final Long id) throws DBException, EntityNotFoundException;
}
