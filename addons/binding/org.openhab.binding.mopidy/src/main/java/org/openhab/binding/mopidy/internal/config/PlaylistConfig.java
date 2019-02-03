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
 * Configuration settings for a {@link org.openhab.binding.mopidy.internal.handler.MopidyPlaylistHandler}.
 *
 * @author Daniel Bauer - Initial contribution
 */
public class PlaylistConfig {
    private String uri;

    /**
     * Get host used to connect to server.
     *
     * @return server host
     */
    public String getUri() {
        return uri;
    }

    /**
     * Set the host used to connect to server.
     *
     * @param port host port
     */
    public void setUri(String uri) {
        this.uri = uri;
    }
}
