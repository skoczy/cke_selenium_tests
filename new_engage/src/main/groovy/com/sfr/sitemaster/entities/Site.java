/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import javax.validation.Valid;
import java.util.List;

/**
 * Site Entity.
 *
 * @author piotr
 */
@Entity
@SuppressWarnings("PMD.GodClass")
@Audited(withModifiedFlag = true)
public class Site extends JpaSFREntityObject implements HasModifiedBy {

    private static final long serialVersionUID = 1L;

    @Id
    protected Long id;

    @Embedded
    @Audited
    @Valid
    private OpeningInfo openingInfo;
    private String xCoord;
    private String yCoord;
    @ManyToMany
    @JoinTable(name = "site_service", inverseJoinColumns = @JoinColumn(name = "service_id", referencedColumnName = "id"))
    private List<Service> services;
    @ManyToMany
    @JoinTable(name = "site_fuel", inverseJoinColumns = @JoinColumn(name = "fuel_id", referencedColumnName = "id"))
    private List<Fuel> fuels;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "site")
    private List<SitePerson> sitePersons;
    @Version
    @Column(name = "_version", columnDefinition = "integer DEFAULT 0", nullable = false)
    Long version;
    @Column(name = "modifiedby")
    String modifiedBy;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public OpeningInfo getOpeningInfo() {
        return openingInfo;
    }

    public void setOpeningInfo(final OpeningInfo openingInfo) {
        this.openingInfo = openingInfo;
    }

    public String getxCoord() {
        return xCoord;
    }

    public void setxCoord(final String xCoord) {
        this.xCoord = xCoord;
    }

    public String getyCoord() {
        return yCoord;
    }

    public void setyCoord(final String yCoord) {
        this.yCoord = yCoord;
    }

    public List<Service> getServices() {
        return services;
    }

    public void setServices(final List<Service> services) {
        this.services = services;
    }

    public List<Fuel> getFuels() {
        return fuels;
    }

    public void setFuels(final List<Fuel> fuels) {
        this.fuels = fuels;
    }

    public List<SitePerson> getSitePersons() {
        return sitePersons;
    }

    public void setSitePersons(final List<SitePerson> sitePersons) {
        this.sitePersons = sitePersons;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(final String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }
}
