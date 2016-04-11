package com.sfr.sitemaster.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.sfr.sitemaster.enums.Days
import com.sfr.sitemaster.enums.Owner
import com.sfr.sitemaster.validation.ValidateDates

import javax.validation.Valid
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern
import java.time.LocalDate

/**
 * Created by piotr on 20/10/15.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
class OpeningInfoDTO {
    @SMProperty(owner = Owner.SITEMASTER, path = 'alwaysOpen', target = Boolean.class, label = 'Always open')
    Boolean alwaysOpen

    @SMProperty(owner = Owner.SITEMASTER, path = 'openingTimes', target = Map.class, label = 'Opening times')
    @Valid
    Map<Days, OpeningInfoDTO.OpeningTimeDTO> openingTimes = [:]

    @SMProperty(owner = Owner.SITEMASTER, path = 'temporarilyClosed', target = List.class, label = 'Temporarily closed')
    @Valid
    List<OpeningInfoDTO.TemporarilyClosedDTO> temporarilyClosed = []
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class OpeningTimeDTO {
        @NotNull
        @Pattern(regexp = '([01]?[0-9]|2[0-3]):[0-5][0-9]')
        @SMProperty(owner = Owner.SITEMASTER, path = 'open', label = 'Open')
        String open

        @NotNull
        @Pattern(regexp = '([01]?[0-9]|2[0-3]):[0-5][0-9]')
        @SMProperty(owner = Owner.SITEMASTER, path = 'close', label = 'Close')
        String close

        public boolean equals(final Object obj) {
            if (obj instanceof OpeningTimeDTO) {
                return ((OpeningTimeDTO) obj).open.equals(this.open) && ((OpeningTimeDTO) obj).close.equals(this.close);
            }
            return false;
        }

        public int hashCode() {
            return (open.hashCode() * close.hashCode()) / 2;
        }
    }

    @ValidateDates(date = 'from', compareTo = 'to', rule = ValidateDates.RULE.NOT_AFTER)
    @JsonIgnoreProperties(ignoreUnknown = true)
    static class TemporarilyClosedDTO {
        @NotNull
        @SMProperty(owner = Owner.SITEMASTER, path = 'from', label = 'From', target = LocalDate)
        LocalDate from

        @NotNull
        @SMProperty(owner = Owner.SITEMASTER, path = 'to', label = 'To', target = LocalDate)
        LocalDate to

        public boolean equals(final Object obj) {
            if (obj instanceof TemporarilyClosedDTO) {
                return ((TemporarilyClosedDTO) obj).from.isEqual(this.from) && ((TemporarilyClosedDTO) obj).to.isEqual(this.to);
            }
            return false;
        }

        public int hashCode() {
            if (from == null || to == null) {
                return super.hashCode()
            }
            return (from.hashCode() * to.hashCode()) / 2;
        }
    }
}
