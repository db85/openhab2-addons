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
