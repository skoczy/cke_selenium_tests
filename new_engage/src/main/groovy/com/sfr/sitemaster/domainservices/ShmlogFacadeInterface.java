/**
 * Copyright (c) 2014 ~ 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

/**
 * Interface for shmlog.
 * 
 * @author yves
 * 
 */
public interface ShmlogFacadeInterface {

    int write(String entry);

    String[] read();

    int getBufferSize();
}
