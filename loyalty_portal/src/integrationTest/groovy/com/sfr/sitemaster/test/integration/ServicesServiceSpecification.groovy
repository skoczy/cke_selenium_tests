package com.sfr.sitemaster.test.integration

import com.google.inject.Inject
import com.sfr.apicore.auth.AuthModule
import com.sfr.apicore.injection.InjectionService
import com.sfr.apicore.injection.SFRInjector
import com.sfr.apicore.injection.guice.GuiceSFRInjector
import com.sfr.sitemaster.app.SitemasterPropertyService
import com.sfr.sitemaster.domainservices.ServiceService
import com.sfr.sitemaster.dto.ServiceDTO
import com.sfr.sitemaster.entities.PersistentSession
import com.sfr.sitemaster.injection.guice.CoreServiceModule
import com.sfr.sitemaster.injection.guice.DAOModule
import com.sfr.sitemaster.injection.guice.DBModule
import com.sfr.sitemaster.injection.guice.DomainServiceModule
import data.LiquiTool
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

/**
 * Created by piotr on 20/10/15.
 */
@Stepwise
class ServicesServiceSpecification extends Specification {

    @Inject
    @Shared
    ServiceService servicesService

    @Shared
    PersistentSession persistentSession

    @Shared
    def siteId = 10116

    @Shared
    List<ServiceDTO> serviceDTOs

    def setupSpec() { //Done once before all tests in this class.
        LiquiTool.reset(); //Drop all objects in db and recreate schema
        SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();
        final SFRInjector injector = new GuiceSFRInjector(new CoreServiceModule(sitemasterPropertyService), new AuthModule(), new DBModule(sitemasterPropertyService), new DAOModule(),
                new DomainServiceModule());
        InjectionService.registerServiceInjector(injector);
        injector.injectMembers(this)
        persistentSession = new PersistentSession(userId: 'test@user.com')
    }

    def 'should get all services'() {
        when:
        serviceDTOs = servicesService.findAll()
        then:
        serviceDTOs.size() == 16
        ['Car rental', 'Trailer rental', 'Simply Great Coffee', 'Routex Atlas', 'TruckDiesel Network', 'Shower'].each { type ->
            assert serviceDTOs.find { it.type == type } != null
        }
    }

    def 'should update Site\'s service list'() {
        setup:
        def servicesForSite = []
        servicesForSite << serviceDTOs.find { it.type == 'ATM' }
        servicesForSite << serviceDTOs.find { it.type == 'Simply Great Coffee' }
        servicesForSite << serviceDTOs.find { it.type == 'Wifi' }
        when:
        servicesService.save(persistentSession, siteId, servicesForSite)
        then:
        notThrown(Exception)
    }

    def 'should get services for one Site'() {
        when:
        def services = servicesService.getServicesForSite(siteId)
        then:
        services.size() == 3
        services.find { it.type == 'ATM' } != null
        services.find { it.type == 'Simply Great Coffee' } != null
        services.find { it.type == 'Wifi' } != null
    }
}
