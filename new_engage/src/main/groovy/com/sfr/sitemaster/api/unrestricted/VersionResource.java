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
import com.sfr.apicore.commons.JSONResponseUtils;
import com.sfr.sitemaster.domainservices.VersionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

/**
 * The root url for the unrestricted API
 *
 * @author yves
 */
@Path("unres/")
@Api("version") //NOPMD
@SwaggerDefinition(
        tags = {
                @Tag(name = "version", description = "Meta information about the app.")
        }
)
public class VersionResource extends SFRBaseResource {

    @Inject
    private VersionService facade;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("versionjson")
    @ApiOperation(value = "Returns current application version as a json.")
    public Response version_json() throws IOException {
        final JSONObject obj = new JSONObject();
        obj.put("version", facade.getAppVersion());
        obj.put("name", facade.getAppName());
        obj.put("timestamp", facade.getAppTimestamp());
        obj.put("api_version", facade.getAPIVersion());
        return Response.status(200).entity(obj.toString()).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("version") //NOPMD
    @ApiOperation(value = "Returns current application version as a string.")
    public Response version() {
        final StringBuilder builder = new StringBuilder(41);
        try {
            builder.append("version=" + facade.getAppVersion() + ", name="
                    + facade.getAppName() + ", timestamp="
                    + facade.getAppTimestamp() + ", api_version="
                    + facade.getAPIVersion());
            return Response.status(200).entity(builder.toString()).build();
        } catch (IOException ex) {
            return JSONResponseUtils.buildJSONResponse(500, "IO error. Missing property files? "
                    + ex.getMessage());
        } catch (Exception ex) {
            return JSONResponseUtils.buildJSONResponse(500, ex.getMessage());
        }
    }
}
