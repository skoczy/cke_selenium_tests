/*
 * Copyright (c) 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.injection.guice;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;

import static com.google.inject.Scopes.SINGLETON;

/**
 * Guice module for metrics.
 *
 * @author piotr
 */
public class MetricModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(MetricRegistry.class).toProvider(()-> {
            return new MetricRegistry();
        }).in(SINGLETON);
    }
}
