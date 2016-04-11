/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

/*
 * Copyright (c) 2016 Statoil Fuel & Retail ASA
 * All rights reserved.
 *
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */

package com.sfr.sitemaster.test.unit

import com.sfr.sitemaster.validation.ValidateDates
import spock.lang.Shared
import spock.lang.Specification

import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.time.LocalDate

/**
 * Created by piotr on 08/01/16.
 */
class DateValidatorSpecification extends Specification {
    @Shared
    Validator validator


    def setupSpec() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.IS_BEFORE)
    static class IsBeforeTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The first date should be BEFORE the second'() {
        when:
        def test = new IsBeforeTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now().plusDays(2)  | 0
        LocalDate.now() | LocalDate.now().minusDays(2) | 1
        LocalDate.now() | LocalDate.now()              | 1
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.IS_AFTER)
    static class IsAfterTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The first date should be AFTER the second'() {
        when:
        def test = new IsAfterTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now().plusDays(2)  | 1
        LocalDate.now() | LocalDate.now().minusDays(2) | 0
        LocalDate.now() | LocalDate.now()              | 1
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.NOT_AFTER)
    static class NotAfterTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The first date shouldn\'t be AFTER the second'() {
        when:
        def test = new NotAfterTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now().plusDays(2)  | 0
        LocalDate.now() | LocalDate.now().minusDays(2) | 1
        LocalDate.now() | LocalDate.now()              | 0
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.NOT_BEFORE)
    static class NotBeforeTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The first date shouldn\'t be BEFORE the second'() {
        when:
        def test = new NotBeforeTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now().plusDays(2)  | 1
        LocalDate.now() | LocalDate.now().minusDays(2) | 0
        LocalDate.now() | LocalDate.now()              | 0
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.EQUAL)
    static class EqualTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The dates should be EQUAL'() {
        when:
        def test = new EqualTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now()              | 0
        LocalDate.now() | LocalDate.now().plusDays(2)  | 1
        LocalDate.now() | LocalDate.now().minusDays(2) | 1
    }

    @ValidateDates(date = 'firstDate', compareTo = 'secondDate', rule = ValidateDates.RULE.NOT_EQUAL)
    static class NotEqualTest {
        LocalDate firstDate
        LocalDate secondDate
    }

    def 'The dated shouldn\'t be EQUAL'() {
        when:
        def test = new NotEqualTest(firstDate: firstDate, secondDate: secondDate)
        then:
        validator.validate(test).size() == errors
        where:
        firstDate       | secondDate                   | errors
        LocalDate.now() | LocalDate.now()              | 1
        LocalDate.now() | LocalDate.now().plusDays(2)  | 0
        LocalDate.now() | LocalDate.now().minusDays(2) | 0
    }
}