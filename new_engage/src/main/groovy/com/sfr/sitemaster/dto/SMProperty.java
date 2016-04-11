package com.sfr.sitemaster.dto;

import com.sfr.sitemaster.enums.Owner;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by piotr on 19/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SMProperty {

    Owner owner() default Owner.SITEMASTER;

    String path() default "";

    Class get() default Void.class;

    String label() default "NoLabel";

    Class target() default String.class;
}
