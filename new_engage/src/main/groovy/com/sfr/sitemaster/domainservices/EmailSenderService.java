package com.sfr.sitemaster.domainservices;

/**
 * Created by tuxbear on 07/01/15.
 */
public interface EmailSenderService {

    void sendEmail(String to, String from, String message);
}
