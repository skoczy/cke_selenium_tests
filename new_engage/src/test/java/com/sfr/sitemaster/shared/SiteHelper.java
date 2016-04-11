/*
 *  Copyright (c) 2015 Statoil Fuel & Retail ASA
 *  All rights reserved.
 *  
 *  This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 *  distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.shared;

import com.sfr.sitemaster.entities.OpeningInfo;
import com.sfr.sitemaster.entities.OpeningTime;
import com.sfr.sitemaster.entities.Site;
import com.sfr.sitemaster.enums.Days;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by piotr on 18.08.15.
 */
public final class SiteHelper {
    private SiteHelper() {
    }

    public static Site createEntity() {
        final Random generator = new Random();
        return createEntity(generator.nextInt(10));
    }

    public static Site createEntity(final Integer id) {
        final Site site = new Site();
        site.setId(Long.valueOf(id));
        final OpeningInfo openingInfo = new OpeningInfo();
        openingInfo.setAlwaysOpen(false);
        openingInfo.setOpeningTimes(new HashMap<Days, OpeningTime>());
        openingInfo.getOpeningTimes().put(Days.WEEKDAYS, new OpeningTime(site, Days.WEEKDAYS, "08:00", "22:00"));
        openingInfo.getOpeningTimes().put(Days.SATURDAY, new OpeningTime(site, Days.SATURDAY, "09:00", "18:00"));
        openingInfo.getOpeningTimes().put(Days.SUNDAY, new OpeningTime(site, Days.SUNDAY, "12:00", "16:00"));
        site.setOpeningInfo(openingInfo);
        return site;
    }
}
