package com.sfr.sitemaster.test.unit

import com.sfr.sitemaster.commons.SiteMergeUtil
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.dto.SiteDTO
import com.sfr.sitemaster.entities.Service
import com.sfr.sitemaster.entities.Site
import com.sfr.sitemaster.entities.TemporarilyClosed
import com.sfr.sitemaster.enums.Days
import com.sfr.sitemaster.enums.Owner
import com.sfr.sitemaster.integration.impl.JDEResponse
import com.sfr.sitemaster.integration.impl.JdeWsClientImpl
import com.sfr.sitemaster.shared.SiteHelper
import groovy.util.slurpersupport.GPathResult
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate
import java.time.ZoneId

/**
 * Created by piotr on 19/10/15.
 */
class SiteMergeUtilSpecification extends Specification {
    @Shared
    Site site
    @Shared
    GPathResult smResponse
    @Shared
    JDEResponse jdeResponse

    def getVal = {
        GPathResult val -> val.text().trim().empty ? null : val.text().trim()
    }

    def setupSpec() {
        smResponse = new XmlSlurper().parseText('''
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns0:siteMasterQueryResponse xmlns:ns0="http://oracle.e1.bssv.JP550103/">
         <siteMasterResponseSubVo>
            <siteMasterAddressVo>
               <EANCode>5790001326784</EANCode>
               <areaSalesManager>401695</areaSalesManager>
               <areaSalesManagerName>Andersen Thomas</areaSalesManagerName>
               <chainConvenience>Y</chainConvenience>
               <clusterName>Not applicable</clusterName>
               <compName>57052</compName>
               <companyName>1-2-3 AKSDAL</companyName>
               <concept_Category>1-2-3</concept_Category>
               <country>Norway</country>
               <dealer_Stationman>57052</dealer_Stationman>
               <dealer_StationmanName>1-2-3 AKSDAL</dealer_StationmanName>
               <district>Automat Sør/Øst</district>
               <mailingName>1-2-3 AKSDAL</mailingName>
               <orgNumber></orgNumber>
               <owner>123 Automate</owner>
               <salesZone>Nord Norge</salesZone>
               <siteID>57052</siteID>
               <siteMasterAddressDetailVo>
                  <address>123</address>
                  <city_place>AKSDAL</city_place>
                  <countyName>Rogaland Fylke</countyName>
                  <zipCode>5570</zipCode>
               </siteMasterAddressDetailVo>
               <siteMasterEmailVo>
                  <emailAddressow>57052@statoilfuelretail.com</emailAddressow>
               </siteMasterEmailVo>
               <siteMasterEmailVo>
                  <emailAddressow>dshelpno@statoilfuelretail.com</emailAddressow>
               </siteMasterEmailVo>
               <siteMasterEmailVo>
                  <emailAddressow>57052@statoilfuelretail.com</emailAddressow>
               </siteMasterEmailVo>
               <siteMasterEmailVo>
                  <emailAddressow>dshelpno@statoilfuelretail.com</emailAddressow>
               </siteMasterEmailVo>
               <siteMasterPhoneVo>
                  <phoneNumber>80200123</phoneNumber>
                  <phoneNumberType>WOR</phoneNumberType>
                  <phonePrefix>+45</phonePrefix>
               </siteMasterPhoneVo>
               <siteMasterPhoneVo>
                  <phoneNumber>0920-19902</phoneNumber>
                  <phoneNumberType>FAX</phoneNumberType>
                  <phonePrefix></phonePrefix>
               </siteMasterPhoneVo>
               <siteName>1-2-3 AKSDAL</siteName>
               <siteType>Automate</siteType>
               <status>Active</status>
            </siteMasterAddressVo>
         </siteMasterResponseSubVo>
      </ns0:siteMasterQueryResponse>
   </soap:Body>
</soap:Envelope>
''').Body.siteMasterQueryResponse.siteMasterResponseSubVo.siteMasterAddressVo
        jdeResponse = JdeWsClientImpl.parseJdeResponse(smResponse)
        site = SiteHelper.createEntity()
        site.openingInfo.temporarilyClosed = []
        site.openingInfo.temporarilyClosed << new TemporarilyClosed(from: LocalDate.parse('2015-01-02'), to: LocalDate.parse('2015-02-02'))
        site.openingInfo.alwaysOpen = false
        site.services = []
        site.services << new Service(id: 1, displayName: 'DN1', type: 'T1', lang: 'EN')
        site.services << new Service(id: 2, displayName: 'DN2', type: 'T2', lang: 'EN')
        site.services << new Service(id: 2, displayName: 'DN3', type: 'T3', lang: 'EN')
    }


