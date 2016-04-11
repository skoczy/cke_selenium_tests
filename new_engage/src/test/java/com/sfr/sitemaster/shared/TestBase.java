/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.shared;

import org.junit.Before;

import com.sfr.apicore.injection.InjectionService;

/**
 * Base unit test class. All unit test should inherit this.
 * 
 * @author yves
 * 
 */
public class TestBase {

    @Before
    public void _init() {
        InjectionService.getInjector().injectMembers(this);
    }
}
