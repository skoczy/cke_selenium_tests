/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Site Entity.
 *
 * @author piotr
 */
@SuppressWarnings("PMD.GodClass")
@JsonIgnoreProperties(value = {"created", "modelSchema"}, ignoreUnknown = true)
public class ChangeSetDTO {

    List<ChangeDTO> changes;
    String modifiedBy;
    LocalDateTime date;


    public List<ChangeDTO> getChanges() {
        return changes;
    }

    public void setChanges(final List<ChangeDTO> changes) {
        this.changes = changes;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(final LocalDateTime date) {
        this.date = date;
    }

}
