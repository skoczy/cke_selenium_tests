/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.injection.guice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.inject.AbstractModule;
import com.sfr.apicore.commons.PropertyService;
import com.sfr.sitemaster.domainservices.ShmlogFacadeInterface;
import com.sfr.sitemaster.domainservices.impl.ShmlogFacadeImplementation;
import com.sfr.sitemaster.rx.ObservableSOAPClientFactory;

import static com.google.inject.Scopes.SINGLETON;

/**
 * Facade module
 *
 * @author yves
 */
public class CoreServiceModule extends AbstractModule {
    final PropertyService propertyService;

    public CoreServiceModule(final PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    @Override
    protected void configure() {
        bind(PropertyService.class).toInstance(propertyService);
        bind(ShmlogFacadeInterface.class).to(ShmlogFacadeImplementation.class).in(SINGLETON);
        bind(ObjectMapper.class).toProvider(() -> {
            final ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
            objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
            objectMapper.registerModule(new JavaTimeModule());
            return objectMapper;
        });
        bind(ObservableSOAPClientFactory.class).toProvider(()-> {
            return new ObservableSOAPClientFactory();
        });
    }
}
