/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.impl;

import apicore.dao.jpa.BaseJpaDao;
import com.google.inject.Inject;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.ServiceDao;
import com.sfr.sitemaster.entities.QService;
import com.sfr.sitemaster.entities.Service;
import rx.Observable;

import java.util.List;

import static com.sfr.sitemaster.entities.QService.service;
import static com.sfr.sitemaster.entities.QSite.site;

/**
 * Site DAO
 *
 * @author piotr
 */
public class ServiceDAOImpl extends BaseJpaDao<Service, QService> implements ServiceDao {

    @Inject
    public ServiceDAOImpl() {
        super(service);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return service.id;
    }

    public List<Service> find(final Long... id) throws DBException {
        return this.from().where(this.getIdField().in(id)).fetchAll().listResults(service).getResults();
    }

    public List<Service> getServicesForSite(final Long siteId) {
        return query().from(site).innerJoin(site.services, service).where(site.id.eq(siteId)).listResults(service).getResults();
    }

    public Observable<Service> getObservableServicesForSite(final Long siteId) {
        return Observable.from(getServicesForSite(siteId));
    }

}
