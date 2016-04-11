/**
 * Copyright (c) 2014 ~ 2015 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.unit.domainservices;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.inject.Inject;
import com.sfr.sitemaster.domainservices.ShmlogFacadeInterface;
import com.sfr.sitemaster.domainservices.impl.Shmlog;
import com.sfr.sitemaster.unit.UnitTestBase;

/**
 * Shmlog tests
 * 
 * @author yves
 * 
 */
public class ShmlogTests extends UnitTestBase {

    private final Object lock = new Object();
    private volatile transient int currentcount;
    private static final int num_runners = 20;
    private final Set<Integer> counters = Collections.synchronizedSet(new TreeSet<Integer>());

    private final Shmlog shmlog = Shmlog.getInstance();
    private volatile boolean failed;

    @Inject
    private ShmlogFacadeInterface shmlogFacade;

    private class Runner implements Runnable {
        private final boolean useFacade;
        private final int iteration;

        public Runner(final boolean useFacade, final int iteration) {
            this.useFacade = useFacade;
            this.iteration = iteration;
        }

        @Override
        public void run() {
            for (int i = 0; i < iteration; i++) {
                final String log = "/" + System.currentTimeMillis();
                if (useFacade) {
                    shmlogFacade.write(log);
                } else {
                    shmlog.write(log);
                }

                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            currentcount++;
        }
    };

    private class Notifier implements Runnable {
        private final ShmlogTests syncpoint;

        public Notifier(final ShmlogTests sync) {
            this.syncpoint = sync;
        }

        @Override
        public void run() {
            while (true) {
                if (currentcount == num_runners) {
                    syncpoint.notifyTest();
                    break;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public void notifyTest() {
        synchronized (lock) {
            lock.notifyAll();
        }
    }

    public void waitTest(final long timeout) {
        synchronized (lock) {
            try {
                lock.wait(timeout);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void concurrentWrites(final boolean useFacade, final int iteration) {
        final Thread notifier = new Thread(new Notifier(this));
        notifier.start();

        for (int i = 0; i < num_runners; i++) {
            Thread runner = new Thread(new Runner(useFacade, iteration)); // NOPMD
                                                                          // we
                                                                          // need
                                                                          // to
                                                                          // create
                                                                          // these
                                                                          // thread
                                                                          // on
                                                                          // the
                                                                          // fly.
                                                                          // not
                                                                          // really
                                                                          // about
                                                                          // performance.
            runner.start();
        }
        waitTest(10000);
        if (failed) {
            Assert.fail("duplicate index found");
        }
    }

    @Before
    public void reset() {
        shmlog.resetIndex();
        currentcount = 0;
        counters.clear();
        failed = false;
    }

    @Test
    public void writeTest_noinjection() {
        concurrentWrites(false, 3);
    }

    @Test
    public void readTest_noinjection() {
        concurrentWrites(false, 3);
        final String[] readthis = shmlog.read();
        Assert.assertNotNull(readthis);
    }

    @Test
    public void writeTest_injection() {
        concurrentWrites(true, 3);
    }

    @Test
    public void readTest_injection() {
        concurrentWrites(true, 3);
        final String[] readthis = shmlogFacade.read();
        Assert.assertNotNull(readthis);
    }

    @Test
    public void overflowWrites_noinjection() {
        concurrentWrites(false, 300);
        final String[] readthis = shmlog.read();
        Assert.assertNotNull(readthis);
        Assert.assertEquals(shmlog.getBufferSize(), readthis.length);
    }

    @Test
    public void overflowWrites_injection() {
        concurrentWrites(true, 300);
        final String[] readthis = shmlogFacade.read();
        Assert.assertNotNull(readthis);
        Assert.assertEquals(shmlogFacade.getBufferSize(), readthis.length);
    }
}
