/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *  
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.restricted;

import com.google.inject.Inject;
import com.sfr.apicore.api.restricted.CRUDBaseResource;
import com.sfr.apicore.dao.BasicDao;
import com.sfr.sitemaster.dao.PredefinedSearchDao;
import com.sfr.sitemaster.entities.PredefinedSearch;

import javax.ws.rs.Path;

/**
 * Site REST endpoint.
 *
 * @author piotr
 */
@Path("res/predefinedSearches")
public class PredefinedSearchResource extends CRUDBaseResource<Long, PredefinedSearch> {

    @Inject
    private PredefinedSearchDao facade;

    @Override
    protected Class<PredefinedSearch> getEntityClass() {
        return PredefinedSearch.class;
    }

    @Override
    protected BasicDao<Long, PredefinedSearch> getFacade() {
        return facade;
    }

}
