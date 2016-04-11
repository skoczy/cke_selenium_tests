package com.sfr.sitemaster.exceptions;


import com.sfr.apicore.dao.SFREntityObject;

/**
 * Created by piotr on 13.08.15.
 */
public class NoResultsException extends Exception {

    private static final long serialVersionUID = 8235425371807665823L;

    /**
     * Constructs a no results exception for a given entity.
     *
     * @param clazz Class which extends {@link SFREntityObject}
     */
    public NoResultsException(final Class<? extends SFREntityObject<?>> clazz) {
        super("No results for entity: " + clazz.getSimpleName());
    }

    /**
     * Constructs a no results exception.
     */
    public NoResultsException() {
        super("No results.");
    }

}
