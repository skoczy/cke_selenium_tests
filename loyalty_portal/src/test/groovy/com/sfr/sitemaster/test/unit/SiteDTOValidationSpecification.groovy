/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.test.unit

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.dto.SiteDTO
import spock.lang.Shared
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory

/**
 * Created by piotr on 08/01/16.
 */
class SiteDTOValidationSpecification extends Specification {
    @Shared
    Validator validator

    @Shared
    String payload

    @Shared
    ObjectMapper objectMapper

    @Shared
    SiteDTO siteDTO


    def setupSpec() {
        objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.INDENT_OUTPUT, true);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.registerModule(new JavaTimeModule());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        payload = '''{
            "id" : 10895,
            "siteId" : 10895,
            "stationName" : "1-2-3 Fåborg",
            "stationMan" : "1-2-3 Fåborg",
            "category" : "1-2-3",
            "stationType" : "123 Automate",
            "district" : "Själland/Fyn",
            "stationFormat" : "Automate",
            "country" : "Denmark",
            "salesZone" : "Automatdrift",
            "compName" : "1-2-3 Fåborg",
            "orgNumber" : null,
            "clusterName" : "Not applicable",
            "dealerStationManName" : "1-2-3 Fåborg",
            "areaSalesManagerName" : "Møllerskov, Klaus",
            "status" : "Active",
            "eanCode" : 5790001329051,
            "email" : [ "cape@statoilfuelretail.com                                                                                                                                                                                                                                      ", "10895@statoilfuelretail.com                                                                                                                                                                                                                                     " ],
            "address" : {
                "street" : "Nyborgvej 56",
                "postalCode" : "5600",
                "city" : "Fåborg",
                "county" : "Denmark",
                "country" : null,
                "municipality" : null,
                "mailingAddress" : null,
                "mailingAddressPostalCode" : null
            },
            "openingInfo" : {
                "alwaysOpen" : true,
                "openingTimes" : { },
                "temporarilyClosed" : [ ]
            },
            "phone" : "+4580200123",
            "fax" : null,
            "xCoord" : "10.228996632892",
            "yCoord" : "55.111443484756",
            "chainConvenience" : false,
            "services" : [ {
                               "id" : 4,
                               "name" : "Routex Atlas",
                               "type" : "Routex Atlas",
                               "icon" : "FeatureRoutexAtlas",
                               "lang" : "en"
                           } ],
            "fuels" : [ {
                            "id" : 22,
                            "name" : "Blyfri 92",
                            "type" : "Blyfri 92",
                            "icon" : "FeatureFuelMiles92",
                            "lang" : "en"
                        }, {
                            "id" : 23,
                            "name" : "Blyfri 95",
                            "type" : "Blyfri 95",
                            "icon" : "FeatureFuelMiles95",
                            "lang" : "en"
                        } ]
        }'''

        siteDTO = objectMapper.readValue(payload, SiteDTO)

    }

    def 'Correct payload should validate without errors'() {
        when:
        def c = validator.validate(siteDTO)
        then:
        assert c.size() == 0
    }

    def 'should be invalid if null from/to in temporarilyClosed'() {
        when:
        siteDTO.openingInfo.temporarilyClosed[0] = new OpeningInfoDTO.TemporarilyClosedDTO(from: null, to: null)
        def c = validator.validate(siteDTO)
        then:
        assert c.size() == 3
    }

    def 'should accept payload if null temporarilyClosed'() {
        when:
        siteDTO.openingInfo.temporarilyClosed = null
        def c = validator.validate(siteDTO)
        then:
        assert c.size() == 0
    }
}