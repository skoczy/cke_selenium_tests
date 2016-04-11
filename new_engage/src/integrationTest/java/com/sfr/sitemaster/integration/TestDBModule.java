/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration;


/**
 * Test configuration for integration test db.
 * 
 * @author yves
 * 
 */
public class TestDBModule extends apicore.injection.guice.AbstractJpaDBModule {
    public TestDBModule() {
        super("sitemasterPU");

    }
}
