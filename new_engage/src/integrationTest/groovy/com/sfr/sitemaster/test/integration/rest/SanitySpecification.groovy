package com.sfr.sitemaster.test.integration.rest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by piotr on 09/09/15.
 */
class SanitySpecification extends AbstractRestSpecification {

    def 'VersionResource should retrieve data describing the app'() {
        when:
        def properties = [:]
        RESTClient restClient = new RESTClient("http://localhost:${port}")
        restClient.handler.failure = restClient.handler.success
        HttpResponseDecorator response = restClient.get(
                path: '/api/v1/unres/version',
                requestContentType: URLENC)
        response.data.text.tokenize(',').each {
            def (key, value) = it.tokenize('=')
            properties[key?.trim()] = value
        }

        then:
        response.status == 200
        properties.version != null
        properties.name == 'siterumasteru'
        properties.timestamp
        properties.api_version
    }

    def 'VersionResource should retrieve data describing the app in JSON'() {
        when:
        RESTClient restClient = new RESTClient("http://localhost:${port}")
        restClient.handler.failure = restClient.handler.success
        HttpResponseDecorator response = restClient.get(
                path: '/api/v1/unres/versionjson',
                requestContentType: URLENC)
        then:
        response.status == 200
        response.data.version != null
        response.data.name == 'siterumasteru'
        response.data.timestamp
        response.data.api_version
    }

}
