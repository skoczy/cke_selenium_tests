package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by piotr on 09/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class UserDTO {
    String email
    List<String> roles
}
