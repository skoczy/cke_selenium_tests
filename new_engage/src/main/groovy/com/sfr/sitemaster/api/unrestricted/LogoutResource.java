/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.api.unrestricted;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * logout api.
 * 
 * @author yves
 * 
 */
@Path("unres/logout")
@Api("authentication")
public class LogoutResource {
    @POST
    @Produces(MediaType.TEXT_PLAIN)
    @ApiOperation(value = "Performs a user logout.",
            notes = "Performs a user logout and invalidates a session assigned to a user.")
    public Response logout(final @Context HttpServletRequest req) {
        final HttpSession httpSession = req.getSession(false);
        if (httpSession != null) {
            httpSession.invalidate();
        }
        return Response.ok().build();
    }
}