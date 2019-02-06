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
package org.openhab.binding.mopidy.internal.handler;

import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.library.types.NextPreviousType;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.PlayPauseType;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.RefreshType;
import org.openhab.binding.mopidy.internal.MopidyBindingConstants;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetCurrentPlayingTrack;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetPlaybackStateMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetRepeatMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.GetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.NextMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PauseMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PlayMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.PreviousMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.RpcMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.SetMuteMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.SetRepeatMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.SetVolumeMessage;
import org.openhab.binding.mopidy.internal.server.message.rpc.StopMessage;

/**
 * The {@link CommandRequestMapper} is responsible for handling commands
 *
 * @author Daniel Bauer - Initial contribution
 */
public class CommandRequestMapper {

    public RpcMessage transform(ChannelUID channelUID, Command command) {
        if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYER)) {
            if (PlayPauseType.PLAY.equals(command)) {
                return new PlayMessage();
            } else if (PlayPauseType.PAUSE.equals(command)) {
                return new PauseMessage();
            } else if (NextPreviousType.NEXT.equals(command)) {
                return new NextMessage();
            } else if (NextPreviousType.PREVIOUS.equals(command)) {
                return new PreviousMessage();
            } else if (command instanceof RefreshType) {
                return new GetPlaybackStateMessage();
            }

        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME_MUTE)) {
            if (OnOffType.ON.equals(command)) {
                return new SetMuteMessage(true);
            } else if (OnOffType.OFF.equals(command)) {
                return new SetMuteMessage(false);
            } else if (command instanceof RefreshType) {
                return new GetMuteMessage();
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_VOLUME)) {
            if (command instanceof RefreshType) {
                return new GetVolumeMessage();
            } else if (command instanceof DecimalType) {
                DecimalType value = (DecimalType) command;
                return new SetVolumeMessage(value.intValue());
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_TRACK_NAME)) {
            if (command instanceof RefreshType) {
                return new GetCurrentPlayingTrack();
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_STOP)) {
            if (OnOffType.ON.equals(command)) {
                return new StopMessage();
            }
        } else if (channelUID.getId().equals(MopidyBindingConstants.CHANNEL_PLAYBACK_REPEAT)) {
            if (OnOffType.ON.equals(command)) {
                return new SetRepeatMessage(true);
            } else if (OnOffType.OFF.equals(command)) {
                return new SetRepeatMessage(false);
            } else if (command instanceof RefreshType) {
                return new GetRepeatMessage();
            }
        }
        return null;
    }
}
