package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

/**
 * Created by piotr on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SitePersonDTO {

    @SMProperty(path = 'name')
    String name
    @SMProperty(path = 'phone')
    Integer phone
    @SMProperty(path = 'phonecc')
    Integer phonecc
    @SMProperty(path = 'role', target = SitePersonRoleDTO.class)
    SitePersonRoleDTO role
    @SMProperty(path = 'email')
    String email
    @SMProperty(path = 'externalId')
    String externalId
}
