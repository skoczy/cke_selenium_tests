package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import org.hibernate.envers.Audited;

import javax.persistence.*; //NOPMD

/**
 * Created by piotr on 19/10/15.
 */
@Entity
@Audited
public class SitePerson extends JpaSFREntityObject {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    protected String externalId;

    String name;
    Integer phone;
    Integer phonecc;
    @OneToOne
    @JoinColumn(name = "role")
    SitePersonRole role;
    String email;

    @ManyToOne(targetEntity = Site.class)
    Site site;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(final String externalId) {
        this.externalId = externalId;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Integer getPhone() {
        return phone;
    }

    public void setPhone(final Integer phone) {
        this.phone = phone;
    }

    public Integer getPhonecc() {
        return phonecc;
    }

    public void setPhonecc(final Integer phonecc) {
        this.phonecc = phonecc;
    }

    public SitePersonRole getRole() {
        return role;
    }

    public void setRole(final SitePersonRole role) {
        this.role = role;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(final Site site) {
        this.site = site;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
