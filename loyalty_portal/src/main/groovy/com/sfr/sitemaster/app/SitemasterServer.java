package com.sfr.sitemaster.app;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jetty9.InstrumentedHandler;
import com.codahale.metrics.jvm.ClassLoadingGaugeSet;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.servlets.MetricsServlet;
import com.codahale.metrics.servlets.ThreadDumpServlet;
import com.sfr.apicore.auth.AuthModule;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.injection.SFRInjector;
import com.sfr.apicore.injection.guice.GuiceSFRInjector;
import com.sfr.sitemaster.api.filters.AuthFilter;
import com.sfr.sitemaster.injection.guice.CoreServiceModule;
import com.sfr.sitemaster.injection.guice.DAOModule;
import com.sfr.sitemaster.injection.guice.DBModule;
import com.sfr.sitemaster.injection.guice.DomainServiceModule;
import com.sfr.sitemaster.injection.guice.MetricModule;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.SessionManager;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.server.session.HashSessionIdManager;
import org.eclipse.jetty.server.session.HashSessionManager;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.util.resource.Resource;
import org.glassfish.jersey.servlet.ServletContainer;
import org.glassfish.jersey.servlet.ServletProperties;

import javax.servlet.DispatcherType;
import java.util.EnumSet;

/**
 * Main class to boot the Sitemaster application with embedded Jetty.
 * <p>
 * Created by piotr on 17/09/15.
 */
public final class SitemasterServer { //NOPMD
    private static Server server;
    private static String port;

    public static void start(final SFRInjector injector, final String... args) {
        final CommandLine cmd = SitemasterArgs.get(args);
        final MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);
        try {
            port = cmd.getOptionValue(SitemasterArgs.PORT, "8080");
            server = new Server(Integer.parseInt(port));


            //Servlets
            final ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
            servletContextHandler.setContextPath(cmd.getOptionValue(SitemasterArgs.CONTEXT_PATH, "/"));
            servletContextHandler.setClassLoader(Thread.currentThread().getContextClassLoader());
            servletContextHandler.setWelcomeFiles(new String[]{"index.html"});
            final HashSessionIdManager hashSessionIdManager = new HashSessionIdManager();
            final SessionHandler sessionHandler = new SessionHandler();
            final SessionManager sessionManager = new HashSessionManager();
            sessionManager.setSessionIdManager(hashSessionIdManager);
            sessionHandler.setSessionManager(sessionManager);
            sessionHandler.setHandler(servletContextHandler);
            sessionHandler.setServer(server);
            server.setSessionIdManager(hashSessionIdManager);

            //Resources
            final ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setWelcomeFiles(new String[]{"index.html"});
            final String contentRootPath = cmd.getOptionValue(SitemasterArgs.CONTENT_ROOT);
            final Resource baseResource = contentRootPath == null ? Resource.newResource(SitemasterServer.class.getClassLoader().getResource("content/")) : Resource.newResource(contentRootPath, false);
            resourceHandler.setBaseResource(baseResource);

            //Jersey Servlet
            final ServletHolder jerseyServlet = servletContextHandler.addServlet(ServletContainer.class, "/api/v1/*");
            jerseyServlet.setInitOrder(0);
            jerseyServlet.setInitParameter(ServletProperties.JAXRS_APPLICATION_CLASS, SitemasterApp.class.getCanonicalName());

            //MetricServlet
            servletContextHandler.addServlet(MetricsServlet.class, "/metrics");
            servletContextHandler.setAttribute(MetricsServlet.METRICS_REGISTRY, metricRegistry);
            servletContextHandler.addServlet(ThreadDumpServlet.class, "/threads");
            //Filters
            if (!cmd.hasOption(SitemasterArgs.DISABLE_AUTH)) {
                servletContextHandler.addFilter(AuthFilter.class, "/api/v1/res/*", EnumSet.of(DispatcherType.REQUEST));
            }
            final FilterHolder filterHolder = servletContextHandler.addFilter(CrossOriginFilter.class, "/api/v1/*", EnumSet.of(DispatcherType.REQUEST));
            filterHolder.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
            filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "null");

            //InstrumentedHandler
            final InstrumentedHandler instrumentedServletHandler = new InstrumentedHandler(metricRegistry);
            instrumentedServletHandler.setHandler(servletContextHandler);

            final HandlerList handlers = new HandlerList();
            handlers.setHandlers(new Handler[]{resourceHandler, instrumentedServletHandler});
            server.setHandler(handlers);
            server.start();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void stop() {
        try {
            server.stop();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static SFRInjector initGuice() {
        final SitemasterPropertyService sitemasterPropertyService = new SitemasterPropertyService();
        final SFRInjector injector = new GuiceSFRInjector(new MetricModule(), new CoreServiceModule(sitemasterPropertyService), new AuthModule(), new DBModule(sitemasterPropertyService), new DAOModule(),
                new DomainServiceModule());
        InjectionService.registerServiceInjector(injector);
        return injector;
    }

    public static void setupMetrics(final SFRInjector injector) {
        final MetricRegistry metricRegistry = injector.getInstance(MetricRegistry.class);
        metricRegistry.registerAll(new MemoryUsageGaugeSet());
        metricRegistry.registerAll(new ClassLoadingGaugeSet());
        metricRegistry.registerAll(new GarbageCollectorMetricSet());
    }

    public static void main(final String... args) {
        final SFRInjector injector = initGuice();
        setupMetrics(injector);
        SitemasterServer.start(injector, args);
    }


}
