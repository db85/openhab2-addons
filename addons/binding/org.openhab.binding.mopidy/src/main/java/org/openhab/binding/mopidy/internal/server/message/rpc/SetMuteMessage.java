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

import com.google.gson.annotations.SerializedName;

/**
 * The {@link SetMuteMessage} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class SetMuteMessage extends RpcMessage {

    class MutePara {
        @SerializedName("mute")
        private Boolean mute;

        public MutePara(Boolean mute) {
            this.mute = mute;
        }
    }

    @SerializedName("params")
    private MutePara params;

    public SetMuteMessage(Boolean mute) {
        params = new MutePara(mute);
    }

    @SerializedName("method")
    private String method = "core.mixer.set_mute";

    public String getMethod() {
        return method;
    }
}
