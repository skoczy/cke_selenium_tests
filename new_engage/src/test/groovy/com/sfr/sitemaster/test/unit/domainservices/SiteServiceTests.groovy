/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.test.unit.domainservices

import com.sfr.apicore.exceptions.pojo.DBException
import com.sfr.sitemaster.dao.FuelDao
import com.sfr.sitemaster.dao.ServiceDao
import com.sfr.sitemaster.dao.SiteDao
import com.sfr.sitemaster.domainservices.SiteService
import com.sfr.sitemaster.domainservices.impl.SiteServiceImpl
import com.sfr.sitemaster.dto.SiteDTO
import com.sfr.sitemaster.entities.PersistentSession
import com.sfr.sitemaster.entities.Site
import com.sfr.sitemaster.entities.TemporarilyClosed
import com.sfr.sitemaster.exceptions.EntityNotFoundException
import com.sfr.sitemaster.integration.JdeWsClient
import com.sfr.sitemaster.integration.impl.JdeWsClientImpl
import com.sfr.sitemaster.shared.SiteHelper
import com.sfr.sitemaster.unit.UnitTestBase
import groovy.util.slurpersupport.GPathResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.runners.MockitoJUnitRunner
import rx.Observable

import javax.security.auth.login.CredentialNotFoundException
import java.time.LocalDate

import static org.mockito.Matchers.any
import static org.mockito.Mockito.when

@RunWith(MockitoJUnitRunner.class)
public class SiteServiceTests extends UnitTestBase {
    final private static Long siteId = 2L

    @Mock
    private SiteDao siteDaoMock

    @Mock
    private ServiceDao servicesDaoMock

    @Mock
    private FuelDao fuelDaoMock

    @Mock
    private JdeWsClient jdeWsClient

    @Mock
    private SiteService siteService

    @Before
    def void reset() {
        Mockito.reset(siteDaoMock)
        siteService = new SiteServiceImpl(siteDaoMock, jdeWsClient, servicesDaoMock, fuelDaoMock)

        def site = SiteHelper.createEntity()
        site.openingInfo.temporarilyClosed = []
        site.openingInfo.temporarilyClosed << new TemporarilyClosed(from: LocalDate.parse('2015-01-02'), to: LocalDate.parse('2015-02-02'))
        site.openingInfo.alwaysOpen = false
        when(siteDaoMock.getObservableById(any())).thenReturn(Observable.just(site))
        GPathResult smResponse = new XmlSlurper().parseText('''
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns0:siteMasterQueryResponse xmlns:ns0="http://oracle.e1.bssv.JP550103/">
         <siteMasterResponseSubVo>
            <siteMasterAddressVo>
               <areaSalesManager>401695</areaSalesManager>
               <areaSalesManagerName>Andersen Thomas</areaSalesManagerName>
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
               <siteName>1-2-3 AKSDAL</siteName>
               <siteType>Automate</siteType>
               <status>Active</status>
            </siteMasterAddressVo>
         </siteMasterResponseSubVo>
      </ns0:siteMasterQueryResponse>
   </soap:Body>
</soap:Envelope>
''').Body.siteMasterQueryResponse.siteMasterResponseSubVo.siteMasterAddressVo
        def jdeResponse = JdeWsClientImpl.parseJdeResponse(smResponse)
        when(jdeWsClient.getSiteById(any())).thenReturn(Observable.just(jdeResponse))
    }

    @Test
    public void entityIsRetrieved() throws DBException, CredentialNotFoundException, EntityNotFoundException {
        Assert.assertEquals('AKSDAL', siteService.get(siteId).address.city)
    }

    @Test(expected = EntityNotFoundException.class)
    def void noEntityFails() throws DBException, CredentialNotFoundException, EntityNotFoundException {
        when(siteDaoMock.getObservableById(siteId)).thenThrow(new EntityNotFoundException(Site.class, siteId))
        siteService.get(siteId)
    }

    @Test
    def void incompleteSiteUpdate() throws DBException, CredentialNotFoundException, EntityNotFoundException {
        SiteDTO site = siteService.get(siteId)
        site.fuels = null
        site.services = null
        site.openingInfo = null
        siteService.save(new PersistentSession(), site)
    }


}
