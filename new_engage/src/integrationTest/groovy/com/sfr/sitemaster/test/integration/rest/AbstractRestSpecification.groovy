package com.sfr.sitemaster.test.integration.rest

import com.sfr.apicore.auth.AuthModule
import com.sfr.apicore.injection.InjectionService
import com.sfr.apicore.injection.SFRInjector
import com.sfr.apicore.injection.guice.GuiceSFRInjector
import com.sfr.sitemaster.app.SitemasterPropertyService
import com.sfr.sitemaster.app.SitemasterServer
import com.sfr.sitemaster.injection.guice.CoreServiceModule
import com.sfr.sitemaster.injection.guice.DAOModule
import com.sfr.sitemaster.injection.guice.DBModule
import com.sfr.sitemaster.injection.guice.DomainServiceModule
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import spock.lang.Specification

/**
 * Created by piotr on 18/09/15.
 */
abstract class AbstractRestSpecification extends Specification {
    def static port = '8080';
    def static username = 'mobile@app.com'
    def static pass = 'CanUHardcodeit12'

    protected final Log log = LogFactory.getLog(getClass());

    def setupSpec() {
        SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();
        final SFRInjector injector = new GuiceSFRInjector(new CoreServiceModule(sitemasterPropertyService), new AuthModule(), new DBModule(sitemasterPropertyService), new DAOModule(),
                new DomainServiceModule());
        InjectionService.registerServiceInjector(injector);
        injector.injectMembers(this)
        SitemasterServer.start(injector, null);
    }

    def cleanupSpec() {
        SitemasterServer.stop();
    }
}
