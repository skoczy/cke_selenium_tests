package com.sfr.sitemaster.test.unit

import com.sfr.sitemaster.exceptions.EntityNotFoundException
import com.sfr.sitemaster.exceptions.JDEException
import com.sfr.sitemaster.integration.impl.JdeWsClientImpl
import com.sfr.sitemaster.rx.ObservableSOAPClient
import com.sfr.sitemaster.rx.ObservableSOAPClientFactory
import groovy.util.slurpersupport.GPathResult
import rx.Observable
import rx.observers.TestSubscriber
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by piotr on 11/12/15.
 */
class JdeUnitSpecification extends Specification {

    @Shared
    JdeWsClientImpl jdeWsClient

    @Shared
    ObservableSOAPClientFactory clientFactory

    @Shared
    ObservableSOAPClient client

    def setup() {
        clientFactory = Mock(ObservableSOAPClientFactory)
        client = Mock(ObservableSOAPClient)
        clientFactory.get(JdeWsClientImpl.SOAP_ACTION, null, null, null) >> client
        jdeWsClient = new JdeWsClientImpl(clientFactory, null, null, null)
    }

    def 'should throw JDEException if soap response contains faultstring'() {
        setup:
        def faultyResponse = new XmlSlurper().parseText('''<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/"><soap:Body>
<soap:Fault><faultcode xmlns:ns1="http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd">ns1:FailedAuthentication</faultcode>
<faultstring>Failed to assert identity with UsernameToken.</faultstring></soap:Fault></soap:Body></soap:Envelope>''')
        client.sendAndObserve(_) >> Observable.just(faultyResponse)
        when:
        TestSubscriber<GPathResult> testSubscriber = new TestSubscriber<>();
        jdeWsClient.getSiteById(1).subscribe(testSubscriber)
        then:
        testSubscriber.assertError(JDEException)
    }

    def 'should parse JDE Response'() {
        setup:
        GPathResult jdeResponseXML = new XmlSlurper().parseText('''
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
        def getVal = {
            GPathResult val -> val.text().trim().empty ? null : val.text().trim()
        }
        when:
        def jdeResponse = jdeWsClient.parseJdeResponse(jdeResponseXML)
        then:
        assert getVal(jdeResponseXML['EANCode']) as Long == jdeResponse.EANCode
        assert getVal(jdeResponseXML['areaSalesManager']) == jdeResponse.areaSalesManager
        assert getVal(jdeResponseXML['areaSalesManagerName']) == jdeResponse.areaSalesManagerName
        assert (getVal(jdeResponseXML['chainConvenience']) == 'Y') == jdeResponse.chainConvenience
        assert getVal(jdeResponseXML['clusterName']) == jdeResponse.clusterName
        assert getVal(jdeResponseXML['compName']) == jdeResponse.compName
        assert getVal(jdeResponseXML['companyName']) == jdeResponse.companyName
        assert getVal(jdeResponseXML['concept_Category']) == jdeResponse.concept_Category
        assert getVal(jdeResponseXML['country']) == jdeResponse.country
        assert getVal(jdeResponseXML['dealer_Stationman']) == jdeResponse.dealer_Stationman
        assert getVal(jdeResponseXML['dealer_StationmanName']) == jdeResponse.dealer_StationmanName
        assert getVal(jdeResponseXML['district']) == jdeResponse.district
        assert getVal(jdeResponseXML['mailingName']) == jdeResponse.mailingName
        assert getVal(jdeResponseXML['orgNumber']) == jdeResponse.orgNumber
        assert getVal(jdeResponseXML['owner']) == jdeResponse.owner
        assert getVal(jdeResponseXML['salesZone']) == jdeResponse.salesZone
        assert getVal(jdeResponseXML['siteID']) as Long == jdeResponse.siteID
        assert getVal(jdeResponseXML['siteName']) == jdeResponse.siteName
        assert getVal(jdeResponseXML['siteType']) == jdeResponse.siteType
        assert getVal(jdeResponseXML['status']) == jdeResponse.status


        assert getVal(jdeResponseXML['siteMasterPhoneVo'][0]['phoneNumber']) == jdeResponse.siteMasterPhone['WOR'].phoneNumber
        assert getVal(jdeResponseXML['siteMasterPhoneVo'][0]['phoneNumberType']) == jdeResponse.siteMasterPhone['WOR'].phoneNumberType
        assert getVal(jdeResponseXML['siteMasterPhoneVo'][0]['phonePrefix']) == jdeResponse.siteMasterPhone['WOR'].phonePrefix

        assert getVal(jdeResponseXML['siteMasterPhoneVo'][1]['phoneNumber']) == jdeResponse.siteMasterPhone['FAX'].phoneNumber
        assert getVal(jdeResponseXML['siteMasterPhoneVo'][1]['phoneNumberType']) == jdeResponse.siteMasterPhone['FAX'].phoneNumberType
        assert getVal(jdeResponseXML['siteMasterPhoneVo'][1]['phonePrefix']) == jdeResponse.siteMasterPhone['FAX'].phonePrefix


        assert getVal(jdeResponseXML['siteMasterEmailVo'][0]['emailAddressow']) == jdeResponse.siteMasterEmail[0]
        assert getVal(jdeResponseXML['siteMasterEmailVo'][1]['emailAddressow']) == jdeResponse.siteMasterEmail[1]


        assert getVal(jdeResponseXML['siteMasterAddressDetailVo']['address']) == jdeResponse.siteMasterAddressDetail.address
        assert getVal(jdeResponseXML['siteMasterAddressDetailVo']['city_place']) == jdeResponse.siteMasterAddressDetail.city_place
        assert getVal(jdeResponseXML['siteMasterAddressDetailVo']['countyName']) == jdeResponse.siteMasterAddressDetail.countyName
        assert getVal(jdeResponseXML['siteMasterAddressDetailVo']['zipCode']) == jdeResponse.siteMasterAddressDetail.zipCode
    }

    def 'should throw EntityNotFoundException if soap response contains empty site'() {
        setup:
        def emptyResponse = new XmlSlurper().parseText('''<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns0:siteMasterQueryResponse xmlns:ns0="http://oracle.e1.bssv.JP550103/">
         <siteMasterResponseSubVo/>
      </ns0:siteMasterQueryResponse>
   </soap:Body>
</soap:Envelope>''')
        client.sendAndObserve(_) >> Observable.just(emptyResponse)
        when:
        TestSubscriber<GPathResult> testSubscriber = new TestSubscriber<>();
        jdeWsClient.getSiteById(1).subscribe(testSubscriber)
        then:
        testSubscriber.assertError(JDEException)
    }
}