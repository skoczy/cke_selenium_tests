/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.unrestricted;

import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.auth.User;
import com.sfr.apicore.auth.exceptions.ExternalAuthenticationFailureException;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.api.filters.AuthFilter;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.dto.UserDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

/**
 * Login API support
 *
 * @author yves
 */
@Path("unres/login")
@Api("authentication")
@SwaggerDefinition(
        tags = {
                @Tag(name = "authentication", description = "Performs login/logout.")
        }
)
public class LoginResource extends SFRBaseResource {
    @Inject
    private UserService facade;

    @Inject
    private PersistentSessionService persistentSessionService;

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "Performs a user authentication.",
            notes = "Returns a user as JSON object.",
            response = UserDTO.class
    )
    @ApiImplicitParams({
            @ApiImplicitParam(name = AuthFilter.USERNAME_PARAM, required = true, dataType = "string", paramType = "form"),
            @ApiImplicitParam(name = AuthFilter.PASSWD_PARAM, required = true, dataType = "string", paramType = "form")
    })
    public UserDTO login(final @Context HttpServletRequest req, final MultivaluedMap<String, String> form) throws ExternalAuthenticationFailureException, DBException {
        // Check if user is already authenticated, if so respond with 200
//        if (persistentSessionService.isSessionValid(req)) {
//            return Response.ok().build();
//        }

        // Otherwise do authentication
//        if (form == null || form.getFirst(AuthFilter.USERNAME_PARAM) == null || form.getFirst(AuthFilter.PASSWD_PARAM) == null) {
//            return Response.status(401).entity("No username or password supplied").build();
//        }
        final String email = form.getFirst(AuthFilter.USERNAME_PARAM);
        final String password = form.getFirst(AuthFilter.PASSWD_PARAM);

        final User user = facade.login(email, password);
        final HttpSession newLocalSession = req.getSession(true);
        persistentSessionService.createNewPersistenSession(user, newLocalSession.getId());
        final UserDTO userDTO = new UserDTO();
        userDTO.setEmail(user.getEmail());
        userDTO.setRoles(user.getRoles());
        return userDTO;
    }
}
