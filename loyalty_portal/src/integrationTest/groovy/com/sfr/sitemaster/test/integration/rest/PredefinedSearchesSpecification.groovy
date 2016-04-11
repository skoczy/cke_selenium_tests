package com.sfr.sitemaster.test.integration.rest
import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Inject
import com.sfr.sitemaster.dao.impl.PredefinedSearchDAOImpl
import com.sfr.sitemaster.entities.PredefinedSearch
import com.sfr.sitemaster.entities.SearchFilter
import groovy.json.JsonOutput
import groovyx.net.http.HttpResponseDecorator
import groovyx.net.http.RESTClient
import spock.lang.Shared

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.ContentType.URLENC
/**
 * Created by piotr on 09/09/15.
 */
class PredefinedSearchesSpecification extends AbstractRestSpecification {
    @Inject
    @Shared
    ObjectMapper objectMapper
    @Shared
    def predefSearch
    @Inject
    @Shared
    PredefinedSearchDAOImpl predefSearchDao;

    @Shared
    RESTClient restClient = new RESTClient("http://localhost:${port}")

    @Shared
    def cookies = [:]

    def setup() {
        predefSearchDao.removeAll();
        def p1 = new PredefinedSearch(name: 'OnlyActive')
        p1.searchFilters = [new SearchFilter(field: 'status', operator: SearchFilter.OPERATOR.EQUALS, value: ['Active'], predefinedSearch: p1)]


        def p2 = new PredefinedSearch(name: '1-2-3')
        p2.searchFilters = [
                new SearchFilter(field: 'marketingName', operator: SearchFilter.OPERATOR.LIKE, value: ['1-2-3', '3-2-1'], predefinedSearch: p2),
                new SearchFilter(field: 'status', operator: SearchFilter.OPERATOR.EQUALS, value: ['Active'], predefinedSearch: p2)
        ]
        predefSearch = predefSearchDao.save p1
        predefSearchDao.save p2

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

    def 'should retrieve all predefined searches'() {
        when:
        HttpResponseDecorator response = restClient.get(
                path: '/api/v1/res/predefinedSearches',
                headers: cookies)
        then:
        response.data.size == 2
        response.status == 200
    }

    def 'should retrieve one predefined search'() {
        when:
        HttpResponseDecorator response = restClient.get(path: "/api/v1/res/predefinedSearches/${predefSearch.id}", headers: cookies)
        then:
        response.status == 200
        response.data.id == predefSearch.id
        response.data.name == predefSearch.name
        response.data.searchFilters.size() == predefSearch.searchFilters.size()
    }

    def 'should remove one predefined search'() {
        when:
        HttpResponseDecorator response = restClient.delete(path: "/api/v1/res/predefinedSearches/${predefSearch.id}", headers: cookies)
        then:
        response.status == 200
    }

    def 'should add predefined search'() {
        when:
        def predefSearch = JsonOutput.toJson([name: 'Test', searchFilters: [
                [field: 'marketingName', operator: SearchFilter.OPERATOR.LIKE, value: ['1-2-3', '3-2-1']],
                [field: 'status', operator: SearchFilter.OPERATOR.EQUALS, value: ['Active']]

        ]])
        HttpResponseDecorator response = restClient.post(
                path: '/api/v1/res/predefinedSearches',
                body: predefSearch,
                headers: cookies,
                requestContentType: JSON)
        then:
        response.status == 200
    }
}
