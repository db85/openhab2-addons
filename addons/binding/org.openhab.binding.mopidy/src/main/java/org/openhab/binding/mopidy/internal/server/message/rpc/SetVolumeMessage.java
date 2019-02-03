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

public class SetVolumeMessage extends RpcMessage {

    class VolumePara {
        @SerializedName("volume")
        private int volume;

        public VolumePara(int volume) {
            this.volume = volume;
        }
    }

    @SerializedName("params")
    private VolumePara params;

    public SetVolumeMessage(int volume) {
        params = new VolumePara(volume);
    }

    @SerializedName("method")
    private String method = "core.mixer.set_volume";

    public String getMethod() {
        return method;
    }
}
