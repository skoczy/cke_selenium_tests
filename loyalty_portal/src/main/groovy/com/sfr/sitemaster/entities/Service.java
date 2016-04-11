/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.entities;

import org.hibernate.envers.Audited;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Site Entity.
 *
 * @author piotr
 */
@Entity
@Audited
@Table(name = "SERVICE")
@DiscriminatorValue("SERVICE")
public class Service extends BaseService {
    private static final long serialVersionUID = 2381931182052314399L;

}
