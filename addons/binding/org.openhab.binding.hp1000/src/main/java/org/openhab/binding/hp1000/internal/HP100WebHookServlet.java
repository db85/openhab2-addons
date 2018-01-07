/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hp1000.internal;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.http.HttpService;
import org.osgi.service.http.NamespaceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main OSGi service and HTTP servlet for HP1000 Webhook.
 *
 * @author Daniel Bauer
 */
@Component(service = HttpServlet.class, configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true)
public class HP100WebHookServlet extends HttpServlet {

    private static final String PATH = "/hp1000";
    private final Logger logger = LoggerFactory.getLogger(HP100WebHookServlet.class);

    private HttpService httpService;

    /**
     * OSGi activation callback.
     *
     * @param config Service config.
     */
    @Activate
    protected void activate(Map<String, Object> config) {
        try {
            httpService.registerServlet(PATH, this, null, httpService.createDefaultHttpContext());
            logger.info("Started HP1000 Webhook servlet at {}", PATH);
        } catch (ServletException | NamespaceException exception) {
            logger.error("Could not start HP1000 Webhook servlet: {}", exception.getMessage(), exception);
        }
    }

    /**
     * OSGi deactivation callback.
     */
    @Deactivate
    protected void deactivate() {
        httpService.unregister(PATH);
        logger.info("HP1000 webhook servlet stopped");
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String requestStr = IOUtils.toString(request.getInputStream());
        logger.info("HP1000 webhook input: {}", requestStr);
        response.getWriter().write("");
    }

    @Reference
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }
}
