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
package org.openhab.binding.mopidy.internal.server.message.rpc;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link AddToTracklistMessage} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class AddToTracklistMessage extends RpcMessage {

    class AddToTracklistPara {
        @SerializedName("uris")
        private List<String> uris;

        public AddToTracklistPara(List<String> uris) {
            this.uris = uris;
        }
    }

    @SerializedName("params")
    private AddToTracklistPara params;

    public AddToTracklistMessage(List<String> uris) {
        params = new AddToTracklistPara(uris);
    }

    @SerializedName("method")
    private String method = "core.tracklist.add";

    public String getMethod() {
        return method;
    }
}
