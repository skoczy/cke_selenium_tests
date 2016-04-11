/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.impl;

import apicore.dao.jpa.BaseJpaDao;
import com.google.inject.Inject;
import com.mysema.query.types.path.NumberPath;
import com.sfr.sitemaster.dao.SitePersonsRoleDao;
import com.sfr.sitemaster.entities.QSitePersonRole;
import com.sfr.sitemaster.entities.SitePersonRole;

import static com.sfr.sitemaster.entities.QSitePerson.sitePerson;
import static com.sfr.sitemaster.entities.QSitePersonRole.sitePersonRole;

/**
 * Site DAO
 *
 * @author piotr
 */
public class SitePersonsRoleDAOImpl extends BaseJpaDao<SitePersonRole, QSitePersonRole> implements SitePersonsRoleDao {

    @Inject
    public SitePersonsRoleDAOImpl() {
        super(sitePersonRole);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return sitePerson.id;
    }


    @Override
    public SitePersonRole getByName(final String name) {
        return this.query().from(sitePersonRole).where(sitePersonRole.name.eq(name)).singleResult(sitePersonRole);
    }
}
