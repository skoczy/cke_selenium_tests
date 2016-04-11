package com.sfr.sitemaster.exceptions;

/**
 * Created by piotr on 13.08.15.
 */
public class SearchFilterException extends Exception {

    private static final long serialVersionUID = 8235425371807665823L;

    /**
     * Constructs an exception for Search Filter.
     *
     * @param reason Reason of exception
     */
    public SearchFilterException(final String message, final Exception reason) {
        super("SearchFilterException: " + message + " - " + reason.getMessage());
    }

    public SearchFilterException(final String message) {
        super("SearchFilterException: " + message);
    }

    /**
     * Constructs a no results exception.
     */
    public SearchFilterException() {
        super("No results.");
    }

}
