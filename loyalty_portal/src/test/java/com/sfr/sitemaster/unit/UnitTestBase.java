/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.unit;

import com.sfr.apicore.commons.PropertyService;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;
import com.sfr.sitemaster.injection.guice.CoreServiceModule;
import com.sfr.sitemaster.shared.TestBase;
import org.mockito.Mockito;

public class UnitTestBase extends TestBase {
    protected final static PropertyService propertyService;

    static {
        propertyService = Mockito.mock(PropertyService.class);
        final SFRInjector injector = new GuiceSFRInjector(new CoreServiceModule(propertyService));
        InjectionService.registerServiceInjector(injector);
    }
}
