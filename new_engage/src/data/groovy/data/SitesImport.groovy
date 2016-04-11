package data

import com.opencsv.CSVReader
import com.opencsv.CSVWriter
import com.sfr.sitemaster.entities.Site
import com.sfr.sitemaster.entities.OpeningInfo
import com.sfr.sitemaster.entities.OpeningTime
import com.sfr.sitemaster.entities.Service
import com.sfr.sitemaster.entities.Fuel
import com.sfr.sitemaster.enums.Days
import javax.validation.Validation
import javax.validation.Validator
import javax.validation.ValidatorFactory
import java.util.stream.Stream

/**
 * Created by piotr on 24/11/15.
 */
class SitesImport {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    Validator validator = factory.getValidator();
    TreeSet<Long> valid = new TreeSet<Long>()
    TreeSet<Long> invalid = new TreeSet<Long>()
    TreeSet<Long> duplicates = new TreeSet<Long>()

    public static void main(String[] args) {
        doImport()
    }

    static void doImport() {
        def instance = new SitesImport()
        instance.generateCsvs()
    }

    def readCsv = {
        file -> new CSVReader(new InputStreamReader(getClass().classLoader.getResourceAsStream(file)), ',' as char, '\"' as char).readAll().stream()
    }

    def writer = {
        file -> new CSVWriter(new File("src/data/resources/${file}").newWriter(), ',' as char, '\"' as char)
    }

    def getSites(Integer limit) {
        def services = []
        def fuels = []
        readCsv('dicts/siteservices.csv').skip(1).each {
            if (it[0] == 'SERVICE') {
                services << new Service(id: it[1] as Long, displayName: it[2], icon: it[3], lang: it[4], type: it[5])
            } else if (it[0] == 'FUEL') {
                fuels << new Fuel(id: it[1] as Long, displayName: it[2], icon: it[3], lang: it[4], type: it[5])
            }
        };
        Stream<String[]> stream = readCsv('osm/sites.csv').skip(1);
        if (limit != null) {
            stream = stream.limit(limit)
        }
        stream.collect {
            def StationID = it[0];
            def (AlwaysOpen, WorkingDays, Saturdays, String Sundays, XCoord, YCoord, Services, Fuels) = it[40..49];
            def site = new Site()
            site.with {
                id = StationID as Long
                xCoord = XCoord
                yCoord = YCoord
                openingInfo = new OpeningInfo()
                openingInfo.alwaysOpen = (AlwaysOpen?.trim() == '00.00-24.00')
            }
            if (!site.openingInfo.alwaysOpen) {
                site.openingInfo.openingTimes = [:]
                if (!WorkingDays.trim().empty) {
                    def (open, close) = toHours(WorkingDays);
                    site.openingInfo.openingTimes.put(Days.WEEKDAYS, new OpeningTime(days: Days.WEEKDAYS, open: open, close: close, site: site))
                }
                if (!Saturdays.trim().empty) {
                    def (open, close) = toHours(Saturdays);
                    site.openingInfo.openingTimes.put(Days.SATURDAY, new OpeningTime(days: Days.SATURDAY, open: open, close: close, site: site))
                }
                if (!Sundays.trim().empty) {
                    def (open, close) = toHours(Sundays);
                    site.openingInfo.openingTimes.put(Days.SUNDAY, new OpeningTime(days: Days.SUNDAY, open: open, close: close, site: site))
                }
            }
            site.services = []
            site.fuels = []
            Services.split(',').collect { it.trim() }.unique().each { s ->
                services.find { it.type.trim() == s.trim() }.each {
                    site.services << it
                }
            }
            Fuels.split(',').collect { it.trim() }.unique().each { s ->
                fuels.find { it.type.trim() == s.trim() }.each {
                    site.fuels << it
                }
            }
            site
        }.findAll { Site site ->
            !isDuplicated(site.id) && isValid(site)
        }
    }

    def isValid(Site site) {
        Set constraints = validator.validate(site)
        if (constraints.size() > 0) {
            invalid.add(site.id)
            false
        } else {
            valid.add(site.id)
            true
        }
    }

    def isDuplicated(Long id) {
        if (valid.contains(id)) {
            duplicates.add(id)
            true
        }
        false
    }

    def generateCsvs() {
        Integer index = 1
        def sitesWriter = writer('osm/generated_sites.csv')
        def openingTimesWriter = writer('osm/generated_openingtimes.csv')
        def servicesWriter = writer('osm/generated_services.csv')
        def fuelsWriter = writer('osm/generated_fuels.csv')
        sitesWriter.writeNext(['id', 'alwaysopen', '_version', 'xcoord', 'ycoord'] as String[], false)
        openingTimesWriter.writeNext(['id', 'site_id', 'days', 'close', 'open'] as String[], false)
        servicesWriter.writeNext(['site_id', 'service_id'] as String[], false)
        fuelsWriter.writeNext(['site_id', 'fuel_id'] as String[], false)
        getSites().each { Site site ->
            String[] line = [site.id, site.openingInfo.alwaysOpen, '0', site.xCoord ? site.xCoord : 'null', site.yCoord ? site.yCoord : 'null'] as String[]
            sitesWriter.writeNext(line, false)
            site.openingInfo.getOpeningTimes().each { k, v ->
                openingTimesWriter.writeNext(['nextval(\'hibernate_sequence\')', site.id, v.days.toString().toUpperCase(), v.close, v.open] as String[], false)
                index++
            }
            site.services.each { service ->
                servicesWriter.writeNext([site.id, service.id] as String[], false)
            }
            site.fuels.each { fuel ->
                fuelsWriter.writeNext([site.id, fuel.id] as String[], false)
            }
        }
        [sitesWriter, openingTimesWriter, servicesWriter, fuelsWriter].each { it.flush(); it.close() }
    }

    static def String[] toHours(String str) {
        return str.trim().replace('.', ':').split('-')
    }

}
