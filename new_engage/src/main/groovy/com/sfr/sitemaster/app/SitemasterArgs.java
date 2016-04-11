package com.sfr.sitemaster.app;

import org.apache.commons.cli.*; //NOPMD

/**
 * Basic class which describes arguments which can be passed to the app.
 *
 * Created by piotr on 18/09/15.
 */
public final class SitemasterArgs { //NOPMD
    public static final String PORT = "port";
    public static final String SERVICE_PORT = "servicePort";
    public static final String CONTEXT_PATH = "contextPath";
    public static final String DISABLE_AUTH = "disableAuth";
    public static final String CONTENT_ROOT = "contentRoot";

    public static CommandLine get(final String... args) {
        final Options options = new Options();
        options.addOption(PORT, true, "HTTP server port");
        options.addOption(SERVICE_PORT, true, "Jetty service port");
        options.addOption(CONTEXT_PATH, true, "Server context path");
        options.addOption(DISABLE_AUTH, false, "Disables authentication");
        options.addOption(CONTENT_ROOT, true, "Directory with content which should be served");

        final CommandLineParser parser = new DefaultParser();
        final CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
            return cmd;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
