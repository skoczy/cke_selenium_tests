package com.sfr.sitemaster.test.integration.rest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Unroll

import static groovyx.net.http.ContentType.URLENC
/**
 * Created by piotr on 09/09/15.
 */
class LogoutSpecification extends AbstractRestSpecification {

    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")

    @Shared
    def headers = [:]

    @Unroll('LogoutResource should respond with #status when #when')
    def 'LogoutResource'() {
        when:
        restClient.handler.failure = restClient.handler.success
        headers = [:]
        if (loginFirst) {
            HttpResponseDecorator _response = restClient.post(
                    path: '/api/v1/unres/login',
                    body: [username: username, password: pass],
                    headers: cookies,
                    requestContentType: URLENC)
            _response.getHeaders('Set-Cookie').each { headers['Cookie'] = it.value.split(';')[0] }
            assert _response.status == 200
        }
        HttpResponseDecorator response = restClient.post(
                path: '/api/v1/unres/logout',
                headers: cookies,
                requestContentType: URLENC)

        then:
        response.status == status

        where:
        loginFirst | cookies | status | when
        true       | headers | 200    | ' user was previously authenticated and provided session cookie'
        false      | headers | 200    | ' user was not previously authenticated and provided session cookie'
        false      | null    | 200    | ' user was not previously authenticated and did not provide session cookie'
    }
}
