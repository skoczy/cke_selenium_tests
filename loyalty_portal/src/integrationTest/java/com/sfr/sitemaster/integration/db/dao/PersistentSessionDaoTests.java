/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.db.dao;

import apicore.dao.jpa.BaseJpaDao;
import com.google.inject.Inject;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.dao.impl.PersistentSessionDAOImpl;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.QPersistentSession;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

import static com.sfr.sitemaster.entities.QPersistentSession.persistentSession;
import static org.junit.Assert.assertEquals;

/**
 * PersistentSession integration test. writes to db. so make sure tear down
 * occurs.
 *
 * @author
 */
public class PersistentSessionDaoTests extends DaoTestBase<PersistentSession, QPersistentSession> {

    @Inject
    private PersistentSessionDAOImpl persistentSessionDao;

    @BeforeClass
    public static void setup() {
        IntegrationTestBase.setup();
    }

    @Override
    protected BaseJpaDao<PersistentSession, QPersistentSession> getMainTestDao() {
        return persistentSessionDao;
    }

    protected PersistentSession createEntity() {
        final PersistentSession pojo = new PersistentSession();
        pojo.setSessionId("682749r9-asfw32-23f-aef-23f");
        pojo.setUserId("68272323");
        return pojo;
    }

    @Before
    public void startTransaction() throws DBException, IllegalAccessException, InvocationTargetException, InstantiationException {
        persistentSessionDao.removeAll();
        createAndSaveBatches();
        assertEquals(30, persistentSessionDao.count());
    }

    @Test
    public void basicSaveAndRetreivePersistentSession() throws DBException { // NOPMD
        // pmd complains that one assert one test case.
        // lets just ignore that one in this case.
        persistentSessionDao.removeAll();
        assertEquals("count not equal", 0, persistentSessionDao.count());
        final PersistentSession entity = createEntity();
        persistentSessionDao.save(entity);
        assertEquals("count not equal after adding one", 1, persistentSessionDao.count());
        final PersistentSession retreived = persistentSessionDao.findAll().get(0);
        assertEquals("entity not equal", entity, retreived);
    }

    @Override
    protected Class<PersistentSession> getCoreEntityClass() {
        return PersistentSession.class;
    }

    @Override
    protected Long getObjectId(final PersistentSession persistentSession) {
        return persistentSession.getId();
    }

    @Override
    protected NumberPath<Long> getIdField() {
        return persistentSession.id;
    }

    @Override
    protected SimpleExpression<String> getOneFieldName() {
        return persistentSession.sessionId;
    }

    @Override
    protected Comparable<?> getOneFilterValue() {
        return "682749r9-asfw32-23f-aef-23f";
    }

    @Override
    protected OrderSpecifier<?> getOneOrderCondition() {
        return persistentSession.sessionId.asc();
    }

    @Override
    public void eagerSaveTest() throws DBException {
//empty
    }

    @Override
    public void eagerRemoveTest() throws DBException {
//empty
    }


}
