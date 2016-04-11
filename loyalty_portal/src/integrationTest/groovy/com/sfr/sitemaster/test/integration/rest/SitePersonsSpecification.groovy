package com.sfr.sitemaster.test.integration.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.sfr.sitemaster.dto.SitePersonRoleDTO
import data.LiquiTool
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC

/**
 * Created by piotr on 09/09/15.
 */
@Stepwise
class SitePersonsSpecification extends AbstractRestSpecification {
    @Shared
    @Inject
    ObjectMapper objectMapper

    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")

    @Shared
    def cookies = [:]

    @Shared
    def rolesDTO = [
            'SM' : new SitePersonRoleDTO(name: 'SM', label: 'Station Manager', fromJDE: true),
            'SMA': new SitePersonRoleDTO(name: 'SMA', label: 'Station Manager Assistant', fromJDE: false),
            'ASM': new SitePersonRoleDTO(name: 'ASM', label: 'Area Sales Manager', fromJDE: true),
            'CT1': new SitePersonRoleDTO(name: 'CT1', label: 'Caretaker1', fromJDE: false),
            'CT2': new SitePersonRoleDTO(name: 'CT2', label: 'Caretaker2', fromJDE: false),
    ]

    def setupSpec() {
        LiquiTool.reset();
        restClient.handler.failure = restClient.handler.success
        // Log in first
        HttpResponseDecorator response = restClient.post(
                path: '/api/v1/unres/login',
                body: [username: username, password: pass],
                headers: cookies,
                requestContentType: URLENC)
        //log.debug(response.data)
        assert response.status == 200
        response.getHeaders('Set-Cookie').each { cookies['Cookie'] = it.value.split(';')[0] }
    }


    def 'should add a site person to a site'() {
        when:
        def siteId = 57055
        HttpResponseDecorator responseGET1 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        def sitePersons = [] + responseGET1.data
        sitePersons << [name: 'Site Person1', phone: 111111111, role: rolesDTO['SMA']]
        sitePersons << [name: 'Site Person2', phone: 222222222, role: rolesDTO['SMA']]
        sitePersons << [name: 'Site Person3', phone: 333333333, role: rolesDTO['SMA']]
        HttpResponseDecorator responseSAVE = restClient.post(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, body: objectMapper.writeValueAsString(sitePersons), requestContentType: JSON)
        HttpResponseDecorator responseGET2 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        then:
        responseGET1.status == 200
        responseSAVE.status == 204
        responseGET2.status == 200
        responseGET2.data.size() == responseGET1.data.size() + 3
    }

    def 'should get Site Persons for a Site'() {
        when:
        def siteId = 57055
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        log.debug(response.data)
        then:
        response.status == 200
        response.data.findAll { !it.role.fromJDE }.size() == 3
        response.data.find { !it.role.fromJDE && it.name == 'Site Person1' }.phone == 111111111
        response.data.find { !it.role.fromJDE && it.name == 'Site Person2' }.phone == 222222222
        response.data.find { !it.role.fromJDE && it.name == 'Site Person3' }.phone == 333333333
        response.data.findAll { it.role.fromJDE }.size() == 2
    }

    def 'should update every field of NSM Site persons but not name for JDE'() {
        when:
        def siteId = 57055
        HttpResponseDecorator responseGET1 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        def sitePersons = [] + responseGET1.data
        sitePersons.each {
            it.with {
                name = 'Modified'
                phone = 12345678
                email = 'modified@email'
            }
        }
        HttpResponseDecorator responseSAVE = restClient.post(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, body: objectMapper.writeValueAsString(sitePersons), requestContentType: JSON)
        HttpResponseDecorator responseGET2 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        log.debug(responseGET2.data)
        then:
        responseGET1.status == 200
        responseSAVE.status == 204
        responseGET2.status == 200
        responseGET2.data.findAll { it.role.fromJDE }.size() == 2
        responseGET2.data.findAll { it.role.fromJDE }.each {
            assert it.name != 'Modified'
            assert it.email == 'modified@email'
            assert it.phone == 12345678
        }
        responseGET2.data.findAll { !it.role.fromJDE }.each {
            assert it.name == 'Modified'
            assert it.email == 'modified@email'
            assert it.phone == 12345678
        }

    }

    def 'should remove NSM Site persons but not JDE'() {
        when:
        def siteId = 57055
        HttpResponseDecorator responseGET1 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        def sitePersons = []
        HttpResponseDecorator responseSAVE = restClient.post(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, body: objectMapper.writeValueAsString(sitePersons), requestContentType: JSON)
        HttpResponseDecorator responseGET2 = restClient.get(path: "/api/v1/res/site/${siteId}/sitepersons", headers: cookies, requestContentType: JSON)
        log.debug(responseGET2.data)
        then:
        responseGET1.status == 200
        responseSAVE.status == 204
        responseGET2.status == 200
        responseGET2.data.findAll { it.role.fromJDE }.size() == 2
        responseGET2.data.findAll { !it.role.fromJDE }.size() == 0
    }
}