    def 'should merge Site entity and Soap response into single object SiteDTO'() {
        when:
        def dataSources = [:]
        dataSources.put(Owner.SITEMASTER, site)
        dataSources.put(Owner.JDE, jdeResponse)
        SiteDTO siteDTO = SiteMergeUtil.from(dataSources).to(SiteDTO)

        then:
        //from soap
        assert siteDTO.siteId == getVal(smResponse.siteID) as Long
        assert siteDTO.stationName == getVal(smResponse.siteName)
        assert siteDTO.stationMan == getVal(smResponse.dealer_StationmanName)
        assert siteDTO.chainConvenience == (getVal(smResponse.chainConvenience) == 'Y')
        assert siteDTO.category == getVal(smResponse.concept_Category)
        assert siteDTO.stationType == getVal(smResponse.owner)
        assert siteDTO.district == getVal(smResponse.district)
        assert siteDTO.stationFormat == getVal(smResponse.siteType)
        assert siteDTO.salesZone == getVal(smResponse.salesZone)
        assert siteDTO.compName == getVal(smResponse.companyName)
        assert siteDTO.orgNumber == getVal(smResponse.orgNumber)
        assert siteDTO.clusterName == getVal(smResponse.clusterName)
        assert siteDTO.dealerStationManName == getVal(smResponse.dealer_StationmanName)
        assert siteDTO.areaSalesManagerName == getVal(smResponse.areaSalesManagerName)
        assert siteDTO.status == getVal(smResponse.status)
        assert siteDTO.address.street == getVal(smResponse.siteMasterAddressDetailVo.address)
        assert siteDTO.address.postalCode == getVal(smResponse.siteMasterAddressDetailVo.zipCode)
        assert siteDTO.address.city == getVal(smResponse.siteMasterAddressDetailVo.city_place)
        assert siteDTO.address.county == getVal(smResponse.siteMasterAddressDetailVo.countyName)
        assert siteDTO.phone == getVal(smResponse.siteMasterPhoneVo[0].phonePrefix) + getVal(smResponse.siteMasterPhoneVo[0].phoneNumber)
        assert siteDTO.fax == getVal(smResponse.siteMasterPhoneVo[1].phonePrefix) + getVal(smResponse.siteMasterPhoneVo[1].phoneNumber)
        //from entity
        siteDTO.openingInfo.alwaysOpen == site.openingInfo.alwaysOpen
        siteDTO.openingInfo.openingTimes.each { k, v ->
            assert v.open == site.openingInfo.openingTimes[k].open
            assert v.close == site.openingInfo.openingTimes[k].close
        }
        siteDTO.openingInfo.temporarilyClosed[0].from == site.openingInfo.temporarilyClosed[0].from
        siteDTO.openingInfo.temporarilyClosed[0].to == site.openingInfo.temporarilyClosed[0].to
        siteDTO.services.each { v ->
            site.services.find { it.id == v.id } != null
        }

    }

