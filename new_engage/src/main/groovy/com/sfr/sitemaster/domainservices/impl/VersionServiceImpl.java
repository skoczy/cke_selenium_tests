/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.sfr.sitemaster.domainservices.VersionService;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class VersionServiceImpl implements VersionService {

    @Override
    public String getAppVersion() throws IOException {
        return readPropertyFile().getProperty("app_version");
    }

    @Override
    public String getAppTimestamp() throws IOException {
        return readPropertyFile().getProperty("app_timestamp");
    }

    @Override
    public String getAPIVersion() throws IOException {
        return readPropertyFile().getProperty("api_version");
    }

    @Override
    public String getAppName() throws IOException {
        return readPropertyFile().getProperty("app_name");
    }

    private Properties readPropertyFile() throws IOException {
        final Properties prop = new Properties();
        final String propFileName = "sitemaster.properties";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(propFileName);
        prop.load(is);
        if (is == null) {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }
        is.close();
        return prop;
    }
}
