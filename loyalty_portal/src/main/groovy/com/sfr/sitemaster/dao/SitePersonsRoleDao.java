/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao;

import com.sfr.apicore.dao.BasicDao;
import com.sfr.sitemaster.entities.SitePersonRole;


/**
 * Facade for User entity
 *
 * @author yves
 */
public interface SitePersonsRoleDao extends BasicDao<Long, SitePersonRole> {
    SitePersonRole getByName(final String name);
}
