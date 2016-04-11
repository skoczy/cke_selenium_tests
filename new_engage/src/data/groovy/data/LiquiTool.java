package data;

import com.sfr.sitemaster.app.SitemasterPropertyService;
import liquibase.exception.CommandLineParsingException;
import liquibase.exception.LiquibaseException;
import liquibase.integration.commandline.Main;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.utils.FindbugsSuppressWarnings;

import java.io.*;

/**
 * Created by piotr on 27/11/15.
 */
@FindbugsSuppressWarnings("NP_GUARANTEED_DEREF_ON_EXCEPTION_PATH")
public final class LiquiTool {
    final Log log = LogFactory.getLog(getClass());
    SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();

    private LiquiTool() {
        sitemasterPropertyService = new SitemasterPropertyService();
    }

    private void runCmd(final String msg, final String... cmd) {
        log.warn(msg);
        final String[] args = {"--changeLogFile=src/data/resources/main.xml",
                "--url=" + sitemasterPropertyService.getProperty("hibernate.connection.url"),
                "--username=" + sitemasterPropertyService.getProperty("hibernate.connection.username"),
                "--password=" + sitemasterPropertyService.getProperty("hibernate.connection.password")};
        try {
            Main.run(ArrayUtils.addAll(args, cmd));
        } catch (CommandLineParsingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LiquibaseException e) {
            e.printStackTrace();
        }
    }


    public static void tag(final String tag) {
        new LiquiTool().runCmd("Tagging db: ${tag}", "tag", tag);
    }

    public static void drop() {
        new LiquiTool().runCmd("Dropping db", "dropAll");
    }

    public static void update() {
        new LiquiTool().runCmd("Updating db", "update");
    }

    public static void reset() {
        drop();
        update();
    }

    public static void main(final String... args) {
        new LiquiTool().runCmd("[LiquiTool] running cmd: " + args[0], args[0]);
    }
}
