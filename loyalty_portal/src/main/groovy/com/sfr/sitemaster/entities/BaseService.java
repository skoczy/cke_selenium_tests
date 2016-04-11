/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Site Entity.
 *
 * @author piotr
 */
@Entity
@DiscriminatorColumn
@Table(name = "service")
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
public abstract class BaseService extends JpaSFREntityObject implements Serializable {
    private static final long serialVersionUID = -8030015539057048003L;

    @Id
    @GeneratedValue(generator = "service_seq")
    @SequenceGenerator(name = "service_seq", sequenceName = "service_sequence", allocationSize = 10)
    protected Long id;

    private String type;
    private String icon;
    private String lang;
    private String displayName;


    public BaseService() {
        //empty
    }

    public BaseService(final String type, final String icon, final String lang, final String displayName) {
        this.type = type;
        this.icon = icon;
        this.lang = lang;
        this.displayName = displayName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(final String type) {
        this.type = type;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(final String icon) {
        this.icon = icon;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(final String lang) {
        this.lang = lang;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(final String displayName) {
        this.displayName = displayName;
    }

}
