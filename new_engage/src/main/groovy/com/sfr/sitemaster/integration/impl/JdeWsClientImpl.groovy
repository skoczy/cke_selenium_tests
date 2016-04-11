package com.sfr.sitemaster.integration.impl

import com.google.inject.Inject
import com.sfr.apicore.commons.PropertyService
import com.sfr.sitemaster.exceptions.JDEException
import com.sfr.sitemaster.integration.JdeWsClient
import com.sfr.sitemaster.integration.impl.JDEResponse.JDEPhoneResponse
import com.sfr.sitemaster.rx.ObservableSOAPClient
import com.sfr.sitemaster.rx.ObservableSOAPClientFactory
import groovy.util.slurpersupport.GPathResult
import rx.Observable
import rx.exceptions.Exceptions

/**
 * Created by piotr on 09/10/15.
 */
class JdeWsClientImpl implements JdeWsClient {

    def static String SOAP_ACTION = 'http://oracle.e1.bssv.JP550103/SiteMasterManager/siteMasterQueryRequest'
    def static String JDE_URL_PROPERTY = 'jde.url'
    def static String JDE_USERNAME_PROPERTY = 'jde.username'
    def static String JDE_PASSWORD_PROPERTY = 'jde.password'
    private ObservableSOAPClient client

    @Inject
    public JdeWsClientImpl(ObservableSOAPClientFactory clientFactory, PropertyService propertyService) {
        this(clientFactory, propertyService.getProperty(JDE_URL_PROPERTY), propertyService.getProperty(JDE_USERNAME_PROPERTY), propertyService.getProperty(JDE_PASSWORD_PROPERTY))
    }

    public JdeWsClientImpl(ObservableSOAPClientFactory clientFactory, String url, String username, String password) {
        this.client = clientFactory.get(SOAP_ACTION, url, username, password)
    }

    public def Observable<JDEResponse> getSiteById(final Long id) {
        client.sendAndObserve() {
            body {
                's:siteMasterQuery'('xmlns:s': 'http://oracle.e1.bssv.JP550103/') {
                    siteMasterRequestSubVo {
                        siteID(id)
                    }
                }
            }
        }.map {
            if (!it.Body.Fault.faultcode.isEmpty()) {
                final def msg = it.Body.Fault.faultstring.text()
                return Exceptions.propagate(new JDEException(msg))
            }
            if (it.Body.siteMasterQueryResponse.siteMasterResponseSubVo.siteMasterAddressVo.isEmpty()) {
                return Exceptions.propagate(new JDEException("Site ${id} not found"))
            }
            parseJdeResponse it.Body.siteMasterQueryResponse.siteMasterResponseSubVo.siteMasterAddressVo
        }
    }

    def static JDEResponse parseJdeResponse(final GPathResult gPathResult) {
        def getLong = { !it.text().trim().empty ? it.text().trim() as Long : null }
        def getString =  {!it.text().trim().empty ? it.text().trim() : null }
        def getBoolean =  {!it.text().trim().empty ? it.text().trim() == 'Y' : null }

        final JDEResponse jdeResponse = new JDEResponse();
        jdeResponse.with {
            EANCode = getLong gPathResult['EANCode']
            areaSalesManager = getString gPathResult['areaSalesManager']
            areaSalesManagerName = getString gPathResult['areaSalesManagerName']
            chainConvenience = getBoolean gPathResult['chainConvenience']
            clusterName = getString gPathResult['clusterName']
            compName = getString gPathResult['compName']
            companyName = getString gPathResult['companyName']
            concept_Category = getString gPathResult['concept_Category']
            country = getString gPathResult['country']
            dealer_Stationman = getString gPathResult['dealer_Stationman']
            dealer_StationmanName = getString gPathResult['dealer_StationmanName']
            district = getString gPathResult['district']
            mailingName = getString gPathResult['mailingName']
            orgNumber = getString gPathResult['orgNumber']
            owner = getString gPathResult['owner']
            salesZone = getString gPathResult['salesZone']
            siteID = getLong gPathResult['siteID']
            siteName = getString gPathResult['siteName']
            siteType = getString gPathResult['siteType']
            status = getString gPathResult['status']
        }
        jdeResponse.siteMasterAddressDetail = new JDEResponse.JDEAddressResponse()
        jdeResponse.siteMasterAddressDetail.address = getString gPathResult['siteMasterAddressDetailVo']['address']
        jdeResponse.siteMasterAddressDetail.city_place = getString gPathResult['siteMasterAddressDetailVo']['city_place']
        jdeResponse.siteMasterAddressDetail.countyName = getString gPathResult['siteMasterAddressDetailVo']['countyName']
        jdeResponse.siteMasterAddressDetail.zipCode = getString gPathResult['siteMasterAddressDetailVo']['zipCode']

        jdeResponse.siteMasterEmail = gPathResult['siteMasterEmailVo'].collect { it.emailAddressow }.unique() as String[]

        jdeResponse.siteMasterPhone = [:]
        gPathResult['siteMasterPhoneVo'].each { phone ->
            final JDEPhoneResponse jdePhoneResponse = new JDEPhoneResponse()
            jdePhoneResponse.with {
                phoneNumber = getString(phone['phoneNumber'])
                phoneNumberType = getString(phone['phoneNumberType'])
                phonePrefix = getString(phone['phonePrefix'])
            }
            jdeResponse.siteMasterPhone.put(jdePhoneResponse.phoneNumberType, jdePhoneResponse)
        }
        jdeResponse
    }
}