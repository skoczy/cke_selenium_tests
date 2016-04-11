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
import com.sfr.sitemaster.dao.SitePersonsDao;
import com.sfr.sitemaster.entities.QSitePerson;
import com.sfr.sitemaster.entities.SitePerson;
import rx.Observable;

import java.util.List;

import static com.sfr.sitemaster.entities.QSitePerson.sitePerson;

/**
 * Site DAO
 *
 * @author piotr
 */
public class SitePersonsDAOImpl extends BaseJpaDao<SitePerson, QSitePerson> implements SitePersonsDao {

    @Inject
    public SitePersonsDAOImpl() {
        super(sitePerson);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return sitePerson.id;
    }

    public List<SitePerson> getSitePersonsForSite(final Long siteId) {
        return from().where(sitePerson.site.id.eq(siteId)).listResults(sitePerson).getResults();
    }

    public Observable<SitePerson> getObservableSitePersonsForSite(final Long siteId) {
        return Observable.from(from().where(sitePerson.site.id.eq(siteId)).listResults(sitePerson).getResults());
    }

    public SitePerson getSitePerson(final Long siteId, final Long id) {
        return from().where(sitePerson.site.id.eq(siteId).and(sitePerson.id.eq(id))).singleResult(sitePerson);
    }

}
