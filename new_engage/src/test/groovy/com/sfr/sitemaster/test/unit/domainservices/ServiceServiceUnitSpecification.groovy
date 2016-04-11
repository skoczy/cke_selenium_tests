package com.sfr.sitemaster.test.unit.domainservices

import com.sfr.sitemaster.dao.ServiceDao
import com.sfr.sitemaster.dao.SiteDao
import com.sfr.sitemaster.domainservices.ServiceService
import com.sfr.sitemaster.domainservices.impl.ServiceServiceImpl
import com.sfr.sitemaster.dto.ServiceDTO
import com.sfr.sitemaster.entities.Service
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by piotr on 02/11/15.
 */
class ServiceServiceUnitSpecification extends Specification {
    @Shared
    ServiceService serviceService
    @Shared
    SiteDao siteDao
    @Shared
    ServiceDao serviceDao

    @Shared
    List<Service> serviceList

    def setup() {
        siteDao = Mock(SiteDao)
        serviceDao = Mock(ServiceDao)
        serviceService = new ServiceServiceImpl(siteDao, serviceDao)
        serviceList = [
                new Service(id: 0, type: 'type1', icon: 'icon1', lang: 'en', displayName: 'service1'),
                new Service(id: 1, type: 'type2', icon: 'icon2', lang: 'en', displayName: 'service2'),
                new Service(id: 2, type: 'type3', icon: 'icon3', lang: 'en', displayName: 'service3'),
                new Service(id: 3, type: 'type4', icon: 'icon4', lang: 'en', displayName: 'service4'),
                new Service(id: 4, type: 'type5', icon: 'icon5', lang: 'en', displayName: 'service5'),
                new Service(id: 5, type: 'type6', icon: 'icon6', lang: 'en', displayName: 'service6'),
                new Service(id: 6, type: 'type7', icon: 'icon7', lang: 'en', displayName: 'service7')
        ]
    }

    def 'should get one service'() {
        setup:
        serviceDao.find(2) >> serviceList[2]
        when:
        def service = serviceService.get(2)
        then:
        service instanceof ServiceDTO
        service.id == serviceList[2].id
        service.type == serviceList[2].type
        service.icon == serviceList[2].icon
        service.lang == serviceList[2].lang
        service.name == serviceList[2].displayName
    }

    def 'should get services for one site'() {
        setup:
        serviceDao.getServicesForSite(123) >> serviceList[0..2]
        when:
        def services = serviceService.getServicesForSite(123)
        then:
        services.each { it instanceof ServiceDTO }
        [0..2].each {
            services[it].id == serviceList[it].id
            services[it].type == serviceList[it].type
            services[it].icon == serviceList[it].icon
            services[it].lang == serviceList[it].lang
            services[it].name == serviceList[it].displayName
        }
    }

    def 'should get all services'() {
        setup:
        serviceDao.findAll() >> serviceList
        when:
        def services = serviceService.findAll()
        then:
        services.size() == serviceList.size()
        services.each { it instanceof ServiceDTO }
        [0..6].each {
            services[it].id == serviceList[it].id
            services[it].type == serviceList[it].type
            services[it].icon == serviceList[it].icon
            services[it].lang == serviceList[it].lang
            services[it].name == serviceList[it].displayName
        }
    }
}