package org.openhab.binding.mopidy.internal.server.message.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

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