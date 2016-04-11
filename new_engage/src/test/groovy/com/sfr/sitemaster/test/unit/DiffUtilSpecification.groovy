package com.sfr.sitemaster.test.unit

import com.sfr.sitemaster.commons.DiffUtil
import com.sfr.sitemaster.dto.OpeningInfoDTO
import com.sfr.sitemaster.entities.OpeningTime
import com.sfr.sitemaster.entities.Site
import com.sfr.sitemaster.enums.Days
import com.sfr.sitemaster.shared.SiteHelper
import spock.lang.Specification
import spock.lang.Unroll

import java.time.LocalDate

/**
 * Created by piotr on 26/10/15.
 */
class DiffUtilSpecification extends Specification {

    @Unroll('should find #changesCount difference(s) in coordinates when #_case')
    def 'should find #changesCount difference(s) in coordinates when #_case'() {
        when:
        final Site siteBefore = SiteHelper.createEntity(5)
        final Site siteAfter = SiteHelper.createEntity(5)
        siteBefore.with {
            xCoord = xBefore
            yCoord = yBefore
        }
        siteAfter.with {
            xCoord = xAfter
            yCoord = yAfter
        }
        def changes = DiffUtil.compare(siteBefore, siteAfter)
        then:
        changes.size() == changesCount
        if (changesCount > 0) {
            changes.find { it.path.join('/') == 'xCoord' }.before == xBefore
            changes.find { it.path.join('/') == 'xCoord' }.after == xAfter
        } else if (changesCount > 1) {
            changes.find { it.path.join('/') == 'yCoord' }.before == yBefore
            changes.find { it.path.join('/') == 'yCoord' }.after == yAfter
        }

        where:
        _case              | xBefore  | xAfter   | yBefore | yAfter  | changesCount
        'no changes '      | '1.0453' | '1.0453' | '3.123' | '3.123' | 0
        'only one update ' | '1.0453' | '1.2253' | '3.123' | '3.123' | 1
        'new entries'      | null     | '1.453'  | null    | '5.123' | 2
        'updated entries'  | '1.0453' | '1.4543' | '3.123' | '3.321' | 2
        'removed entries'  | '1.0453' | null     | '3.123' | null    | 2
    }

    def 'should find a difference in opening info'() {
        when:
        final Site siteBefore = SiteHelper.createEntity(10)
        siteBefore.with {
            openingInfo.with {
                alwaysOpen = true
                openingTimes = [:]
                openingTimes.put(Days.WEEKDAYS, new OpeningTime(open: '08:00', close: '16:00'))
            }
        }
        final Site siteAfter = SiteHelper.createEntity(10)
        siteAfter.with {
            openingInfo.with {
                alwaysOpen = true
                openingTimes = [:]
                openingTimes.put(Days.WEEKDAYS, new OpeningTime(open: '14:00', close: '16:00'))
            }
        }
        def changes = DiffUtil.compare(siteBefore, siteAfter)
        then:
        changes.size() == 1
        changes.find { it.path.join('/') == 'openingInfo/openingTimes/{weekdays}/open' }.before == '08:00'
        changes.find { it.path.join('/') == 'openingInfo/openingTimes/{weekdays}/open' }.after == '14:00'
    }

    def 'should find a difference in temporarily closed'() {
        when:
        final Site siteBefore = SiteHelper.createEntity(10)
        final Site siteAfter = SiteHelper.createEntity(10)
        siteAfter.openingInfo.temporarilyClosed = []
        siteAfter.openingInfo.temporarilyClosed << new OpeningInfoDTO.TemporarilyClosedDTO(from: LocalDate.now().plusMonths(1), to: LocalDate.now().plusMonths(2))
        def changes = DiffUtil.compare(siteBefore, siteAfter)
        then:
        changes.size() == 2
        changes.find { it.path.join('/') == 'openingInfo/temporarilyClosed/{0}/from' }?.before == null
        changes.find { it.path.join('/') == 'openingInfo/temporarilyClosed/{0}/to' }?.before == null
        changes.find { it.path.join('/') == 'openingInfo/temporarilyClosed/{0}/from' }.after == LocalDate.now().plusMonths(1).toString()
        changes.find { it.path.join('/') == 'openingInfo/temporarilyClosed/{0}/to' }.after == LocalDate.now().plusMonths(2).toString()
    }

}