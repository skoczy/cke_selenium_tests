package com.sfr.sitemaster.test.integration.rest

import data.LiquiTool
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by piotr on 09/09/15.
 */
@Stepwise
class ServiceSpecification extends AbstractRestSpecification {
    @Shared
    def servicesCount = 16
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")
    @Shared
    def cookies = [:]
    @Shared
    def serviceId = 3
    @Shared
    def siteId = 10282
    @Shared
    def site

    def setup() {
        restClient.handler.failure = restClient.handler.success
        // Log in first
        HttpResponseDecorator response = restClient.post(
                path: '/api/v1/unres/login',
                body: [username: username, password: pass],
                headers: cookies,
                requestContentType: URLENC)
        assert response.status == 200
        response.getHeaders('Set-Cookie').each { cookies['Cookie'] = it.value.split(';')[0] }
    }

    def setupSpec() {
        LiquiTool.reset();
    }

    def 'should retrieve one service by id'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/service/${serviceId}", headers: cookies)
        site = response.data
        then:
        response.status == 200
        response.data.name == 'Simply Great Coffee'
        response.data.icon == 'FeatureCoffee'
        response.data.type == 'Simply Great Coffee'
        response.data.lang == 'en'

    }

    def 'should retrieve all services'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: '/api/v1/res/service', headers: cookies)
        then:
        response.status == 200
        response.data.size == servicesCount
    }

    def 'should retrieve services for a site'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/service/site/${siteId}", headers: cookies)
        then:
        response.status == 200
        response.data.size == 4
        response.data.find { it.type == 'Trailer rental' } != null
        response.data.find { it.type == 'Simply Great Coffee' } != null
        response.data.find { it.type == 'Routex Atlas' } != null
        response.data.find { it.type == 'Car wash' } != null
    }

}
