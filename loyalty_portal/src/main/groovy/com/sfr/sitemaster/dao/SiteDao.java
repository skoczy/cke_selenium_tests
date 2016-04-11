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
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.SearchFilterException;
import rx.Observable;

import java.util.List;

/**
 * Facade for User entity
 *
 * @author yves
 */
public interface SiteDao extends BasicDao<Long, Site> {
    Site getById(Long id) throws DBException, EntityNotFoundException;

    Observable<Site> getObservableById(Long id) throws DBException, EntityNotFoundException;

    Observable<Site> getObservableByIdAndRev(Long id, Number rev) throws DBException, EntityNotFoundException;

    List<Site> search(List<SearchFilter> searchFilters) throws SearchFilterException;

    List getRevisions(final Long id) throws DBException;
}
