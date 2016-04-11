/**
 * Copyright (c) 2014 ~ 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Shared memory rotational log for logging all tranactions through the API
 * 
 * @author yves
 * 
 */
public final class Shmlog {
    private static final String SHMLOG_SIZE_PROPERTY = "com.sfr.sitemaster.shmlog.size";
    private static final int DEFAULT_BUFFER_SIZE = 100;
    private static int BUFFER_SIZE = 100;

    private String[] buffer;
    private final AtomicInteger index;
    private volatile transient int size;

    private static Shmlog shmlog = new Shmlog(BUFFER_SIZE);

    static {
        if (System.getProperty(SHMLOG_SIZE_PROPERTY) != null) {
            try {
                BUFFER_SIZE = Integer.parseInt(System.getProperty(SHMLOG_SIZE_PROPERTY,
                        String.valueOf(DEFAULT_BUFFER_SIZE)));
            } catch (NumberFormatException ex) {
                BUFFER_SIZE = DEFAULT_BUFFER_SIZE;
            }
        }
    }

    public static Shmlog getInstance() {
        return shmlog;
    }

    private Shmlog(final int size) {
        this.size = size;
        buffer = new String[size];
        index = new AtomicInteger(0);
    }

    private int getCurrentIndex() {
        if (index.compareAndSet(this.size, 0)) {
            return index.get();
        } else {
            return index.getAndIncrement();
        }
    }

    public int write(final String log) {
        synchronized (this) {
            final int pos = getCurrentIndex();
            buffer[pos] = log;
            return pos;
        }
    }

    public String[] read() {
        // make a copy of the array
        synchronized (this) {
            return Arrays.copyOf(buffer, buffer.length, String[].class);
        }
    }

    public void resetIndex() {
        index.set(0);
    }

    public int getBufferSize() {
        return BUFFER_SIZE;
    }
}
