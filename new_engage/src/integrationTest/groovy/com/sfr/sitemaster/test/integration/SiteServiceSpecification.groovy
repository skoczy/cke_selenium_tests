package com.sfr.sitemaster.test.integration

import com.google.inject.Inject
import com.sfr.apicore.auth.AuthModule
import com.sfr.apicore.injection.InjectionService
import com.sfr.apicore.injection.SFRInjector
import com.sfr.apicore.injection.guice.GuiceSFRInjector
import com.sfr.sitemaster.app.SitemasterPropertyService
import com.sfr.sitemaster.dao.impl.SiteDAOImpl
import com.sfr.sitemaster.domainservices.ServiceService
import com.sfr.sitemaster.domainservices.SiteService
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.dto.SiteDTO
import com.sfr.sitemaster.entities.ChangeSetDTO
import com.sfr.sitemaster.entities.PersistentSession
import com.sfr.sitemaster.entities.Site
import com.sfr.sitemaster.enums.Days
import com.sfr.sitemaster.injection.guice.CoreServiceModule
import com.sfr.sitemaster.injection.guice.DAOModule
import com.sfr.sitemaster.injection.guice.DBModule
import com.sfr.sitemaster.injection.guice.DomainServiceModule
import com.sfr.sitemaster.integration.JdeWsClient
import com.sfr.sitemaster.integration.impl.JDEResponse
import data.LiquiTool
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Stepwise

import java.time.LocalDate

/**
 * Created by piotr on 20/10/15.
 */
@Stepwise
class SiteServiceSpecification extends Specification {

    @Shared
    PersistentSession persistentSession

    @Inject
    @Shared
    SiteDAOImpl siteDao

    @Inject
    @Shared
    JdeWsClient jdeWsClient

    @Shared
    Site siteEntity
    @Shared
    JDEResponse jdeResponse

    @Inject
    @Shared
    SiteService siteService

    @Inject
    @Shared
    ServiceService servicesService

    @Shared
    def siteId = 10116

    def setupSpec() {
        LiquiTool.reset();
        SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();
        final SFRInjector injector = new GuiceSFRInjector(new CoreServiceModule(sitemasterPropertyService), new AuthModule(), new DBModule(sitemasterPropertyService), new DAOModule(),
                new DomainServiceModule());
        InjectionService.registerServiceInjector(injector);
        injector.injectMembers(this)
        siteEntity = siteDao.getById siteId
        jdeResponse = jdeWsClient.getSiteById(siteId).toBlocking().first()
        persistentSession = new PersistentSession(userId: 'test@user.com')
    }

    def 'should merge two data sources'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        then:
        assert siteDTO.siteId == jdeResponse.siteID
        assert siteDTO.stationName == jdeResponse.siteName
        assert siteDTO.stationMan == jdeResponse.dealer_StationmanName
        assert siteDTO.category == jdeResponse.concept_Category
        assert siteDTO.stationType == jdeResponse.owner
        assert siteDTO.district == jdeResponse.district
        assert siteDTO.stationFormat == jdeResponse.siteType
        assert siteDTO.salesZone == jdeResponse.salesZone
        assert siteDTO.compName == jdeResponse.companyName
        assert siteDTO.orgNumber == jdeResponse.orgNumber
        assert siteDTO.clusterName == jdeResponse.clusterName
        assert siteDTO.dealerStationManName == jdeResponse.dealer_StationmanName
        assert siteDTO.areaSalesManagerName == jdeResponse.areaSalesManagerName
        assert siteDTO.status == jdeResponse.status
        assert siteDTO.address.street == jdeResponse.siteMasterAddressDetail.address
        assert siteDTO.address.postalCode == jdeResponse.siteMasterAddressDetail.zipCode
        assert siteDTO.address.city == jdeResponse.siteMasterAddressDetail.city_place
        assert siteDTO.address.county == jdeResponse.siteMasterAddressDetail.countyName

        //from entity
        assert siteDTO.openingInfo.alwaysOpen == siteEntity.openingInfo.alwaysOpen
        assert siteDTO.openingInfo.openingTimes.findAll { k, v ->
            v.open == siteEntity.openingInfo.openingTimes[k].open && v.close == siteEntity.openingInfo.openingTimes[k].close
        }.size() == siteEntity.openingInfo.openingTimes.size()
        assert siteDTO.openingInfo?.temporarilyClosed[0]?.from == siteEntity.openingInfo?.temporarilyClosed[0]?.from as String
        assert siteDTO.openingInfo?.temporarilyClosed[0]?.to == siteEntity.openingInfo?.temporarilyClosed[0]?.to as String

