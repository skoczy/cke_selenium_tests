/**
 * Copyright (c) 2014 ~ 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.sitemaster.domainservices.ShmlogFacadeInterface;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * exposing a GET routine for in-memory log for tracking all api transactions.
 *
 * @author yves
 *
 */
@Path("res/log")
public class ShmlogResource extends SFRBaseResource {

    @Inject
    private ShmlogFacadeInterface shmlogFacade;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response log(final String obj) {
        System.out.println(obj);
        shmlogFacade.write(obj);
        return Response.status(200).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response read(final @Context HttpServletRequest req) {
        final String[] logs = shmlogFacade.read();
        final JSONObject obj = toJSON(logs);
        return Response.status(200).entity(obj.toString()).build();
    }

    private JSONObject toJSON(final String... logs) throws JSONException {
        final JSONObject response = new JSONObject();
        final JSONArray array = new JSONArray();
        for (final String log : logs) {
            if (log != null) {
                array.put(new JSONObject(log)); // NOPMD
            }
        }
        response.put("logs", array);
        return response;
    }
}
