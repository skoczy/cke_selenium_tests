package com.sfr.sitemaster.enums

import com.fasterxml.jackson.annotation.JsonCreator

/**
 * Created by piotr on 22/10/15.
 */
enum Days {
    WEEKDAYS('weekdays'), SATURDAY('saturday'), SUNDAY('sunday');
    private final String text;

    Days(final String text) {
        this.text = text;
    }

    @JsonCreator
    public static Days fromValue(final String value) {
        return Days.valueOf(value.toUpperCase(Locale.ENGLISH));
    }

    @Override
    public String toString() {
        return text;
    }
}