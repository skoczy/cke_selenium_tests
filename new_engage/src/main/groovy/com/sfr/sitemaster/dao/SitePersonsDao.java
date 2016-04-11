/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao;

import com.sfr.apicore.dao.BasicDao;
import com.sfr.sitemaster.entities.SitePerson;
import rx.Observable;

import java.util.List;

/**
 * Facade for User entity
 *
 * @author yves
 */
public interface SitePersonsDao extends BasicDao<Long, SitePerson> {
    List<SitePerson> getSitePersonsForSite(final Long siteId);
    Observable<SitePerson> getObservableSitePersonsForSite(final Long siteId);

    SitePerson getSitePerson(final Long siteId, final Long id);
}
