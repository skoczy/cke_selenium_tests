/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.impl;

import apicore.dao.jpa.BaseJpaDao;
import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.PredefinedSearchDao;
import com.sfr.sitemaster.entities.PredefinedSearch;
import com.sfr.sitemaster.entities.QPredefinedSearch;
import com.sfr.sitemaster.entities.SearchFilter;
import org.json.JSONException;
import org.json.JSONObject;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import javax.validation.ValidationException;
import java.io.IOException;

import static com.sfr.sitemaster.entities.QPredefinedSearch.predefinedSearch;

/**
 * Site DAO
 *
 * @author piotr
 */
public class PredefinedSearchDAOImpl extends BaseJpaDao<PredefinedSearch, QPredefinedSearch> implements PredefinedSearchDao {
    @Inject
    private Provider<EntityManager> entityManager;

    @Inject
    public PredefinedSearchDAOImpl() {
        super(predefinedSearch);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return predefinedSearch.id;
    }

    @Override
    public PredefinedSearch save(final PredefinedSearch entity) throws DBException, ValidationException {
        if (entity.getSearchFilters() != null) {
            for (final SearchFilter searchFilter : entity.getSearchFilters()) {
                searchFilter.setPredefinedSearch(entity);
            }
        }
        return super.save(entity);
    }

    @Override
    public long removeAll() throws DBException {
        //TODO: @piotr It looks like QueryDSL ignores orphanRemoval/cascade.all while removing entity
        for (final PredefinedSearch predefinedSearch : findAll()) {
            this.entityManager.get().remove(predefinedSearch);
        }
        return 0l;
    }

    @Override
    public JSONObject serializeEntityToJSON(final JpaSFREntityObject saved) throws JSONException {
        final ObjectMapper mapper = new ObjectMapper();
        JSONObject json = null;
        try {
            json = new JSONObject(mapper.writeValueAsString(saved));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    @Override
    public PredefinedSearch serializeJSONToEntity(final JSONObject obj, final Class<PredefinedSearch> clazz) throws JSONException, SecurityException, IllegalArgumentException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonFactory jfactory = new JsonFactory();
        PredefinedSearch predefinedSearch = null;
        try {
            predefinedSearch = mapper.readValue(jfactory.createParser(obj.toString()), PredefinedSearch.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return predefinedSearch;
    }
}
