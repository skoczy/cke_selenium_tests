package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;
import com.sfr.sitemaster.validation.ValidateDates;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

/**
 * Created by piotr on 01/09/15.
 */
@Entity
@Audited
@ValidateDates(date = "from", compareTo = "to", rule = ValidateDates.RULE.NOT_AFTER)
public class TemporarilyClosed extends JpaSFREntityObject {

    private static final long serialVersionUID = 8355744161440478098L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "\"from\"")
    LocalDate from;
    @Column(name = "\"to\"")
    LocalDate to;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDate getFrom() {
        return from;
    }

    public void setFrom(final LocalDate from) {
        this.from = from;
    }

    public LocalDate getTo() {
        return to;
    }

    public void setTo(final LocalDate to) {
        this.to = to;
    }

    @Override
    public boolean equals(final Object obj) { //NOPMD
        if (obj instanceof TemporarilyClosed) {
            return ((TemporarilyClosed) obj).from.isEqual(this.from) && ((TemporarilyClosed) obj).to.isEqual(this.to);
        }
        return false;
    }

    @Override
    public int hashCode() {     //NOPMD
        return super.hashCode();
    }
}
