/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.integration.db.dao;

import apicore.dao.jpa.BaseJpaDao;
import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.expr.SimpleExpression;
import com.mysema.query.types.path.NumberPath;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.integration.IntegrationTestBase;
import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public abstract class DaoTestBase<T extends JpaSFREntityObject, Q extends EntityPath<T>> extends IntegrationTestBase {

    protected static final int MAX_BATCH_SIZE = 30;
    protected static final int LIMIT = 3;

    protected T createEntity() throws InstantiationException, IllegalAccessException {
        return getCoreEntityClass().newInstance();
    }

    public int getMaxBatchSize() {
        return MAX_BATCH_SIZE;
    }

    public int getOneFieldFilteredCount() {
        return getMaxBatchSize();
    }

    protected abstract BaseJpaDao<T, Q> getMainTestDao();

    protected abstract Class<T> getCoreEntityClass();

    protected abstract Long getObjectId(T t);

    protected abstract NumberPath<Long> getIdField();

    protected abstract <C extends Comparable> SimpleExpression<C> getOneFieldName();

    protected abstract Comparable<?> getOneFilterValue();

    protected abstract OrderSpecifier<?> getOneOrderCondition();

    @Test
    public abstract void eagerSaveTest() throws DBException;

    @Test
    public abstract void eagerRemoveTest() throws DBException;

    private static Method getSingleMethod(final Object o, final String methodName) { //NOPMD the violation was for unused private method. but this method is used by several protected method?!
        final Class<?> c = o.getClass();
        final Method[] methods = c.getMethods();
        for (final Method m : methods) {
            if (m.getName().startsWith(methodName)) {
                return m;
            }
        }
        return null;
    }

    protected List<T> createAndSaveBatches() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, DBException {
        final List<T> list = new ArrayList<>();
        for (int i = 0; i < getMaxBatchSize(); i++) {
            final T t = createEntity();
            list.add(getMainTestDao().save(t));
        }
        Assert.assertEquals(getMaxBatchSize(), list.size());
        return list;
    }

    @Test
    public void save() throws DBException, InstantiationException, IllegalAccessException,
            NoSuchMethodException, InvocationTargetException {
        // build up
        final T t = getMainTestDao().save(createEntity());
        // test assertion
        Assert.assertTrue(getMainTestDao().findAll().contains(t));
        // tear down
        getMainTestDao().remove(t);
    }

    @Test
    public void find() throws DBException, InstantiationException, IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(createEntity());
        // test assertion
        final T _t = getMainTestDao().find(getObjectId(t));
        Assert.assertEquals(t, _t);
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findOneBaseOnField() throws DBException, InstantiationException, IllegalAccessException {
        final T t = getMainTestDao().save(createEntity());
        final T _t = getMainTestDao().findOneBasedOnField(getIdField(), getObjectId(t));
        Assert.assertEquals(_t, t);
        getMainTestDao().remove(_t);
    }

    @Test
    public void findBasedOnField() throws DBException, InstantiationException,
            IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(createEntity());
        // test assertion
        final List<T> _t = getMainTestDao().findBasedOnField(getIdField(), getObjectId(t));
        Assert.assertTrue(_t.contains(t));
        // tear down
        getMainTestDao().remove(_t);
    }

    @Test
    public void findBasedOnFieldWithLimit() throws InstantiationException,
            IllegalAccessException, DBException, IllegalArgumentException,
            InvocationTargetException {
        final List<T> _list = getMainTestDao().findBasedOnFieldWithLimit(getOneFieldName(), getOneFilterValue(), LIMIT);
        Assert.assertEquals(LIMIT, _list.size());
    }

    @Test
    public void findBasedOnFieldWithOrder() throws InstantiationException,
            IllegalAccessException, DBException, IllegalArgumentException,
            InvocationTargetException {
        final List<T> _list = getMainTestDao().findBasedOnFieldWithOrder(getOneFieldName(), getOneFilterValue(), getOneOrderCondition());
        Assert.assertEquals(getOneFieldFilteredCount(), _list.size());
    }


    @Test
    public void findBasedOnFieldWithLimitAndOrder_exactMatchTrue() throws InstantiationException,
            IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            DBException {
        final List<T> _list = getMainTestDao().findBasedOnFieldWithLimitAndOrder(getOneFieldName(), getOneFilterValue(), getOneOrderCondition(), LIMIT);
        Assert.assertEquals(_list.size(), LIMIT);
    }

    @Test
    public void findAll() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, DBException {
        final List<T> _list = getMainTestDao().findAll();
        Assert.assertEquals(getMaxBatchSize(), _list.size());
    }

    @Test
    public void findOrderedList() throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException, DBException, InstantiationException {
        final List<T> _list = getMainTestDao().findOrdered(getOneOrderCondition());
        Assert.assertEquals(getMaxBatchSize(), _list.size());
    }

    @Test
    public void findOrderRange() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {

        // lets say 7 items a page. that should give us 5 pages with the last
        // page having 2 items.
        final int lastPage = getMaxBatchSize() / 7 + 1;
        final List<T> _list = getMainTestDao().findOrderedRange((lastPage - 1) * 7, lastPage * 7, getOneOrderCondition());

        Assert.assertEquals(getMaxBatchSize() % 7, _list.size());

        final List<T> __list = getMainTestDao().findOrderedRange(1, 7, getOneOrderCondition());
        Assert.assertEquals(7, __list.size());

        // -1 is an even worse page number, so it should not have fetched
        // anything
        final List<T> realyBadPage = getMainTestDao().findOrderedRange(-1, 7, getOneOrderCondition());
        Assert.assertEquals(0, realyBadPage.size());

        // 0 items on the page should give us 0 result also
        final List<T> zeroItems = getMainTestDao().findOrderedRange(1, 0, getOneOrderCondition());
        Assert.assertEquals(0, zeroItems.size());

        // -1 items on the page should give us 0 result too
        final List<T> badItems = getMainTestDao().findOrderedRange(1, -1, getOneOrderCondition());
        Assert.assertEquals(0, badItems.size());
    }

    @Test
    public void findRange() throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException, DBException {

        // 30 items, we can go from 0 to 29 items.
        final List<T> _list = getMainTestDao().findRange(10, 19);
        Assert.assertEquals(getMainTestDao().findAll().subList(10, 20), _list);
        Assert.assertEquals(10, _list.size());

        // just get 10 items
        final List<T> ten = getMainTestDao().findRange(0, 9);
        Assert.assertEquals(10, ten.size());

        // go out of range by -1, return 0
        final List<T> outOfRange = getMainTestDao().findRange(-1, 9);
        Assert.assertEquals(0, outOfRange.size());

        // out of range completely
        final List<T> _outOfRange = getMainTestDao().findRange(getMaxBatchSize() + 1, getMaxBatchSize() + 10);
        Assert.assertEquals(0, _outOfRange.size());

        // out of range by 10
        final List<T> __outOfRange = getMainTestDao().findRange(0, getMaxBatchSize() + 10);
        Assert.assertEquals(getMaxBatchSize(), __outOfRange.size());

        // from > end
        final List<T> bad = getMainTestDao().findRange(29, 0);
        Assert.assertEquals(0, bad.size());

        // just generally stupid
        final List<T> _bad = getMainTestDao().findRange(-29, 0);
        Assert.assertEquals(0, _bad.size());
    }

    @Test
    public void count() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        Assert.assertEquals(getMaxBatchSize(), getMainTestDao().count());
    }

    @Test
    public void remove() throws DBException, InstantiationException, IllegalAccessException {
        // build up
        final T t = getMainTestDao().save(createEntity());
        // test assertion
        final T _t = getMainTestDao().find(getObjectId(t));
        Assert.assertEquals(t, _t);

        getMainTestDao().remove(_t);
        final T __t = getMainTestDao().find(getObjectId(t));
        Assert.assertNull(__t);
    }

    @Test
    public void removeList() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        getMainTestDao().remove(getMainTestDao().findAll());
        Assert.assertEquals(0, getMainTestDao().count());
    }

    @Test
    public void removeBasedOnFieldValue() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        getMainTestDao().remove(getOneFieldName(), getOneFilterValue());
        final List<T> _list = getMainTestDao().findBasedOnField(getOneFieldName(), getOneFilterValue());
        Assert.assertEquals(0, _list.size());
    }

    @Test
    public void dropCollection() throws IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException, DBException {
        getMainTestDao().removeAll();
        Assert.assertEquals(0, getMainTestDao().count());
    }
}
