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
import com.sfr.sitemaster.entities.Fuel;
import rx.Observable;

import java.util.List;

/**
 * Facade for User entity
 *
 * @author yves
 */
public interface FuelDao extends BasicDao<Long, Fuel> {
    List<Fuel> find(Long... id) throws DBException;

    List<Fuel> getFuelsForSite(final Long siteId);

    Observable<Fuel> getObservableFuelsForSite(final Long siteId);
}