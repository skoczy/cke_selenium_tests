/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.domainservices;

import com.sfr.apicore.auth.User;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.entities.PersistentSession;

import javax.servlet.http.HttpServletRequest;

/**
 * Facade for PersistentSession entity
 *
 * @author tuxbear
 *
 */
public interface PersistentSessionService {
    void createNewPersistenSession(User user, String sessionId) throws DBException;

    PersistentSession findSessionFromKey(String sessionId) throws DBException;

    boolean isSessionValid(HttpServletRequest req) throws DBException;
}
