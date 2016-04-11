/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.unit;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.lang.reflect.Field;


/**
 * 
 * Base object merging tests.
 * 
 * @author yves
 * 
 */
public abstract class EntityUnitTestBase<T extends JpaSFREntityObject> {

    private static int unique_counter;

    protected abstract Class<T> getCoreEntityClass();

    @BeforeClass
    public static void resetCounter() {
        unique_counter = 0;
    }

    @Test
    public void printModelSchema() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, JSONException {
        final T t1 = getCoreEntityClass().newInstance();
        System.out.println(t1.getModelSchema().toString());
    }

    @Test
    public void merge_successful() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, SecurityException, NoSuchFieldException {
        final T t1 = getCoreEntityClass().newInstance();
        final T t2 = getCoreEntityClass().newInstance();
        fillStrings(t1, false);
        fillStrings(t2, false);
        t1.merge(t2);
        assertMerge(t1, t2, false);
    }

    @Test
    public void merge_null_fields() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, SecurityException, NoSuchFieldException {
        final T t1 = getCoreEntityClass().newInstance();
        final T t2 = getCoreEntityClass().newInstance();
        fillStrings(t1, false);
        fillStrings(t2, true);
        t1.merge(t2);
        assertMerge(t1, t2, true);
    }

    private void fillStrings(final T t, final boolean setNull) {
        final Field[] origin_fields = getCoreEntityClass().getDeclaredFields();
        for (final Field f : origin_fields) {
            f.setAccessible(true);
            if (!t.skipModifer(f) && !t.skipAnnotatedFields(f)) {
                try {
                    if (setNull) {
                        f.set(t, null);
                    } else {
                        // force set the value.
                        f.set(t, String.valueOf(unique_counter));
                    }
                } catch (IllegalArgumentException e) { // NOPMD
                } catch (IllegalAccessException e) { // NOPMD
                }
            }
            unique_counter++;
        }
    }

    private void assertMerge(final T t1, final T t2, final boolean t2containsnull) throws IllegalArgumentException,
            IllegalAccessException {
        final Field[] origin_fields = getCoreEntityClass().getDeclaredFields();
        for (final Field f : origin_fields) {
            f.setAccessible(true);
            final Object originalValue = f.get(t1);
            final Object newValue = f.get(t2);
            if (t1.skipAnnotatedFields(f) || t1.skipClasses(originalValue) || t1.skipModifer(f)) {
                continue;
            }
            if (t1.noMerge(f)) {
                Assert.assertNotSame(originalValue, newValue);
            } else {
                if (t1.nullAllowed(f) && t2containsnull) {
                    Assert.assertEquals(originalValue, newValue);
                } else if (t1.nullAllowed(f) && !t2containsnull) {
                    Assert.assertEquals(originalValue, newValue);
                } else if (!t1.nullAllowed(f) && t2containsnull) {
                    // enums will be null, and thus the same
                    if (f.getType() != String.class) { // NOPMD
                        // if its not a string, it might not be set probably.
                        // and thus the original value could pertain its default
                        // value or could also be null
                        // System.out.println( f.getName() + " not a string.");
                    } else {
                        // System.out.println(f.getType().getName()+", "+f.getDeclaringClass().getName()+", "+f.getName()+" original="+originalValue+" new="+newValue);
                        Assert.assertNotSame(originalValue, newValue);
                    }
                } else if (!t1.nullAllowed(f) && !t2containsnull) {
                    Assert.assertEquals(originalValue, newValue);
                }
            }
        }
    }
}
