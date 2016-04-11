package com.sfr.sitemaster.test.integration.rest

import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import spock.lang.Specification
import spock.lang.Unroll

/**
 * Created by yveshwang on 26/10/15.
 */
class JDESpecification extends Specification {

    def static username = System.getProperty('jde.username=', 'sitemaster')
    def static password = System.getProperty('jde.password', 'wheredDadEafAt8knep}')

    def static Logger logger = LogManager.getLogger(JDESpecification.class.getName());

    @Unroll('JDE BSSV on #protocol://#hostname:#port#url for stations should respond with #status for #label')
    def 'testing JDE BSSV for Sitemaster'() {
        logger.info("\n\n=======================\n${protocol}://${hostname}:${port}${url}\n=======================")
        when:
        RESTClient restClient = new RESTClient("${protocol}://${hostname}:${port}")
        if (!hostname.toLowerCase().contains('google')) {
            restClient.headers['Authorization'] = 'Basic ' + "${username}:${password}".bytes.encodeBase64()
        }
        restClient.handler.failure = restClient.handler.success //standardising the access to response body
        restClient.properties.each { key, value ->
            logger.info("${key}=${value}")
        }
        restClient.ignoreSSLIssues()
        HttpResponseDecorator response
        try {
            if (queryString) {
                response = restClient.get(path: url, queryString: queryString)
            } else {
                response = restClient.get(path: url)
            }
        } catch (javax.net.ssl.SSLException ssl) {
            logger.error('SSL Exception occured.', ssl)
        } catch (all) {
            logger.error('generic error', all)
        }

        then:
        response != null
        response.status == status

        where:
        label                 | protocol | url                                                | queryString | status | port  | hostname
        'Google - sanity'     | 'http'   | '/'                                                | null        | 200    | 80    | 'www.google.com'
        'QA endpoint'         | 'https'  | '/jde-site-query.default/services/site-query'      | 'wsdl'      | 200    | 30443 | 'internal.cap.avengers.io'
        'Prod endpoint'       | 'https'  | '/jde-site-query-prod.default/services/site-query' | 'wsdl'      | 200    | 30443 | 'internal.cap.avengers.io'
        'apigw health check'  | 'https'  | '/_apigw/status'                                   | null        | 200    | 30443 | 'internal.cap.avengers.io'
        'QA - 10.24.228.65'   | 'https'  | '/jde-site-query.default/services/site-query'      | 'wsdl'      | 200    | 30443 | '10.24.228.65'
        'QA - 10.24.228.66'   | 'https'  | '/jde-site-query.default/services/site-query'      | 'wsdl'      | 200    | 30443 | '10.24.228.66'
        'QA - 10.24.228.67'   | 'https'  | '/jde-site-query.default/services/site-query'      | 'wsdl'      | 200    | 30443 | '10.24.228.67'
        'QA - 10.24.228.68'   | 'https'  | '/jde-site-query.default/services/site-query'      | 'wsdl'      | 200    | 30443 | '10.24.228.68'
        'Prod - 10.24.228.65' | 'https'  | '/jde-site-query-prod.default/services/site-query' | 'wsdl'      | 200    | 30443 | '10.24.228.65'
        'Prod - 10.24.228.66' | 'https'  | '/jde-site-query-prod.default/services/site-query' | 'wsdl'      | 200    | 30443 | '10.24.228.66'
        'Prod - 10.24.228.67' | 'https'  | '/jde-site-query-prod.default/services/site-query' | 'wsdl'      | 200    | 30443 | '10.24.228.67'
        'Prod - 10.24.228.68' | 'https'  | '/jde-site-query-prod.default/services/site-query' | 'wsdl'      | 200    | 30443 | '10.24.228.68'
    }

    //TODO: @piotr should respond with an easy tu understand error if there is a problem with JDE
}
