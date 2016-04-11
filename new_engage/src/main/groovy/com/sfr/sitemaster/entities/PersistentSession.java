package com.sfr.sitemaster.entities;

import apicore.dao.jpa.entities.JpaSFREntityObject;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by tuxbear on 08/01/15.
 */
@Entity
public class PersistentSession extends JpaSFREntityObject {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    String userId;
    String sessionId;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(final Long id) {
        this.id = id;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(final String sessionId) {
        this.sessionId = sessionId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(final String userId) {
        this.userId = userId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        final PersistentSession that = (PersistentSession) o;

        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) { // NOPMD
            return false;
        }
        if (userId != null ? !userId.equals(that.userId) : that.userId != null) { // NOPMD
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (userId != null ? userId.hashCode() : 0); // NOPMD
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0); // NOPMD
        return result;
    }
}
