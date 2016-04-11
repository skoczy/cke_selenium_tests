/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.app

/**
 * Created by piotr on 05/01/16.
 */
class AppProperties {

    private static Properties properties

    static {
        properties = new Properties()
        properties.load(AppProperties.class.classLoader.getResourceAsStream('sitemaster.properties'))
    }

    static String get(key) {
        properties.getProperty(key)
    }
}
