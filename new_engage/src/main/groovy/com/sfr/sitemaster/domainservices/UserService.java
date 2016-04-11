/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.apicore.auth.User;
import com.sfr.apicore.auth.exceptions.ExternalAuthenticationFailureException;

/**
 * Facade for User entity
 *
 * @author yves
 *
 */
public interface UserService {

    User login(String email, String password) throws ExternalAuthenticationFailureException;

}
