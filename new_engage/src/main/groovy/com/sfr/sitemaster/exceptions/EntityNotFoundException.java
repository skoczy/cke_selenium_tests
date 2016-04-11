/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *  
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.exceptions;

import com.sfr.apicore.dao.SFREntityObject;

/**
 * Created by piotr on 13.08.15.
 */
public class EntityNotFoundException extends Exception {

    private static final long serialVersionUID = 5584885623369943783L;

    public EntityNotFoundException(final Class<? extends SFREntityObject<?>> clazz, final Object id) {
        super("No entity " + clazz.getSimpleName() + " found for id: " + id.toString());
    }

}
