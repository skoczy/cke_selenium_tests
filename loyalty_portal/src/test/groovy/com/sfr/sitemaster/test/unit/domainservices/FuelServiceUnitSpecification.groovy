package com.sfr.sitemaster.test.unit.domainservices

import com.sfr.sitemaster.dao.FuelDao
import com.sfr.sitemaster.dao.SiteDao
import com.sfr.sitemaster.domainservices.FuelService
import com.sfr.sitemaster.domainservices.impl.FuelServiceImpl
import com.sfr.sitemaster.dto.ServiceDTO
import com.sfr.sitemaster.entities.Fuel
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by piotr on 02/11/15.
 */
class FuelServiceUnitSpecification extends Specification {
    @Shared
    FuelService fuelService
    @Shared
    SiteDao siteDao
    @Shared
    FuelDao fuelDao

    @Shared
    List<Fuel> fuelList

    def setup() {
        siteDao = Mock(SiteDao)
        fuelDao = Mock(FuelDao)
        fuelService = new FuelServiceImpl(siteDao, fuelDao)
        fuelList = [
                new Fuel(id: 0, type: 'type1', icon: 'icon1', lang: 'en', displayName: 'fuel1'),
                new Fuel(id: 1, type: 'type2', icon: 'icon2', lang: 'en', displayName: 'fuel2'),
                new Fuel(id: 2, type: 'type3', icon: 'icon3', lang: 'en', displayName: 'fuel3'),
                new Fuel(id: 3, type: 'type4', icon: 'icon4', lang: 'en', displayName: 'fuel4'),
                new Fuel(id: 4, type: 'type5', icon: 'icon5', lang: 'en', displayName: 'fuel5'),
                new Fuel(id: 5, type: 'type6', icon: 'icon6', lang: 'en', displayName: 'fuel6'),
                new Fuel(id: 6, type: 'type7', icon: 'icon7', lang: 'en', displayName: 'fuel7')
        ]
    }

    def 'should get one fuel'() {
        setup:
        fuelDao.find(2) >> fuelList[2]
        when:
        def fuel = fuelService.get(2)
        then:
        fuel instanceof ServiceDTO
        fuel.id == fuelList[2].id
        fuel.type == fuelList[2].type
        fuel.icon == fuelList[2].icon
        fuel.lang == fuelList[2].lang
        fuel.name == fuelList[2].displayName
    }

    def 'should get fuels for one site'() {
        setup:
        fuelDao.getFuelsForSite(123) >> fuelList[0..2]
        when:
        def fuels = fuelService.getFuelsForSite(123)
        then:
        fuels.each { it instanceof ServiceDTO }
        [0..2].each {
            fuels[it].id == fuelList[it].id
            fuels[it].type == fuelList[it].type
            fuels[it].icon == fuelList[it].icon
            fuels[it].lang == fuelList[it].lang
            fuels[it].name == fuelList[it].displayName
        }
    }

    def 'should get all fuels'() {
        setup:
        fuelDao.findAll() >> fuelList
        when:
        def fuels = fuelService.findAll()
        then:
        fuels.size() == fuelList.size()
        fuels.each { it instanceof ServiceDTO }
        [0..6].each {
            fuels[it].id == fuelList[it].id
            fuels[it].type == fuelList[it].type
            fuels[it].icon == fuelList[it].icon
            fuels[it].lang == fuelList[it].lang
            fuels[it].name == fuelList[it].displayName
        }
    }


}
