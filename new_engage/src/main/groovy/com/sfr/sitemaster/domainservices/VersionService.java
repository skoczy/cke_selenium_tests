/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import java.io.IOException;

public interface VersionService {

    String getAppVersion() throws IOException;

    String getAppTimestamp() throws IOException;

    String getAPIVersion() throws IOException;

    String getAppName() throws IOException;
}