        assert siteDTO.xCoord == siteEntity.xCoord
        assert siteDTO.yCoord == siteEntity.yCoord

    }

    def 'should update Site\'s coordinates data'() {
        when:

        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.xCoord = '5.123'
        siteDTO.yCoord = '1.123'
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.xCoord == '5.123'
        site.yCoord == '1.123'
        revisions.size() == 2
    }


    def 'should update Site\'s openingInfo data'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.openingInfo.alwaysOpen = false
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.alwaysOpen == false
        revisions.size() == 3
    }

    def 'should get 2 changeSets for a Site'() {
        when:
        List<ChangeSetDTO> changeSets = siteService.getChangeSets(siteId)

        then:
        changeSets.size() == 2
        changeSets.get(0).changes.size() == 2
        changeSets.get(1).changes.size() == 1
    }

    def 'should update Site\'s temporarily closed data'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.openingInfo.temporarilyClosed << new OpeningInfoDTO.TemporarilyClosedDTO(from: LocalDate.parse('2015-09-10'), to: LocalDate.parse('2025-09-10'))
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.temporarilyClosed.size() == 1
        site.openingInfo.temporarilyClosed.get(0).from == LocalDate.parse('2015-09-10')
        site.openingInfo.temporarilyClosed.get(0).to == LocalDate.parse('2025-09-10')
        revisions.size() == 4
    }

    def 'should update Site\'s openingInfo data2'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.openingInfo.alwaysOpen = false
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.alwaysOpen == false
        revisions.size() == 5
    }


    def 'should add Site\'s opening hours'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.openingInfo.openingTimes.put(Days.WEEKDAYS, new OpeningInfoDTO.OpeningTimeDTO(open: '08:00', close: '20:00'))
        siteDTO.openingInfo.openingTimes.put(Days.SATURDAY, new OpeningInfoDTO.OpeningTimeDTO(open: '08:00', close: '20:00'))
        siteDTO.openingInfo.openingTimes.put(Days.SUNDAY, new OpeningInfoDTO.OpeningTimeDTO(open: '10:00', close: '16:00'))
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.openingTimes[Days.WEEKDAYS].open == '08:00'
        site.openingInfo.openingTimes[Days.WEEKDAYS].close == '20:00'
        site.openingInfo.openingTimes[Days.SATURDAY].open == '08:00'
        site.openingInfo.openingTimes[Days.SATURDAY].close == '20:00'
        site.openingInfo.openingTimes[Days.SUNDAY].open == '10:00'
        site.openingInfo.openingTimes[Days.SUNDAY].close == '16:00'
        revisions.size() == 6
    }

    def 'should update Site\'s opening hours for Saturday and set closed for Sunday'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.openingInfo.openingTimes.put(Days.SATURDAY, new OpeningInfoDTO.OpeningTimeDTO(open: '10:00', close: '16:00'))
        siteDTO.openingInfo.openingTimes.remove(Days.SUNDAY)
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.openingTimes[Days.WEEKDAYS].open == '08:00'
        site.openingInfo.openingTimes[Days.WEEKDAYS].close == '20:00'
        site.openingInfo.openingTimes[Days.SATURDAY].open == '10:00'
        site.openingInfo.openingTimes[Days.SATURDAY].close == '16:00'
        site.openingInfo.openingTimes[Days.SUNDAY] == null
        revisions.size() == 7
    }

    def 'should update Site\'s services'() {
        when:
        SiteDTO siteDTO = siteService.get(siteId)
        siteDTO.services = servicesService.findAll().findAll { it.type == 'Car rental' || it.type == 'ATM' }
        siteService.save(persistentSession, siteDTO)
        SiteDTO site = siteService.get(siteId)
        def revisions = siteDao.getRevisions(siteId)
        then:
        site.openingInfo.openingTimes[Days.WEEKDAYS].open == '08:00'
        site.openingInfo.openingTimes[Days.WEEKDAYS].close == '20:00'
        site.openingInfo.openingTimes[Days.SATURDAY].open == '10:00'
        site.openingInfo.openingTimes[Days.SATURDAY].close == '16:00'
        site.openingInfo.openingTimes[Days.SUNDAY] == null
        revisions.size() == 8
    }

    def 'should get changeSets for a Site'() {
        when:
        List<ChangeSetDTO> changeSets = siteService.getChangeSets(siteId)
        then:
        changeSets.size() == 7
        changeSets.get(0).changes.size() == 2
        changeSets.get(1).changes.size() == 1
        changeSets.get(2).changes.size() == 2
        changeSets.get(3).changes.size() == 0
        changeSets.get(4).changes.size() == 6
        changeSets.get(5).changes.size() == 4
    }

    def 'should get correct modifiedBy(user) for each changeSet'() {
        when:
        List<ChangeSetDTO> changeSets = siteService.getChangeSets(siteId)
        then:
        changeSets.each {
            it.modifiedBy == 'test@user.com'
        }
    }
}