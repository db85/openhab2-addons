/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.hp1000.internal;

import static org.openhab.binding.hp1000.HP1000BindingConstants.*;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
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
public class HP1000WebHookServlet extends HttpServlet {

    private final Logger logger = LoggerFactory.getLogger(HP1000WebHookServlet.class);

    private HttpService httpService;
    private HP1000HandlerFactory handlerFactory;

    /**
     * OSGi activation callback.
     *
     * @param config Service config.
     */
    @Activate
    protected void activate(Map<String, Object> config) {
        try {
            httpService.registerServlet(SERVLET_BINDING_ALIAS, this, null, httpService.createDefaultHttpContext());
            logger.info("Started HP1000 Webhook servlet at {}", SERVLET_BINDING_ALIAS);
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

    // GET /weatherstation/updateweatherstation.php?
    // ID=wetter&
    // PASSWORD=wetter&
    // tempf=41.9&
    // humidity=99&
    // dewptf=41.7&
    // windchillf=41.9&
    // winddir=182&
    // windspeedmph=0.00&
    // windgustmph=0.00&
    // rainin=0.00&
    // dailyrainin=0.00&
    // weeklyrainin=1.57&
    // monthlyrainin=1.64&
    // yearlyrainin=1.64&
    // solarradiation=67.98&
    // UV=1
    // &indoortempf=64.4&
    // indoorhumidity=51&
    // baromin=29.03&
    // lowbatt=2&
    // dateutc=2018-1-8%2010:49:5&
    // softwaretype=Weather%20logger%20V2.2.2&
    // action=updateraw&
    // realtime=1&rtfreq=5
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.getWriter().write("");

        String path = request.getPathInfo();
        if (WEBHOOL_PATH.equalsIgnoreCase(path)) {
            handlerFactory.webHookEvent(request.getRemoteHost());
        }
    }

    @Reference
    public void setHttpService(HttpService httpService) {
        this.httpService = httpService;
    }

    public void unsetHttpService(HttpService httpService) {
        this.httpService = null;
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    public void setHP1000HandlerFactory(HP1000HandlerFactory handlerFactory) {
        this.handlerFactory = handlerFactory;
    }

    public void unsetHP1000HandlerFactory(HP1000HandlerFactory handlerFactory) {
        this.handlerFactory = null;
    }
}
