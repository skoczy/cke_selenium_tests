/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.inject.Inject;
import com.sfr.apicore.api.unrestricted.SFRBaseResource;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.apicore.exceptions.pojo.InvalidIdException;
import com.sfr.sitemaster.commons.DtoMetaGenerator;
import com.sfr.sitemaster.domainservices.SiteService;
import com.sfr.sitemaster.dto.SiteDTO;
import com.sfr.sitemaster.entities.ChangeSetDTO;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.JDEException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import com.sfr.sitemaster.exceptions.SearchFilterException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.SwaggerDefinition;
import io.swagger.annotations.Tag;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import java.util.List;

/**
 * Site REST endpoint.
 *
 * @author piotr
 */
@Path("res/site/")
@Api("site")
@SwaggerDefinition(
        tags = {
                @Tag(name = "site", description = "General information about sites.")
        }
)
public class SiteResource extends SFRBaseResource {

    @Inject
    SiteService siteService;

    @GET
    @Produces({"application/json"}) //NOPMD
    @Timed
    @ApiOperation(value = "Returns all sites.",
            notes = "Returns list of all sites.",
            responseContainer = "list",
            response = SiteDTO.class
    )
    public List<SiteDTO> findAll(@Context final HttpServletRequest req) throws DBException, InvalidIdException, EntityNotFoundException, NoResultsException {
        return siteService.findAll();
    }

    @GET
    @Path("{entity_id}")
    @Produces({"application/json"}) //NOPMD
    @Timed
    @ExceptionMetered(name = "JDE Exception", cause = JDEException.class)
    @ApiOperation(value = "Returns site by id.", response = SiteDTO.class)
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "There is no site with such id.")
    })
    public SiteDTO getById(@PathParam("entity_id") final Long id, @Context final HttpServletRequest req) throws DBException, InvalidIdException, EntityNotFoundException {
        final SiteDTO siteDTO = siteService.get(id);
        siteDTO.setMeta(DtoMetaGenerator.generate(SiteDTO.class));
        return siteDTO;
    }

    @PUT
    @Path("{entity_id}")
    @Consumes({"application/json"})
    @Produces({"application/json"})
    @Timed
    @ApiOperation(value = "Updates a particular site.")
    public void update(final @Context HttpServletRequest request, final @PathParam("entity_id") Long id, @Valid final SiteDTO site, final @Context HttpServletRequest req) throws DBException, EntityNotFoundException, JsonProcessingException {
        siteService.save((PersistentSession) request.getAttribute("persistentSession"), site);
    }

    @POST
    @Consumes({"application/json"}) //NOPMD
    @Produces({"application/json"}) //NOPMD
    @Path("search")
    @Timed
    public Response search(final List<SearchFilter> searchFilters) throws SearchFilterException {
        return Response.ok().entity(siteService.search(searchFilters)).build();
    }

    @GET
    @Path("{entity_id}/changeLog")
    @Produces({"application/json"}) //NOPMD
    @Timed
    @ApiOperation(value = "Returns a changelog for a given site.", response = SiteDTO.class)
    public List<ChangeSetDTO> getChangesForSite(@PathParam("entity_id") final Long id, @Context final HttpServletRequest req) throws DBException, InvalidIdException, EntityNotFoundException {
        return siteService.getChangeSets(id);
    }
}
