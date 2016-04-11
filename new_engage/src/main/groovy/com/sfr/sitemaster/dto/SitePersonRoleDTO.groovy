package com.sfr.sitemaster.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by piotr on 19/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SitePersonRoleDTO {
    @SMProperty(path = 'name')
    String name;
    @SMProperty(path = 'label')
    String label;
    @SMProperty(path = 'fromJDE')
    Boolean fromJDE;
}
