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
 * The {@link SetRepeatMessage} is a json model
 *
 * @author Daniel Bauer - Initial contribution
 */
public class SetRepeatMessage extends RpcMessage {

    class RepeatPara {
        @SerializedName("value")
        private Boolean value;

        public RepeatPara(Boolean value) {
            this.value = value;
        }
    }

    @SerializedName("params")
    private RepeatPara params;

    public SetRepeatMessage(Boolean value) {
        params = new RepeatPara(value);
    }

    @SerializedName("method")
    private String method = "core.tracklist.set_repeat";

    public String getMethod() {
        return method;
    }
}
