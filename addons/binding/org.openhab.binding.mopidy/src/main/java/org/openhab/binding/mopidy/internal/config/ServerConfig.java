/**
 * Copyright (c) 2010-2019 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.mopidy.internal.config;

/**
 * Configuration settings for a {@link org.openhab.binding.mopidy.internal.handler.MopidyServerHandler}.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class ServerConfig {
    private int port = 8060;
    private String hostname = "127.0.0.1";

    /**
     * Get port used to connect to server.
     *
     * @return server port
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port used to connect to server.
     *
     * @param port server port
     */
    public void setPort(int port) {
        this.port = port;
    }

    /**
     * Get host used to connect to server.
     *
     * @return server host
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Set the host used to connect to server.
     *
     * @param port host port
     */
    public void setHostname(String hostname) {
        this.hostname = hostname;
    }
}
