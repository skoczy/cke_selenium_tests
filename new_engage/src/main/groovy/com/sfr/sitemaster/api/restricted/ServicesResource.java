/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.apicore.exceptions.pojo.InvalidIdException;
import com.sfr.sitemaster.domainservices.ServiceService;
import com.sfr.sitemaster.dto.ServiceDTO;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import java.util.List;

/**
 * Site REST endpoint.
 *
 * @author piotr
 */
@Path("res/service")
@Api("service")
@SwaggerDefinition(
        tags = {
                @Tag(name = "service", description = "General information provided services by sites.")
        }
)
public class ServicesResource extends SFRBaseResource {
    @Inject
    ServiceService servicesService;

    @GET
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Returns all services.",
            notes = "Returns list of services which are provided by sites.",
            responseContainer = "list",
            response = ServiceDTO.class
    )
    public List<ServiceDTO> findAll() throws DBException, InvalidIdException, EntityNotFoundException, NoResultsException { //NOPMD
        return servicesService.findAll();
    }

    @GET
    @Path("{entity_id}")
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Returns a service by id.", response = ServiceDTO.class)
    public ServiceDTO getById(@PathParam("entity_id") final Long id) throws DBException, InvalidIdException, EntityNotFoundException { //NOPMD
        return servicesService.get(id);
    }

    @GET
    @Path("site/{site_id}")
    @Produces({"application/json"})
    @ApiOperation(value = "Returns a list of services which are provided by a particular site.", response = ServiceDTO.class)
    public List<ServiceDTO> findBySite(@PathParam("site_id") final Long siteId) throws DBException, InvalidIdException, EntityNotFoundException, NoResultsException {
        return servicesService.getServicesForSite(siteId);
    }

    @PUT
    @Path("site/{site_id}")
    @Consumes({"application/json"}) //NOPMD
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Updates a list of services assigned to a particular site.")
    public void update(final @Context HttpServletRequest request, @PathParam("site_id") final Long siteId, @PathParam("entity_id") final Long id, final List<ServiceDTO> serviceList) throws DBException, EntityNotFoundException { //NOPMD
        servicesService.save((PersistentSession) request.getAttribute("persistentSession"), siteId, serviceList);
    }

    @POST
    @Path("site/{site_id}")
    @Consumes({"application/json"}) //NOPMD
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Updates a list of services assigned to a particular site.")
    public void save(final @Context HttpServletRequest request, @PathParam("site_id") final Long siteId, @PathParam("entity_id") final Long id, final List<ServiceDTO> serviceList) throws DBException, EntityNotFoundException { //NOPMD
        servicesService.save((PersistentSession) request.getAttribute("persistentSession"), siteId, serviceList);
    }
}
