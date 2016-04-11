package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sfr.sitemaster.enums.Owner

/**
 * Created by piotr on 23/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class PhoneDTO {
    @SMProperty(owner = Owner.JDE, path = 'phonePrefix', label = 'Prefix')
    def prefix
    @SMProperty(owner = Owner.JDE, path = 'phoneNumber', label = 'Number')
    def number
    @SMProperty(owner = Owner.JDE, path = 'phoneNumberType', label = 'Phone type')
    def type
}
