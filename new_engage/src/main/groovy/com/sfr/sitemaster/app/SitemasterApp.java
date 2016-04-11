package com.sfr.sitemaster.app;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.jersey2.InstrumentedResourceMethodApplicationListener;
import com.sfr.apicore.api.AbstractSFRApplication;
import com.sfr.apicore.exceptions.GenericExceptionMapper;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.sitemaster.api.restricted.ServicesResource;
import com.sfr.sitemaster.api.restricted.ShmlogResource;
import com.sfr.sitemaster.api.restricted.SitePersonsResource;
import com.sfr.sitemaster.api.restricted.SiteResource;
import com.sfr.sitemaster.api.restricted.UserResource;
import com.sfr.sitemaster.api.unrestricted.LoginResource;
import com.sfr.sitemaster.api.unrestricted.LogoutResource;
import com.sfr.sitemaster.api.unrestricted.VersionResource;
import com.sfr.sitemaster.exceptions.mappers.EntityNotFoundExceptionMapper;
import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Info;
import org.glassfish.jersey.server.ServerProperties;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;

/**
 * Jersey Configuration class.
 * <p>
 * Created by piotr on 17/09/15.
 */
public final class SitemasterApp extends AbstractSFRApplication { //NOPMD

    public SitemasterApp() {
        super("com.sfr.sitemaster");
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        property(ServerProperties.MOXY_JSON_FEATURE_DISABLE, false);
        register(JacksonJaxbJsonProvider.class);
        register(new InstrumentedResourceMethodApplicationListener(InjectionService.getInstance(MetricRegistry.class)));
        register(SiteResource.class);
        register(SitePersonsResource.class);
        register(ServicesResource.class);
        register(UserResource.class);
        register(ShmlogResource.class);
        register(VersionResource.class);
        register(LoginResource.class);
        register(LogoutResource.class);
        register(GenericExceptionMapper.class);
        register(EntityNotFoundExceptionMapper.class);

        register(io.swagger.jaxrs.listing.ApiListingResource.class);
        register(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        final BeanConfig beanConfig = new BeanConfig();
        beanConfig.setHost("localhost:8080");
        beanConfig.setBasePath("/api/v1");
        beanConfig.setInfo(new Info());
        beanConfig.setTitle("Sitemaster API");
        beanConfig.setDescription("Sitemaster's REST API specification :) ");
        beanConfig.setVersion(AppProperties.get("app_version"));
        beanConfig.setContact("Avengers");
        beanConfig.setPrettyPrint(true);
        beanConfig.setResourcePackage("com.sfr.sitemaster");
        beanConfig.setScan(true);
    }
}
