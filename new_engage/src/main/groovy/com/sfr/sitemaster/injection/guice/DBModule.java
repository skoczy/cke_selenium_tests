/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import apicore.injection.guice.JpaInitializer;
import com.google.inject.Inject;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.sfr.apicore.commons.PropertyService;

import java.util.Arrays;
import java.util.Properties;

/**
 * Configuration of the DB module
 *
 * @author yves
 */
public class DBModule extends apicore.injection.guice.AbstractJpaDBModule {
    public static final String DEFAULT_PU = "sitemasterPU";
    private String pu;
    final PropertyService propertyService;

    public DBModule(final PropertyService propertyService) {
        this(propertyService, DEFAULT_PU);
    }

    public DBModule(final PropertyService propertyService, final String pu) {
        super(pu);
        this.pu = pu;
        this.propertyService = propertyService;
    }

    @Inject
    protected void configure() {
        final Properties jpaProperties = new Properties();
        //TODO: make it better @piotr
        Arrays.asList("hibernate.connection.username", "hibernate.connection.password", "hibernate.connection.url", "hibernate.hbm2ddl.auto").stream().forEach(p -> {
            final String prop = propertyService.getProperty(p);
            if (prop != null) {
                jpaProperties.setProperty(p, propertyService.getProperty(p));
            }
        });
        this.install(new JpaPersistModule(pu).properties(jpaProperties));
        this.bind(JpaInitializer.class).asEagerSingleton();

    }
}
