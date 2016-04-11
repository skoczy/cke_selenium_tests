/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices.impl;

import com.google.inject.Inject;
import com.sfr.apicore.auth.ExternalAuthentication;
import com.sfr.apicore.auth.User;
import com.sfr.apicore.auth.exceptions.ExternalAuthenticationFailureException;
import com.sfr.sitemaster.domainservices.UserService;

/**
 * User domainservices impl using mongo
 *
 * @author yves
 */
public class UserServiceImpl implements UserService {

    private final ExternalAuthentication externalAuthentication;

    @Inject
    public UserServiceImpl(final ExternalAuthentication externalAuthentication) {
        this.externalAuthentication = externalAuthentication;
    }

    @Override
    public User login(final String email, final String password) throws ExternalAuthenticationFailureException {
        return externalAuthentication.getUser(email, password);
    }
}