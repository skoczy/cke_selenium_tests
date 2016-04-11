/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao.impl;

import apicore.dao.jpa.BaseJpaDao;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.PersistentSessionDao;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.QPersistentSession;

import static com.sfr.sitemaster.entities.QPersistentSession.persistentSession;


/**
 * PersistentSession domainservices impl using mongo
 *
 * @author tuxbear
 */
public class PersistentSessionDAOImpl extends BaseJpaDao<PersistentSession, QPersistentSession> implements PersistentSessionDao {


    public PersistentSessionDAOImpl() {
        super(persistentSession);
    }

    @Override
    public PersistentSession findFromSessionId(final String sessionKey) throws DBException {
        return from().where(persistentSession.sessionId.eq(sessionKey)).singleResult(persistentSession);
    }

    @Override
    public NumberPath<Long> getIdField() {
        return persistentSession.id;
    }



}
