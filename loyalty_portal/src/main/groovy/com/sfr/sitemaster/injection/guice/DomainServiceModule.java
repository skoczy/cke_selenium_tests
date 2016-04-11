/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import com.google.gson.Gson;
import com.google.inject.AbstractModule;
import com.sfr.sitemaster.domainservices.EmailSenderService;
import com.sfr.sitemaster.domainservices.FuelService;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.domainservices.ServiceService;
import com.sfr.sitemaster.domainservices.SitePersonsService;
import com.sfr.sitemaster.domainservices.SiteService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.domainservices.VersionService;
import com.sfr.sitemaster.domainservices.impl.DummyEmailFacade;
import com.sfr.sitemaster.domainservices.impl.FuelServiceImpl;
import com.sfr.sitemaster.domainservices.impl.PersistentSessionServiceImpl;
import com.sfr.sitemaster.domainservices.impl.ServiceServiceImpl;
import com.sfr.sitemaster.domainservices.impl.SitePersonsServiceImpl;
import com.sfr.sitemaster.domainservices.impl.SiteServiceImpl;
import com.sfr.sitemaster.domainservices.impl.UserServiceImpl;
import com.sfr.sitemaster.domainservices.impl.VersionServiceImpl;
import com.sfr.sitemaster.integration.JdeWsClient;
import com.sfr.sitemaster.integration.impl.JdeWsClientImpl;

import static com.google.inject.Scopes.SINGLETON;

/**
 * Facade module
 *
 * @author yves
 */
public class DomainServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(Gson.class).in(SINGLETON);
        bind(VersionService.class).to(VersionServiceImpl.class).in(SINGLETON);
        bind(EmailSenderService.class).to(DummyEmailFacade.class).in(SINGLETON);
        bind(PersistentSessionService.class).to(PersistentSessionServiceImpl.class).in(SINGLETON);
        bind(UserService.class).to(UserServiceImpl.class).in(SINGLETON);
        bind(SiteService.class).to(SiteServiceImpl.class).in(SINGLETON);
        bind(ServiceService.class).to(ServiceServiceImpl.class).in(SINGLETON);
        bind(FuelService.class).to(FuelServiceImpl.class).in(SINGLETON);
        bind(SitePersonsService.class).to(SitePersonsServiceImpl.class).in(SINGLETON);
        bind(JdeWsClient.class).to(JdeWsClientImpl.class).in(SINGLETON);
    }
}
