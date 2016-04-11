package com.sfr.sitemaster.test.integration.rest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Unroll

import static groovyx.net.http.ContentType.URLENC

/**
 * Created by piotr on 09/09/15.
 */
class LoginSpecification extends AbstractRestSpecification {

    @Shared
    def headers = [:]


    @Unroll('LoginResource should respond with #status if: #label')
    def 'LoginResource should respond with authorize user with a provided good session cookie'() {
        when:
        RESTClient restClient = new RESTClient("http://localhost:${port}")
        restClient.handler.failure = restClient.handler.success
        HttpResponseDecorator response = restClient.post(
                path: '/api/v1/unres/login',
                body: [username: username, password: password],
                headers: cookies,
                requestContentType: URLENC)
        response.getHeaders('Set-Cookie').each { headers['Cookie'] = it.value.split(';')[0] }

        then:
        response.status == status

        where:
        label                       | username         | password           | cookies | status
        'good credentials'          | 'mobile@app.com' | 'CanUHardcodeit12' | null    | 200
        'wrong password'            | 'foobar'         | 'wrong'            | null    | 401
        'only cookie'               | null             | null               | headers | 401     // Let's use cookie, no credentials
        'no credentials, no cookie' | null             | null               | null    | 401     // No credentials, no cookies
    }
}
