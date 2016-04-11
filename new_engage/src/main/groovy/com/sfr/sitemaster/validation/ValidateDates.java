/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Validator for dates. Can validate if provided dates are equal or one is before another and vice-versa.
 */
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidateDatesValidator.class)
public @interface ValidateDates {

    enum RULE {
        IS_BEFORE, IS_AFTER, NOT_AFTER, NOT_BEFORE, EQUAL, NOT_EQUAL
    }

    String message() default "Provided wrong dates.";

    String date();

    String compareTo();

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    RULE rule() default RULE.IS_BEFORE;


    @Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
    @Retention(RetentionPolicy.RUNTIME)
    @interface List {
        ValidateDates[] value() default {};
    }


}