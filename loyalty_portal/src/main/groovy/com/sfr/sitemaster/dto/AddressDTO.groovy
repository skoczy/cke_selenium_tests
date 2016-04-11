package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sfr.sitemaster.enums.Owner

/**
 * Created by piotr on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class AddressDTO {
    @SMProperty(owner = Owner.JDE, path = 'address', label = 'Street')
    String street
    @SMProperty(owner = Owner.JDE, path = 'zipCode', label = 'Postal code')
    String postalCode
    @SMProperty(owner = Owner.JDE, path = 'city_place', label = 'City')
    String city
    @SMProperty(owner = Owner.JDE, path = 'countyName', label = 'County')
    String county
    String country
    String municipality
    String mailingAddress
    String mailingAddressPostalCode
}