    def 'should create SiteDTO without site entity'() {
        when:
        def dataSources = [:]
        dataSources.put(Owner.JDE, jdeResponse)
        SiteDTO siteDTO = SiteMergeUtil.from(dataSources).to(SiteDTO)

        then:
        assert siteDTO.siteId == getVal(smResponse.siteID) as Long
        assert siteDTO.stationName == getVal(smResponse.siteName)
        assert siteDTO.stationMan == getVal(smResponse.dealer_StationmanName)
        assert siteDTO.chainConvenience == (getVal(smResponse.chainConvenience) == 'Y')
        assert siteDTO.category == getVal(smResponse.concept_Category)
        assert siteDTO.stationType == getVal(smResponse.owner)
        assert siteDTO.district == getVal(smResponse.district)
        assert siteDTO.stationFormat == getVal(smResponse.siteType)
        assert siteDTO.salesZone == getVal(smResponse.salesZone)
        assert siteDTO.compName == getVal(smResponse.companyName)
        assert siteDTO.orgNumber == getVal(smResponse.orgNumber)
        assert siteDTO.clusterName == getVal(smResponse.clusterName)
        assert siteDTO.dealerStationManName == getVal(smResponse.dealer_StationmanName)
        assert siteDTO.areaSalesManagerName == getVal(smResponse.areaSalesManagerName)
        assert siteDTO.status == getVal(smResponse.status)
        assert siteDTO.address.street == getVal(smResponse.siteMasterAddressDetailVo.address)
        assert siteDTO.address.postalCode == getVal(smResponse.siteMasterAddressDetailVo.zipCode)
        assert siteDTO.address.city == getVal(smResponse.siteMasterAddressDetailVo.city_place)
        assert siteDTO.address.county == getVal(smResponse.siteMasterAddressDetailVo.countyName)
        assert siteDTO.phone == getVal(smResponse.siteMasterPhoneVo[0].phonePrefix) + getVal(smResponse.siteMasterPhoneVo[0].phoneNumber)
        assert siteDTO.fax == getVal(smResponse.siteMasterPhoneVo[1].phonePrefix) + getVal(smResponse.siteMasterPhoneVo[1].phoneNumber)

        //from entity
//        siteDTO.openingInfo.alwaysOpen == site.openingInfo.alwaysOpen
//        siteDTO.openingInfo.openingTimes.each { k, v ->
//            assert v.open == site.openingInfo.openingTimes[k].open
//            assert v.close == site.openingInfo.openingTimes[k].close
//        }
//        siteDTO.openingInfo.temporarilyClosed[0].from == site.openingInfo.temporarilyClosed[0].from
//        siteDTO.openingInfo.temporarilyClosed[0].to == site.openingInfo.temporarilyClosed[0].to

    }

    def 'should update Site entity with data from SiteDTO'() {
        when:
        SiteDTO siteDTO = new SiteDTO()
        siteDTO.openingInfo = new OpeningInfoDTO()
        siteDTO.openingInfo.alwaysOpen = false
        siteDTO.openingInfo.openingTimes.put(Days.WEEKDAYS, new OpeningInfoDTO.OpeningTimeDTO(open: '08:00', close: '20:00'))
        siteDTO.openingInfo.openingTimes.put(Days.SUNDAY, new OpeningInfoDTO.OpeningTimeDTO(open: '10:00', close: '16:00'))
        siteDTO.openingInfo.temporarilyClosed << new OpeningInfoDTO.TemporarilyClosedDTO(from: LocalDate.parse('2015-09-08'), to: LocalDate.parse('2016-01-01'))
        Site site = SiteMergeUtil.from(siteDTO).create(Site)

        then:
        assert site.openingInfo.alwaysOpen == false

        assert site.openingInfo.temporarilyClosed[0].from == LocalDate.parse('2015-09-08')
        assert site.openingInfo.temporarilyClosed[0].to == LocalDate.parse('2016-01-01')

        assert site.openingInfo.openingTimes[Days.WEEKDAYS].open == '08:00'
        assert site.openingInfo.openingTimes[Days.WEEKDAYS].close == '20:00'
        assert site.openingInfo.openingTimes[Days.SUNDAY].open == '10:00'
        assert site.openingInfo.openingTimes[Days.SUNDAY].close == '16:00'

    }

    //TODO: @piotr Should throw specific exception if there is a problem with mergin

    def 'should convert dates to LocalDate'() {
        given:
        def util = SiteMergeUtil.from(null)
        expect:
        util.getLocalDateFromSource(src, Void) == LocalDate.parse('2002-09-09')
        where:
        src                                                                                       | _
        new XmlSlurper().parseText('<date>2002-09-09</date>')                                     | _
        LocalDate.parse('2002-09-09')                                                             | _
        Date.from(LocalDate.parse('2002-09-09').atStartOfDay(ZoneId.systemDefault()).toInstant()) | _


    }
}