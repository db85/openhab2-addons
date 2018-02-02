/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal.api;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link Picture} class is mapping the json api model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class Picture {
    @SerializedName("expires")
    private String expires;
    @SerializedName("image_identifier")
    private String image_identifier;
    @SerializedName("location_identifier")
    private String location_identifier;
    @SerializedName("url")
    private String url;

    public String getExpires() {
        return expires;
    }

    public String getImage_identifier() {
        return image_identifier;
    }

    public String getLocation_identifier() {
        return location_identifier;
    }

    public String getUrl() {
        return url;
    }

    public void setExpires(String expires) {
        this.expires = expires;
    }

    public void setImage_identifier(String image_identifier) {
        this.image_identifier = image_identifier;
    }

    public void setLocation_identifier(String location_identifier) {
        this.location_identifier = location_identifier;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}