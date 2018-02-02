/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.handler;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.parrotflower.internal.api.UserProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link UserProfileHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class UserProfileHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(UserProfileHandler.class);

    public UserProfileHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not supported
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);
    }

    private void updateChannel(String channelId, String value) {
        Channel channel = getThing().getChannel(channelId);
        if (channel == null) {
            return;
        }
        updateState(channel.getUID(), new StringType(value));
    }

    public void updateChannels(UserProfile userProfile) {
        logger.info("update user profile");
        Arrays.stream(userProfile.getClass().getDeclaredFields()).forEach(field -> {
            SerializedName serializedName = field.getDeclaredAnnotation(SerializedName.class);
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(userProfile);
                if (fieldValue != null) {
                    updateChannel(serializedName.value(), fieldValue.toString());
                }
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                logger.error("update channel failed", exception);
            }

        });
    }
}