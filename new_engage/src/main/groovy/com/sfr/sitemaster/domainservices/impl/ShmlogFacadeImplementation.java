/**
 * Copyright (c) 2014 ~ 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.sfr.sitemaster.domainservices.ShmlogFacadeInterface;

/**
 * implementation for Shmlog facade.
 * 
 * @author yves
 * 
 */
public class ShmlogFacadeImplementation implements ShmlogFacadeInterface {

    private final Shmlog shmlog = Shmlog.getInstance();

    @Override
    public int write(final String entry) {
        return shmlog.write(entry);
    }

    @Override
    public String[] read() {
        return shmlog.read();
    }

    @Override
    public int getBufferSize() {
        return shmlog.getBufferSize();
    }

}
