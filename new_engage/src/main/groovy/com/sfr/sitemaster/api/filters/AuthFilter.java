/**
 * Copyright (c) 2014 Statoil Fuel & Retail ASA
 * All rights reserved.
 * <p>
 * This code is proprietary and the property of Statoil Fuel & Retail ASA. It may not be
 * distributed without written permission from Statoil Fuel & Retail ASA.
 */
package com.sfr.sitemaster.api.filters;

import com.google.inject.Inject;
import com.sfr.apicore.api.filters.patterns.ChainedFilter;
import com.sfr.apicore.auth.exceptions.ExternalAuthenticationFailureException;
import com.sfr.apicore.injection.InjectionService;
import com.sfr.apicore.exceptions.pojo.DBException;
import com.sfr.sitemaster.domainservices.PersistentSessionService;
import com.sfr.sitemaster.domainservices.UserService;
import com.sfr.sitemaster.entities.PersistentSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.glassfish.jersey.internal.util.Base64;

import javax.security.auth.login.LoginException;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.MalformedInputException;


/**
 * BasicAuthentication filter.
 *
 * @author yves
 */
public class AuthFilter extends ChainedFilter {
    private static final Logger LOGGER = Logger.getLogger(AuthFilter.class);
    public static final String USERNAME_PARAM = "username";
    public static final String PASSWD_PARAM = "password";
    // private static final String EMAIL = "foobar@statoilfuelretail.com";
    // hack for NCR, seems like they cant handle emails with @ char
    // y.h. 16.07.2015

    @Inject
    private UserService userService;

    @Inject
    private PersistentSessionService persistentSessionService;

    @Override
    public void init(final FilterConfig config) throws ServletException {
        InjectionService.getInjector().injectMembers(this);
    }

    @Override
    public boolean filterIncomingRequest(final ServletRequest request,
                                         final ServletResponse response) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) request;
        final HttpServletResponse httpResponse = (HttpServletResponse) response;
        final StringBuilder errorMsg = new StringBuilder(30);
        errorMsg.append("Authentication failed.");
        try {
            final String sessionId = getSessionIdFromRequest(httpRequest);

            if (sessionId == null) {
                // no session, fallback to basic auth
                if (authoriseBasicAuth(httpRequest) != null) {
                    // no need to create the session.
                    return true;
                }
            } else {
                // client claims to have a session
                final PersistentSession persistentSession = persistentSessionService
                        .findSessionFromKey(sessionId);

                if (persistentSession == null) {
                    // no session found, fallback on basic authentication.
                    // uses basic authentication
                    final com.sfr.apicore.auth.User authenticatedUser = authoriseBasicAuth(httpRequest);
                    if (authenticatedUser != null) {
                        // basic auth success - creating session
                        createAndPersistNewSession(httpRequest, authenticatedUser);
                        return true;
                    }
                } else {
                    request.setAttribute("persistentSession", persistentSession);
                    // the client's claim is true
                    return true;
                }
            }
            // httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"" +
            // realm + "\"");
        } catch (Exception e) {
            errorMsg.append(e.getMessage());
        }
        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, errorMsg.toString());
        return false;
    }

    private void createAndPersistNewSession(final HttpServletRequest httpRequest,
                                            final com.sfr.apicore.auth.User authenticatedUser) throws DBException {
        final HttpSession newSession = httpRequest.getSession(true);
        persistentSessionService.createNewPersistenSession(authenticatedUser, newSession.getId());
    }

    private String getSessionIdFromRequest(final HttpServletRequest request) {
        final Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (final Cookie cookie : cookies) {
                if (cookie.getName().equals("JSESSIONID")) {
                    return cookie.getValue();
                }
            }
        }

        return null;
    }

    private com.sfr.apicore.auth.User authoriseBasicAuth(final HttpServletRequest httpRequest) throws MalformedInputException,
            UnsupportedEncodingException, LoginException, DBException, ExternalAuthenticationFailureException {
        final String authorizationHeaderValue = StringUtils.trimToEmpty(httpRequest
                .getHeader("Authorization"));
        if (StringUtils.isBlank(authorizationHeaderValue)) {
            LOGGER.debug("No Authorization header value provided");
            return null;
        }
        if (!authorizationHeaderValue.startsWith("Basic")) {
            LOGGER.debug("Unsupported authorization scheme. Enage supports Basic authentication");
            return null;
        }

        final int index = authorizationHeaderValue.indexOf(' ');
        if (index <= 0) {
            throw new MalformedInputException(index);
        }

        final String encodedCredential = authorizationHeaderValue.substring(index + 1);
        final String decodedCredential = new String(Base64.decode(encodedCredential
                .getBytes("UTF-8")), "UTF-8");

        // Basic authentication does not allow ':' in username, so we are safe
        // in the following line.
        final String[] credentialParts = decodedCredential.split(":", 2);
        if (credentialParts == null || credentialParts.length != 2 || "".equals(credentialParts[0])
                || "".equals(credentialParts[1])) {
            LOGGER.debug("Malformed Authorization header value.");
            return null;
        }

        return userService.login(credentialParts[0], credentialParts[1]);
    }

    @Override
    public void filterOutgoingResponse(final ServletRequest request, final ServletResponse response)
            throws IOException, ServletException {
        // not needed.
    }

    @Override
    public void destroy() {
        // not needed
    }
}
