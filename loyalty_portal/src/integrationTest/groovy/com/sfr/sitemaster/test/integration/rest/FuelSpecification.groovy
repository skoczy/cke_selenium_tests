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
class FuelSpecification extends AbstractRestSpecification {
    @Shared
    def fuelsCount = 32
    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")
    @Shared
    def cookies = [:]
    @Shared
    def fuelId = 41
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

    def 'should retrieve one fuel by id'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/fuel/${fuelId}", headers: cookies)
        site = response.data
        then:
        response.status == 200
        response.data.name == 'miles 98'
        response.data.icon == 'FeatureFuelMiles98'
        response.data.type == 'miles 98'
        response.data.lang == 'en'

    }

    def 'should retrieve all fuels'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: '/api/v1/res/fuel', headers: cookies)
        then:
        response.status == 200
        response.data.size == fuelsCount
    }

    def 'should retrieve fuels for a site'() {
        when:
        //TODO: @anders update this for services
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/fuel/site/${siteId}", headers: cookies)
        then:
        response.status == 200
        response.data.size == 4
        response.data.find { it.type == 'miles 92' } != null
        response.data.find { it.type == 'miles 95' } != null
        response.data.find { it.type == 'miles Diesel' } != null
        response.data.find { it.type == 'milesPLUS Diesel' } != null
    }

}
