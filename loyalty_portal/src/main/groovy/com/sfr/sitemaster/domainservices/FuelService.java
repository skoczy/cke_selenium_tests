/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dto.ServiceDTO;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;

import java.util.List;

/**
 * Facade for Site entity
 *
 * @author yves
 */
public interface FuelService {

    List<ServiceDTO> findAll() throws DBException;

    ServiceDTO get(Long fuelId) throws DBException;

    List<ServiceDTO> getFuelsForSite(Long siteId);

    void save(Long siteId, List<ServiceDTO> fuelDTOList) throws DBException, EntityNotFoundException;
}
