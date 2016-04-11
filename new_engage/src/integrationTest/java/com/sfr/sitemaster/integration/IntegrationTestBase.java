/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration;

import com.sfr.apicore.auth.AuthModule;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;
import com.sfr.sitemaster.app.SitemasterPropertyService;
import com.sfr.sitemaster.injection.guice.CoreServiceModule;
import com.sfr.sitemaster.injection.guice.DAOModule;
import com.sfr.sitemaster.injection.guice.DBModule;
import com.sfr.sitemaster.injection.guice.DomainServiceModule;
import org.junit.Before;

public class IntegrationTestBase {
    public static void setup() {
        final SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();
        final SFRInjector injector = new GuiceSFRInjector(new CoreServiceModule(sitemasterPropertyService), new AuthModule(), new DBModule(sitemasterPropertyService), new DAOModule(),
                new DomainServiceModule());

        InjectionService.registerServiceInjector(injector);
    }

    @Before
    public void _init() throws DBException {
        InjectionService.getInjector().injectMembers(this);
    }
}
