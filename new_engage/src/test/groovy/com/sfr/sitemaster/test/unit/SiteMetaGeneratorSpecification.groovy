package com.sfr.sitemaster.test.unit

import com.sfr.sitemaster.commons.DtoMetaGenerator
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.dto.SiteDTO
import groovy.json.JsonOutput
import spock.lang.Specification

/**
 * Created by piotr on 19/10/15.
 */
class SiteMetaGeneratorSpecification extends Specification {


    def 'should create meta for SiteDTO'() {
        when:
        def json = new groovy.json.JsonSlurper().parseText(JsonOutput.toJson(DtoMetaGenerator.generate(SiteDTO.class)))
        then:
        json.fields?.siteId?.label == 'Station ID'
        json.fields?.siteId?.owner == 'JDE'
        json.types?.openingInfo?.alwaysOpen.owner == 'SITEMASTER'
        json.types?.openingTimes.open.owner == 'SITEMASTER'
        json.types?.openingTimes.close.owner == 'SITEMASTER'

    }

    def 'should create meta for SiteDTO/OpeningInfo/TemporarilyClosed'() {
        when:
        def json = new groovy.json.JsonSlurper().parseText(JsonOutput.toJson(DtoMetaGenerator.generate(OpeningInfoDTO.class)))
        then:
        json.types?.temporarilyClosed.from.owner == 'SITEMASTER'
        json.types?.temporarilyClosed.from.type == 'date'
        json.types?.temporarilyClosed.to.owner == 'SITEMASTER'
        json.types?.temporarilyClosed.to.type == 'date'
    }

}