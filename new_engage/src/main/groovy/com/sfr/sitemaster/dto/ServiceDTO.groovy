package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sfr.sitemaster.enums.Owner

/**
 * Created by anders on 30/11/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ServiceDTO {
    @SMProperty(owner = Owner.SITEMASTER, path = 'id', label = 'Id', target = Long.class)
    Long id
    @SMProperty(owner = Owner.SITEMASTER, path = 'displayName', label = 'Name')
    String name
    @SMProperty(owner = Owner.SITEMASTER, path = 'type', label = 'Type')
    String type
    @SMProperty(owner = Owner.SITEMASTER, path = 'icon', label = 'Icon')
    String icon
    @SMProperty(owner = Owner.SITEMASTER, path = 'lang', label = 'Language')
    String lang

    public boolean equals(final Object obj) {
        if (obj instanceof ServiceDTO) {
            return ((ServiceDTO) obj).id == this.id && ((ServiceDTO) obj).type.equals(this.type);
        }
        return false;
    }

    public int hashCode() {
        return (id * type.hashCode()) / 2;
    }
}
