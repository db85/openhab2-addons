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
package org.openhab.binding.mopidy.internal.server.message.data;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link Artist} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class Artist {

    @SerializedName("__model__")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("uri")
    private String uri;

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }
}
