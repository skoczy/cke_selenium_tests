/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*; //NOPMD
import java.util.List;

/**
 * SearchFilterDTO describes search conditions.
 *
 * @author piotr
 */
@Entity
@JsonIgnoreProperties(value = {"created", "modelSchema", "predefinedSearch"}, ignoreUnknown = true)
public class SearchFilter extends JpaSFREntityObject {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    public enum OPERATOR {
        EQUALS("EQUALS"), NOT_EQUALS("NOT_EQUALS"), LIKE("LIKE"), NOT_LIKE("NOT_LIKE"), STARTS_WITH("STARTS_WITH");

        private String name;

        OPERATOR(final String name) {
            this.name = name;
        }

        @JsonCreator
        public static OPERATOR fromValue(final String value) {
            for (final OPERATOR operator : OPERATOR.values()) {
                if (operator.name != null && operator.name.equalsIgnoreCase(value)) {
                    return operator;
                }
            }
            return null;
        }
    }

    String field;
    OPERATOR operator;
    @ElementCollection(fetch = FetchType.EAGER)
    List<String> value;
    @ManyToOne
    PredefinedSearch predefinedSearch;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getField() {
        return field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public OPERATOR getOperator() {
        return operator;
    }

    public void setOperator(final OPERATOR operator) {
        this.operator = operator;
    }

    public List<String> getValue() {
        return value;
    }

    public void setValue(final List<String> value) {
        this.value = value;
    }

    public PredefinedSearch getPredefinedSearch() {
        return predefinedSearch;
    }

    public void setPredefinedSearch(final PredefinedSearch predefinedSearch) {
        this.predefinedSearch = predefinedSearch;
    }
}
