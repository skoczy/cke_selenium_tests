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
import com.sfr.sitemaster.domainservices.SitePersonsService;
import com.sfr.sitemaster.dto.PersonDTO;
import com.sfr.sitemaster.dto.SitePersonDTO;
import com.sfr.sitemaster.exceptions.EntityNotFoundException;
import com.sfr.sitemaster.exceptions.NoResultsException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.List;

/**
 * Site REST endpoint.
 *
 * @author piotr
 */
@Path("res/site/{site_id}/sitepersons")
@Api("site")
public class SitePersonsResource extends SFRBaseResource {

    @Inject
    SitePersonsService sitePersonsService;

    @GET
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Returns list of site persons working at a particular site.", responseContainer = "list", response = PersonDTO.class)
    public List<SitePersonDTO> findAll(@PathParam("site_id") final Long siteId) throws DBException, InvalidIdException, EntityNotFoundException, NoResultsException { //NOPMD
        return sitePersonsService.findAll(siteId);
    }

    @GET
    @Path("{entity_id}")
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Returns a site person working at a given site.", response = PersonDTO.class)
    public SitePersonDTO getById(@PathParam("site_id") final Long siteId, @PathParam("entity_id") final Long id) throws DBException, InvalidIdException, EntityNotFoundException { //NOPMD
        return sitePersonsService.get(siteId, id);
    }

    @PUT
    @Consumes({"application/json"}) //NOPMD
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Updates a site person working at a given site.")
    public void update(@PathParam("site_id") final Long siteId, @PathParam("entity_id") final Long id, final List<SitePersonDTO> personList) throws DBException, EntityNotFoundException { //NOPMD
        sitePersonsService.save(siteId, personList);
    }

    @POST
    @Consumes({"application/json"}) //NOPMD
    @Produces({"application/json"}) //NOPMD
    @ApiOperation(value = "Adds a site person to a given site.")
    public void save(@PathParam("site_id") final Long siteId, @PathParam("entity_id") final Long id, final List<SitePersonDTO> personList) throws DBException, EntityNotFoundException { //NOPMD
        sitePersonsService.save(siteId, personList);
    }
}
