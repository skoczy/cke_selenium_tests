/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*; //NOPMD
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Site Entity.
 *
 * @author piotr
 */
@Entity
@JsonIgnoreProperties(value = {"created", "modelSchema"}, ignoreUnknown = true)
@SuppressWarnings("PMD.GodClass")
public class PredefinedSearch extends JpaSFREntityObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotNull
    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true, mappedBy = "predefinedSearch")
    List<SearchFilter> searchFilters;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public void setSearchFilters(final List<SearchFilter> searchFilters) {
        this.searchFilters = searchFilters;
    }

    @PrePersist
    public void updateFilters() {
        if (getSearchFilters() == null) {
            return;
        }
        for (final SearchFilter searchFilter : getSearchFilters()) {
            searchFilter.setPredefinedSearch(this);
        }
    }
}
