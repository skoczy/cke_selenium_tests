package com.sfr.sitemaster.domainservices.impl;

import org.apache.log4j.Logger;

import com.sfr.sitemaster.domainservices.EmailSenderService;

/**
 * Created by tuxbear on 07/01/15.
 */
public class DummyEmailFacade implements EmailSenderService {

    Logger LOGGER = Logger.getLogger(DummyEmailFacade.class);

    @Override
    public void sendEmail(final String to, final String from, final String message) {
        LOGGER.info("Dummy email sent: to -> " + to + " from -> " + from + " message -> " + message);
    }
}
