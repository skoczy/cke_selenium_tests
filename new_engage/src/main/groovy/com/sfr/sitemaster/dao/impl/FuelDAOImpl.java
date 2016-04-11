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
import com.sfr.sitemaster.dao.FuelDao;
import com.sfr.sitemaster.entities.Fuel;
import com.sfr.sitemaster.entities.QFuel;
import rx.Observable;

import java.util.List;

import static com.sfr.sitemaster.entities.QFuel.fuel;
import static com.sfr.sitemaster.entities.QSite.site;

/**
 * Fuel DAO
 *
 * @author piotr
 */
public class FuelDAOImpl extends BaseJpaDao<Fuel, QFuel> implements FuelDao {

    @Inject
    public FuelDAOImpl() {
        super(fuel);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return fuel.id;
    }

    public List<Fuel> find(final Long... id) throws DBException {
        return this.from().where(this.getIdField().in(id)).fetchAll().listResults(fuel).getResults();
    }

    public List<Fuel> getFuelsForSite(final Long siteId) {
        return query().from(site).innerJoin(site.fuels, fuel).where(site.id.eq(siteId)).listResults(fuel).getResults();
    }

    public Observable<Fuel> getObservableFuelsForSite(final Long siteId) {
        return Observable.from(getFuelsForSite(siteId));
    }

}
