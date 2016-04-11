package com.sfr.sitemaster.exceptions;

/**
 * Exception for errors while mapping entities.
 *
 * Created by piotr on 13.08.15.
 */
public class EntityMappingException extends Exception {

    private static final long serialVersionUID = 8235425371807665823L;

    public EntityMappingException(final String message, final Exception reason) {
        super("Error while mapping different entities:" + message + " - " + reason.getMessage());
    }

    public EntityMappingException(final String message) {
        super("Error while mapping different entities:" + message);
    }

    public EntityMappingException() {
        super("Error while mapping different entities.");
    }

}
