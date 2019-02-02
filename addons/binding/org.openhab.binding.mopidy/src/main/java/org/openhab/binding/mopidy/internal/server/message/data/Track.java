package org.openhab.binding.mopidy.internal.server.message.data;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Track {
    @SerializedName("__model__")
    private String model;
    @SerializedName("name")
    private String name;
    @SerializedName("disc_no")
    private int discNo;
    @SerializedName("uri")
    private String uri;
    @SerializedName("comment")
    private String comment;
    @SerializedName("date")
    private String date;
    @SerializedName("length")
    private long length;
    @SerializedName("last_modified")
    private long lastModified;
    @SerializedName("track_no")
    private int trackNo;
    @SerializedName("genre")
    private String genre;
    @SerializedName("album")
    private Album album;
    @SerializedName("composers")
    private List<Composer> composers;
    @SerializedName("artists")
    private List<Artist> artists;

    public String getModel() {
        return model;
    }

    public String getName() {
        return name;
    }

    public int getDiscNo() {
        return discNo;
    }

    public String getUri() {
        return uri;
    }

    public String getComment() {
        return comment;
    }

    public String getDate() {
        return date;
    }

    public long getLength() {
        return length;
    }

    public long getLastModified() {
        return lastModified;
    }

    public int getTrackNo() {
        return trackNo;
    }

    public String getGenre() {
        return genre;
    }

    public Album getAlbum() {
        return album;
    }

    public List<Composer> getComposers() {
        return composers;
    }

    public List<Artist> getArtists() {
        return artists;
    }
}
