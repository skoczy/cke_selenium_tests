/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;
import java.time.LocalDate;

/**
 * Created by piotr on 08/01/16.
 */
public class ValidateDatesValidator implements ConstraintValidator<ValidateDates, Object> {
    private String dateFieldName;
    private String compareToFieldName;
    private ValidateDates.RULE rule;

    @Override
    public void initialize(final ValidateDates constraintAnnotation) {
        dateFieldName = constraintAnnotation.date();
        compareToFieldName = constraintAnnotation.compareTo();
        rule = constraintAnnotation.rule();
    }

    @Override
    public boolean isValid(final Object value, final ConstraintValidatorContext context) {
        try {
            final LocalDate compareTo = getLocalDate(value, compareToFieldName);
            final LocalDate date = getLocalDate(value, dateFieldName);
            if (compareTo == null || date == null) {
                return false;
            }
            switch (rule) {
                case IS_BEFORE:
                    return isBefore(date, compareTo);
                case IS_AFTER:
                    return isAfter(date, compareTo);
                case NOT_BEFORE:
                    return notBefore(date, compareTo);
                case NOT_AFTER:
                    return notAfter(date, compareTo);
                case EQUAL:
                    return areEqual(date, compareTo);
                case NOT_EQUAL:
                    return areNotEqual(date, compareTo);
                default:
                    return false;
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    protected LocalDate getLocalDate(final Object object, final String field) throws NoSuchFieldException, IllegalAccessException {
        final Field _field = object.getClass().getDeclaredField(field);
        _field.setAccessible(true);
        if (_field.getType().equals(LocalDate.class)) {
            return (LocalDate) _field.get(object);
        }
        return null;
    }

    boolean isBefore(final LocalDate date, final LocalDate compareTo) {
        return date.isBefore(compareTo);
    }

    boolean isAfter(final LocalDate date, final LocalDate compareTo) {
        return date.isAfter(compareTo);
    }

    boolean notBefore(final LocalDate date, final LocalDate compareTo) {
        return !date.isBefore(compareTo);
    }

    boolean notAfter(final LocalDate date, final LocalDate compareTo) {
        return !date.isAfter(compareTo);
    }

    boolean areEqual(final LocalDate date, final LocalDate compareTo) {
        return date.isEqual(compareTo);
    }

    boolean areNotEqual(final LocalDate date, final LocalDate compareTo) {
        return !date.isEqual(compareTo);
    }

}
