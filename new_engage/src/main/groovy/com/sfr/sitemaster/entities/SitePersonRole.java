package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by piotr on 19/10/15.
 */
@Entity
@Audited
@JsonIgnoreProperties(value = {"created", "modelSchema", "timestamp"}, ignoreUnknown = true)
public class SitePersonRole extends JpaSFREntityObject {

    @Id
    String name;

    String label;

    Boolean fromJDE;


    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(final String label) {
        this.label = label;
    }

    public Boolean getFromJDE() {
        return fromJDE;
    }

    public void setFromJDE(final Boolean fromJDE) {
        this.fromJDE = fromJDE;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public void setId(final Long aLong) {
        //KEEP IT
    }

}
