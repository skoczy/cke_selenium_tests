package com.sfr.sitemaster.test.unit.domainservices

import com.sfr.sitemaster.dao.SiteDao
import com.sfr.sitemaster.dao.SitePersonsDao
import com.sfr.sitemaster.dao.SitePersonsRoleDao
import com.sfr.sitemaster.domainservices.impl.SitePersonsServiceImpl
import com.sfr.sitemaster.dto.SitePersonDTO
import com.sfr.sitemaster.dto.SitePersonRoleDTO
import com.sfr.sitemaster.entities.SitePerson
import com.sfr.sitemaster.entities.SitePersonRole
import com.sfr.sitemaster.integration.JdeWsClient
import com.sfr.sitemaster.integration.impl.JDEResponse
import com.sfr.sitemaster.integration.impl.JdeWsClientImpl
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by piotr on 02/11/15.
 */
class SitePersonsUnitSpecification extends Specification {
    @Shared
    SiteDao siteDao
    @Shared
    SitePersonsDao sitePersonsDao
    @Shared
    SitePersonsRoleDao sitePersonsRoleDao
    @Shared
    SitePersonsServiceImpl sitePersonsService
    @Shared
    JdeWsClient jdeWsClient
    @Shared
    Map roles = [:]
    @Shared
    Map rolesDTO = [:]
    @Shared
    def smResponse
    @Shared
    JDEResponse jdeResponse

    def setupSpec() {
        roles = [
                'SM' : new SitePersonRole(name: 'SM', label: 'Station Manager', fromJDE: true),
                'SMA': new SitePersonRole(name: 'SMA', label: 'Station Manager Assistant', fromJDE: false),
                'ASM': new SitePersonRole(name: 'ASM', label: 'Area Sales Manager', fromJDE: true),
                'CT1': new SitePersonRole(name: 'CT1', label: 'Caretaker1', fromJDE: false),
                'CT2': new SitePersonRole(name: 'CT2', label: 'Caretaker2', fromJDE: false),
        ]
        rolesDTO = [
                'SM' : new SitePersonRoleDTO(name: 'SM', label: 'Station Manager', fromJDE: true),
                'SMA': new SitePersonRoleDTO(name: 'SMA', label: 'Station Manager Assistant', fromJDE: false),
                'ASM': new SitePersonRoleDTO(name: 'ASM', label: 'Area Sales Manager', fromJDE: true),
                'CT1': new SitePersonRoleDTO(name: 'CT1', label: 'Caretaker1', fromJDE: false),
                'CT2': new SitePersonRoleDTO(name: 'CT2', label: 'Caretaker2', fromJDE: false),
        ]
        siteDao = Mock(SiteDao)
        sitePersonsDao = Mock(SitePersonsDao)
        sitePersonsRoleDao = Mock(SitePersonsRoleDao)
        jdeWsClient = Mock(JdeWsClient)
        sitePersonsRoleDao.getByName('SM') >> roles['SM']
        sitePersonsRoleDao.getByName('SMA') >> roles['SMA']
        sitePersonsRoleDao.getByName('ASM') >> roles['ASM']
        sitePersonsRoleDao.getByName('CT2') >> roles['CT2']
        sitePersonsService = new SitePersonsServiceImpl(siteDao, sitePersonsDao, sitePersonsRoleDao, jdeWsClient)


        smResponse = new XmlSlurper().parseText('''
<soap:Envelope xmlns:soap="http://schemas.xmlsoap.org/soap/envelope/">
   <soap:Body>
      <ns0:siteMasterQueryResponse xmlns:ns0="http://oracle.e1.bssv.JP550103/">
         <siteMasterResponseSubVo>
            <siteMasterAddressVo>
               <EANCode>5790001326784</EANCode>
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
               <siteMasterPhoneVo>
                  <phoneNumber>80200123</phoneNumber>
                  <phoneNumberType>OFF</phoneNumberType>
                  <phonePrefix>+45</phonePrefix>
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
    }

    def 'should filter out JDE persons'() {
        setup:
        def sitePersonsDTOs = [
                new SitePersonDTO(name: 'SP1', role: rolesDTO['SM'], email: 'external@sfr', phone: 22333242, externalId: 007),
                new SitePersonDTO(name: 'SP2', role: rolesDTO['SMA']),
                new SitePersonDTO(name: 'SP2', role: rolesDTO['CT2'])
        ]
        when:
        def sitePersons = sitePersonsService.filterOutJdePersons(sitePersonsDTOs)
        then:
        sitePersons.find { it.role.fromJDE }.each {
            assert it.name == null
            assert it.externalId != null
            assert it.email == 'external@sfr'
            assert it.phone == 22333242
        }
        sitePersons.find { !it.role.fromJDE }.each {
            assert it.name != null
            assert it.externalId == null
        }
    }

    def 'should return merged JDE and NSM site persons'() {
        setup:
        def sitePersons = [
                new SitePerson(name: 'SP1', role: roles['SM'], externalId: '464654'),
                new SitePerson(name: 'SP2', role: roles['SMA']),
                new SitePerson(name: 'SP3', role: roles['CT2'])
        ]

        when:
        List<SitePersonDTO> sitePersonDTOs = sitePersonsService.mergeSitePerson(sitePersons, jdeResponse)
        then:
        sitePersonDTOs.find { it.role.name == rolesDTO['SM'].name }.each {
            assert it.externalId == smResponse['dealer_Stationman'].text()
            assert it.name == smResponse['dealer_StationmanName'].text()
        }
        sitePersonDTOs.find { it.role.name == rolesDTO['ASM'].name }.each {
            assert it.externalId == smResponse['areaSalesManager'].text()
            assert it.name == smResponse['areaSalesManagerName'].text()
        }
    }

}