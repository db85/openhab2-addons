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

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link Album} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class Album {
    @SerializedName("__model__")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("num_tracks")
    private int numTracks;
    @SerializedName("uri")
    private String uri;
    @SerializedName("date")
    private String date;
    @SerializedName("num_discs")
    private int numDiscs;
    @SerializedName("artists")
    private List<Artist> artists;

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public int getNumTracks() {
        return numTracks;
    }

    public String getUri() {
        return uri;
    }

    public String getDate() {
        return date;
    }

    public int getNumDiscs() {
        return numDiscs;
    }

    public List<Artist> getArtists() {
        return artists;
    }
}