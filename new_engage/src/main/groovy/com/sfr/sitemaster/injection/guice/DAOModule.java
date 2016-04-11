/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import com.google.inject.AbstractModule;
import com.sfr.sitemaster.dao.FuelDao;
import com.sfr.sitemaster.dao.PersistentSessionDao;
import com.sfr.sitemaster.dao.PredefinedSearchDao;
import com.sfr.sitemaster.dao.ServiceDao;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.dao.SitePersonsDao;
import com.sfr.sitemaster.dao.SitePersonsRoleDao;
import com.sfr.sitemaster.dao.impl.FuelDAOImpl;
import com.sfr.sitemaster.dao.impl.PersistentSessionDAOImpl;
import com.sfr.sitemaster.dao.impl.PredefinedSearchDAOImpl;
import com.sfr.sitemaster.dao.impl.ServiceDAOImpl;
import com.sfr.sitemaster.dao.impl.SiteDAOImpl;
import com.sfr.sitemaster.dao.impl.SitePersonsDAOImpl;
import com.sfr.sitemaster.dao.impl.SitePersonsRoleDAOImpl;

public class DAOModule extends AbstractModule {
    @Override
    protected void configure() {
        // Engage DAO Objects that shields client code from Morphia and Mongo
        bind(PersistentSessionDao.class).to(PersistentSessionDAOImpl.class);
        bind(SiteDao.class).to(SiteDAOImpl.class);
        bind(SitePersonsDao.class).to(SitePersonsDAOImpl.class);
        bind(ServiceDao.class).to(ServiceDAOImpl.class);
        bind(FuelDao.class).to(FuelDAOImpl.class);
        bind(SitePersonsRoleDao.class).to(SitePersonsRoleDAOImpl.class);
        bind(PredefinedSearchDao.class).to(PredefinedSearchDAOImpl.class);
    }
}
