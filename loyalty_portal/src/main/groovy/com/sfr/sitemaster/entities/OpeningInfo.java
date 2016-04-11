package com.sfr.sitemaster.entities;

import com.sfr.sitemaster.enums.Days;
import org.hibernate.envers.AuditJoinTable;
import org.hibernate.envers.Audited;

import javax.persistence.*; //NOPMD
import javax.validation.Valid;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Created by piotr on 10.08.15.
 */
@Embeddable
public class OpeningInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "site")
    @MapKeyEnumerated(EnumType.STRING)
    @Audited
    @MapKey(name = "days")
    @Valid
    Map<Days, OpeningTime> openingTimes;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "site_id", referencedColumnName = "id", nullable = false)
    @AuditJoinTable(name = "aud_site_temporarilyclosed")
    @Audited
    List<TemporarilyClosed> temporarilyClosed;

    Boolean alwaysOpen;

    public Map<Days, OpeningTime> getOpeningTimes() {
        return openingTimes;
    }

    public void setOpeningTimes(final Map<Days, OpeningTime> openingTimes) {
        this.openingTimes = openingTimes;
    }

    public List<TemporarilyClosed> getTemporarilyClosed() {
        return temporarilyClosed;
    }

    public void setTemporarilyClosed(final List<TemporarilyClosed> temporarilyClosed) {
        this.temporarilyClosed = temporarilyClosed;
    }

    public Boolean getAlwaysOpen() {
        return alwaysOpen;
    }

    public void setAlwaysOpen(final Boolean alwaysOpen) {
        this.alwaysOpen = alwaysOpen;
    }
}
