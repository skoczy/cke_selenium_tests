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
import com.google.inject.persist.Transactional;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.SiteDao;
import com.sfr.sitemaster.entities.QSite;
import com.sfr.sitemaster.entities.SearchFilter;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.exceptions.SearchFilterException;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.json.JSONException;
import org.json.JSONObject;
import rx.Observable;

import javax.inject.Provider;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.List;

import static com.sfr.sitemaster.entities.QSite.site;

/**
 * Site DAO
 *
 * @author piotr
 */
public class SiteDAOImpl extends BaseJpaDao<Site, QSite> implements SiteDao {
    @Inject
    private Provider<EntityManager> entityManager;

    @Inject
    public SiteDAOImpl() {
        super(site);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return site.id;
    }

    @Override
    public Site getById(final Long id) throws DBException {
        return find(id);
    }

    public List getRevisions(final Long id) throws DBException {
        if (!entityManager.get().getTransaction().isActive()) {
            entityManager.get().getTransaction().begin();
        }
        final AuditReader reader = AuditReaderFactory.get(entityManager.get());
        final List<Object[]> sites = reader.createQuery().forRevisionsOfEntity(Site.class, false, true).add(AuditEntity.id().eq(id)).getResultList();
        sites.stream().forEach(o -> {
            final Site e = (Site) o[0];
            e.getOpeningInfo().getOpeningTimes().size();
            e.getOpeningInfo().getTemporarilyClosed().size();
            e.getServices().size();
            e.getFuels().size();
            e.getSitePersons().size();
        });
        return sites;
    }

    public Site getByIdAndRev(final Long id, final Number rev) throws DBException {
        final Site site = getById(id);
        final AuditReader reader = AuditReaderFactory.get(entityManager.get());
        return reader.find(Site.class, site.getId(), rev);
    }


    @Override
    public Observable<Site> getObservableById(final Long id) throws DBException {
        return Observable.just(find(id));
    }

    @Override
    public Observable<Site> getObservableByIdAndRev(final Long id, final Number rev) throws DBException {
        return Observable.just(getByIdAndRev(id, rev));
    }

    public List<Site> search(final List<SearchFilter> searchFilters) throws SearchFilterException {
        final FilterBuilder filterBuilder = FilterBuilder.create(site);
        searchFilters.stream().forEach(searchFilter -> {
                    try {
                        filterBuilder.and(searchFilter);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
        return from().where(filterBuilder.getBooleanExpression()).listResults(site).getResults();
    }

    @Transactional
    public long removeAll() throws DBException {
        //TODO: QueryDSL doesn't support cascade. Could it be done better? Surely, could.
        final int before = count();
        remove(findAll());
        return count() - before;
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
    public Site serializeJSONToEntity(final JSONObject obj, final Class<Site> clazz) throws JSONException, SecurityException, IllegalArgumentException {
        final ObjectMapper mapper = new ObjectMapper();
        final JsonFactory jfactory = new JsonFactory();
        Site site = null;
        try {
            site = mapper.readValue(jfactory.createParser(obj.toString()), Site.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return site;
    }
}
