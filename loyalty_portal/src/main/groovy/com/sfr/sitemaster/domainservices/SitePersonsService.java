/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dto.SitePersonDTO;
import com.sfr.sitemaster.entities.SitePersonRole;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;

import java.util.List;

/**
 * Facade for Site entity
 *
 * @author yves
 */
public interface SitePersonsService {

    List<SitePersonDTO> findAll(Long siteId);

    SitePersonDTO get(Long siteId, Long id);

    void save(Long siteId, List<SitePersonDTO> person) throws DBException, EntityNotFoundException;

    SitePersonRole getRole(String name);

    List<SitePersonRole> getAllRoles() throws DBException;
}
