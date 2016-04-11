package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.sfr.sitemaster.enums.Days;
import org.hibernate.envers.Audited;

import javax.persistence.*; //NOPMD
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * Created by piotr on 01/09/15.
 */
@Entity
@Audited
public class OpeningTime extends JpaSFREntityObject implements Serializable {

    private static final long serialVersionUID = -8030015539057048003L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    Days days;

    @NotNull
    @Pattern(regexp = "([01]?[0-9]|2[0-3]):[0-5][0-9]")
    String open;

    @NotNull
    @Pattern(regexp = "([01]?[0-9]|2[0-4]):[0-5][0-9]")
    String close;

    @ManyToOne
    Site site;

    public OpeningTime() {
        //empty
    }

    public OpeningTime(final Days days, final String open, final String close) {
        super();
        this.days = days;
        this.open = open;
        this.close = close;
    }

    public OpeningTime(final Site site, final Days days, final String open, final String close) {
        this(days, open, close);
        this.site = site;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(final Site site) {
        this.site = site;
    }

    public Days getDays() {
        return days;
    }

    public void setDays(final Days days) {
        this.days = days;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getOpen() {
        return open;
    }

    public void setOpen(final String open) {
        this.open = open;
    }

    public String getClose() {
        return close;
    }

    public void setClose(final String close) {
        this.close = close;
    }

    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final OpeningTime that = (OpeningTime) o;
        if (getId() != null && !getId().equals(that.getId())) {
            return false;
        }
        if (getId() == null && that.getId() != null) {
            return false;
        }

        return true;
    }

    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        }
        return 0;

    }
}
