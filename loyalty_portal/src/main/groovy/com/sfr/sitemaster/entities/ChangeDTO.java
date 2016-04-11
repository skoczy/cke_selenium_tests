/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Site Entity.
 *
 * @author piotr
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@SuppressWarnings("PMD.GodClass")
public class ChangeDTO  {
    List<String> path;
    String before;
    String after;
    String state;

    public List<String> getPath() {
        return path;
    }

    public void setPath(final List<String> path) {
        this.path = path;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(final String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(final String after) {
        this.after = after;
    }

    public String getState() {
        return state;
    }

    public void setState(final String state) {
        this.state = state;
    }
}
