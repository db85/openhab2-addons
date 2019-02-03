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
package org.openhab.binding.mopidy.internal.server.message.event;

import com.google.gson.annotations.SerializedName;

public class PlayerStateChangedEvent extends EventMessage {

    @SerializedName("old_state")
    private String oldState;
    @SerializedName("new_state")
    private String newState;

    public PlayerStateChangedEvent() {

    }

    public PlayerStateChangedEvent(String oldState, String newState) {
        super();
        this.oldState = oldState;
        this.newState = newState;
    }

    public String getOldState() {
        return oldState;
    }

    public String getNewState() {
        return newState;
    }
}
