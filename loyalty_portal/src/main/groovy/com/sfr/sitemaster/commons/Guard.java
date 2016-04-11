/**
 * Copyright (c) 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.commons;

/**
 * Created by tuxbear on 13/01/15.
 */
public final class Guard {

    private Guard() {
        // utility class with private constructor
    }

    public static void throwIfNull(final Object object, final Class<? extends Exception> exceptionClass) {
        if (object == null) {
            try {
                final Exception ex = exceptionClass.newInstance();
                throw ex;
            } catch (Exception e) {
                throw new IllegalArgumentException("Given exception type cannot be thrown", e);
            }
        }
    }
}
