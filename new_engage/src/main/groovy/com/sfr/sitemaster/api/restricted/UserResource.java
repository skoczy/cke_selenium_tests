/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.google.inject.Inject;
import com.sfr.sitemaster.domainservices.UserService;

import javax.ws.rs.Path;

/**
 * The root url for the unrestricted API
 * 
 * @author yves
 * 
 */
@Path("res/user/")
public class UserResource  {

    @Inject
    private UserService userService; //NOPMD, TBC

}
