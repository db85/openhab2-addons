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
 * The {@link TLTrack} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class TLTrack {
    @SerializedName("__model__")
    private String model;
    @SerializedName("tlid")
    private int tlid;
    @SerializedName("track")
    private Track track;

    public String getModel() {
        return model;
    }

    public int getTlid() {
        return tlid;
    }

    public Track getTrack() {
        return track;
    }
}
