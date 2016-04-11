package com.sfr.sitemaster.test.unit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.dto.SiteDTO
import com.sfr.sitemaster.entities.TemporarilyClosed
import groovy.json.JsonSlurper
import spock.lang.Shared
import spock.lang.Specification

import java.time.LocalDate

/**
 * Created by piotr on 09/09/15.
 */
class SiteJSONSpecification extends Specification {

    @Shared
    ObjectMapper objectMapper

    def setupSpec() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());
    }


    def 'Jackson should convert Site dates correctly'() {
        when:
        SiteDTO site = new SiteDTO()
        site.openingInfo = new OpeningInfoDTO();
        site.openingInfo.temporarilyClosed = []
        site.openingInfo.temporarilyClosed << new TemporarilyClosed(from: LocalDate.parse(inOperationFrom), to: LocalDate.parse(inOperationFrom))
        StringWriter writer = new StringWriter();
        objectMapper.writeValue(writer, site)

        then:
        def results = new JsonSlurper().parseText(writer.toString());
        results.openingInfo.temporarilyClosed[0].from == expectedFrom
        results.openingInfo.temporarilyClosed[0].to == expectedFrom
        where:
        inOperationFrom | expectedFrom
        '2001-04-04'    | [2001, 4, 4]
    }
}
