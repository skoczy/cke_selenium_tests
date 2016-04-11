/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.dao;


import com.sfr.apicore.dao.BasicDao;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.entities.PersistentSession;

/**
 * Facade for PersistentSession entity db access
 *
 * @author tuxbear
 *
 */
public interface PersistentSessionDao extends BasicDao<Long, PersistentSession> {

    PersistentSession findFromSessionId(String sessionKey) throws DBException;

}
