package com.sfr.sitemaster.rev;

import org.hibernate.envers.RevisionNumber;
import org.hibernate.envers.RevisionTimestamp;
import org.quartz.utils.FindbugsSuppressWarnings;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

/**
 * Created by piotr on 17/11/15.
 */
@Entity
@org.hibernate.envers.RevisionEntity
@FindbugsSuppressWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class RevisionEntity implements Serializable {
    private static final long serialVersionUID = 8530213963961662300L;

    @Id
    @GeneratedValue(generator = "revision_seq", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(name = "revision_seq", sequenceName = "revision_sequence", allocationSize = 1)
    @RevisionNumber
    private int id;

    @RevisionTimestamp
    private long timestamp;

    @Transient
    public Date getRevisionDate() {
        return new Date(timestamp);
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(final long timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof RevisionEntity)) {
            return false;
        }

        final RevisionEntity that = (RevisionEntity) o;
        return id == that.id
                && timestamp == that.timestamp;
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "DefaultRevisionEntity(id = " + id
                + ", revisionDate = " + DateFormat.getDateTimeInstance().format(getRevisionDate()) + ")";
    }
}
