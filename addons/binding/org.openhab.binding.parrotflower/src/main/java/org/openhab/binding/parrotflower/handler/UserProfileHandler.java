package org.openhab.binding.parrotflower.handler;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;

/**
 * The {@link UserProfileHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class UserProfileHandler extends BaseThingHandler {

    public UserProfileHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not supported
    }

}