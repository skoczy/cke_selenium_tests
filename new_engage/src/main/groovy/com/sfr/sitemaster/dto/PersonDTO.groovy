package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by piotr on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PersonDTO {
    def name
    def mobile
}
