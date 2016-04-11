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
import com.google.inject.persist.Transactional;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.sitemaster.dao.impl.SiteDAOImpl;
import com.sfr.sitemaster.entities.OpeningTime;
import com.sfr.sitemaster.entities.PersistentSession;
import com.sfr.sitemaster.entities.QSite;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.enums.Days;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import com.sfr.sitemaster.shared.SiteHelper;
import data.LiquiTool;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import java.util.List;

import static com.sfr.sitemaster.entities.QSite.site;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

/**
 * SiteDao integration tests.
 *
 * @author piotr
 */
public class SiteDaoTest extends DaoTestBase<Site, QSite> {

    @Inject
    private static SiteDAOImpl siteDao;
    @Inject
    private static EntityManager entityManager;

    @BeforeClass
    public static void setup() {
        IntegrationTestBase.setup();
        siteDao = InjectionService.getInjector().getInstance(SiteDAOImpl.class);
        entityManager = InjectionService.getInjector().getInstance(EntityManager.class);
        LiquiTool.reset();
    }

    @Before
    public void before() {
        final PersistentSession persistentSession = new PersistentSession();
        persistentSession.setUserId("test@user.com");
        entityManager.getTransaction().begin();
    }

    @After
    public void after() {
        entityManager.getTransaction().rollback();
    }

    @Override
    protected Site createEntity() throws InstantiationException, IllegalAccessException {
        return SiteHelper.createEntity();
    }

    @Override
    protected BaseJpaDao<Site, QSite> getMainTestDao() {
        return siteDao;
    }

    @Override
    protected Class<Site> getCoreEntityClass() {
        return Site.class;
    }

    @Override
    protected Long getObjectId(final Site site) {
        return site.getId();
    }

    @Override
    protected NumberPath<Long> getIdField() {
        return site.id;
    }

    @Override
    protected SimpleExpression<? extends Comparable<?>> getOneFieldName() {
        return site.openingInfo.alwaysOpen;
    }

    @Override
    protected Comparable<?> getOneFilterValue() {
        return false;
    }

    public int getMaxBatchSize() {
        return 2356;
    }

    @Override
    public int getOneFieldFilteredCount() {
        return 922;
    }

    @Override
    protected OrderSpecifier<?> getOneOrderCondition() {
        return site.id.asc();
    }

    @Override
    public void eagerSaveTest() throws DBException {
        //empty
    }

    @Override
    public void eagerRemoveTest() throws DBException {
        //empty
    }

    @Test
    @Transactional
    public void addNewSiteTest() throws DBException {
        final Site site = SiteHelper.createEntity(100000);
        siteDao.save(site);
        final Site saved = siteDao.find(site.getId());
        assertEquals(getMaxBatchSize() + 1, siteDao.count());
        assertEquals(site.getxCoord(), saved.getxCoord());
        assertNotNull(saved.getOpeningInfo());
        assertFalse(saved.getOpeningInfo().getAlwaysOpen());
        assertNotNull(saved.getOpeningInfo().getOpeningTimes());
        assertEquals(site.getOpeningInfo().getOpeningTimes().size(), saved.getOpeningInfo().getOpeningTimes().size());
    }

//    @Test
//    public void searchSitesIncludesTest() throws SearchFilterException {
//        final SearchFilter searchFilterDTO = new SearchFilter();
//        searchFilterDTO.setField("marketingName");
//        searchFilterDTO.setOperator(SearchFilter.OPERATOR.LIKE);
//        searchFilterDTO.setValue(Arrays.asList("INGO FAABORG"));
//        final List<Site> sites = siteDao.search(Arrays.asList(searchFilterDTO));
//        assert sites.size() == 1;
//    }
//
//    @Test
//    public void searchSitesEqualsTest() throws SearchFilterException {
//        final SearchFilter searchFilterDTO = new SearchFilter();
//        searchFilterDTO.setField("status");
//        searchFilterDTO.setOperator(SearchFilter.OPERATOR.LIKE);
//        searchFilterDTO.setValue(Arrays.asList("Active"));
//        final List<Site> sites = siteDao.search(Arrays.asList(searchFilterDTO));
//        assert sites.size() == 30;
//    }

    @Test
    public void enversOpeningTimesTest() throws DBException {
        final Long id = 10216l;
        final Site site = siteDao.getById(id);
        site.getOpeningInfo().getOpeningTimes().put(Days.SUNDAY, new OpeningTime(site, Days.SUNDAY, "08:00", "12:00"));
        siteDao.save(site);
        entityManager.getTransaction().commit();
        entityManager.getTransaction().begin();
        final List revisions = siteDao.getRevisions(id);
        final Object[] after = (Object[]) revisions.get(1);
        final Site siteAfter = (Site) after[0];
        assertEquals(2, revisions.size());
        assertEquals("08:00", siteAfter.getOpeningInfo().getOpeningTimes().get(Days.SUNDAY).getOpen());
        assertEquals("12:00", siteAfter.getOpeningInfo().getOpeningTimes().get(Days.SUNDAY).getClose());
    }
}
