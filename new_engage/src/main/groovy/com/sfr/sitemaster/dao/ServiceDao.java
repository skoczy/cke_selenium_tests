/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao;

import com.sfr.apicore.dao.BasicDao;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.entities.Service;
import rx.Observable;

import java.util.List;

/**
 * Facade for User entity
 *
 * @author yves
 */
public interface ServiceDao extends BasicDao<Long, Service> {
    List<Service> find(Long... id) throws DBException;

    List<Service> getServicesForSite(final Long siteId);

    Observable<Service> getObservableServicesForSite(final Long siteId);
}