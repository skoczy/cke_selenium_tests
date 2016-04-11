package com.sfr.sitemaster.test.integration.rest

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.enums.Days
import data.LiquiTool
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared
import spock.lang.Stepwise

import java.time.LocalDate

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC

/**
 * Created by piotr on 09/09/15.
 */
@Stepwise
class SiteSpecification extends AbstractRestSpecification {
    @Shared
    @Inject
    ObjectMapper objectMapper
    @Shared
    def sitesCount = 2356

    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")

    @Shared
    def cookies = [:]
    @Shared
    def siteId = 10282
    @Shared
    def nonExistingSiteId = 61000
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

    def 'SiteResource should respond with 404 if Site does not exist'() {
        when:
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/site/${nonExistingSiteId}", headers: cookies)
        site = response.data
        then:
        response.status == 404
    }

    def 'SiteResource should retrieve one Site'() {
        when:

        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/site/${siteId}", headers: cookies)
        site = response.data
        then:
        response.status == 200
    }

    def 'SiteResource should retrieve ${sitesCount} sites'() {
        when:
        HttpResponseDecorator response = restClient.get(path: '/api/v1/res/site', headers: cookies)
        then:
        response.status == 200
        response.data.size == sitesCount
    }

    def 'SiteResource should update opening times'() {
        when:
        site.openingInfo.openingTimes[Days.WEEKDAYS.toString()] = new OpeningInfoDTO.OpeningTimeDTO(open: open, close: close);
        HttpResponseDecorator response = restClient.put(path: "/api/v1/res/site/${site.id}", headers: cookies, body: site, requestContentType: JSON)
        then:
        response.status == status
        where:
        id    | open    | close   | status
        57055 | '04:00' | '22:30' | 204
    }

    def 'SiteResource should update a Site with temporarily closed data or reject if invalid dates'() {
        when:
        site.openingInfo.temporarilyClosed << new OpeningInfoDTO.TemporarilyClosedDTO(from: from, to: to)
        HttpResponseDecorator response = restClient.put(path: "/api/v1/res/site/${site.id}", headers: cookies, body: objectMapper.writeValueAsString(site), requestContentType: JSON)
        log.debug(response.data)
        then:
        response.status == status
        where:
        id    | from                          | to                            | status
        57055 | LocalDate.now().minusYears(1) | LocalDate.now()               | 204
        57055 | LocalDate.now()               | LocalDate.now().minusDays(10) | 400
    }

    def 'SiteResource should update x/y coords for a Site'() {
        when:
        site = restClient.get(path: "/api/v1/res/site/${siteId}", headers: cookies).data;
        site.xCoord = xCoord
        site.yCoord = yCoord
        HttpResponseDecorator response = restClient.put(path: "/api/v1/res/site/${site.id}", headers: cookies, body: objectMapper.writeValueAsString(site), requestContentType: JSON)
        then:
        response.status == status
        where:
        id    | xCoord  | yCoord  | status
        57055 | '3.123' | '4.123' | 204
    }

//    @Unroll('SiteResource should return #sitesCount search results for #field using #operator operator.')
//    def 'SiteResource should return search results based on search filters'() {
//        when:
//        def json = JsonOutput.toJson([[field: field, operator: operator, value: value]])
//        HttpResponseDecorator response = restClient.post(path: '/api/v1/res/site/search', headers: cookies, body: json, requestContentType: JSON)
//        then:
//        response.status == status
//        response.data.size == sitesCount
//        where:
//        field           | operator     | value                               | status | sitesCount
//        'marketingName' | 'equals'     | ['1-2-3 AKSDAL', '1-2-3 ARNATVEIT'] | 200    | 2
//        'status'        | 'equals'     | ['Active']                          | 200    | 30
//        'status'        | 'not_equals' | ['Active']                          | 200    | 0
//    }

    def 'should return a changeLog for a single Site'() {
        when:
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/site/${site.id}/changeLog", headers: cookies)
        then:
        response.data.size == 3
        response.data[0].changes.size() == 2
        response.data[1].changes.size() == 2
        response.data[2].changes.size() == 2
    }

    def 'SiteResource should accept empty temporarilyClosed data'() {
        when:
        site.openingInfo.temporarilyClosed = []
        HttpResponseDecorator response = restClient.put(path: "/api/v1/res/site/${site.id}", headers: cookies, body: site, requestContentType: JSON)
        log.debug(response.data)
        then:
        response.status == 204
    }

    def 'SiteResource should not accept incorrect temporarilyClosed data'() {
        when:
        site.openingInfo.temporarilyClosed[]
        site.openingInfo.temporarilyClosed[0] = [from: null, to: null]
        HttpResponseDecorator response = restClient.put(path: "/api/v1/res/site/${site.id}", headers: cookies, body: site, requestContentType: JSON)
        log.debug(response.data)
        then:
        response.status == 400
    }
}
