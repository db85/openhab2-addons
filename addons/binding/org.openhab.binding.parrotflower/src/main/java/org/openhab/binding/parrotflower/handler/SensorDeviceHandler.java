package org.openhab.binding.parrotflower.handler;

import java.util.Arrays;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.StringType;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.parrotflower.internal.api.GardenConfiguration;
import org.openhab.binding.parrotflower.internal.api.GardenLocationStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.annotations.SerializedName;

/**
 * The {@link SensorDeviceHandler} is responsible for handling commands, which are
 * sent to one of the channels.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class SensorDeviceHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(SensorDeviceHandler.class);

    public SensorDeviceHandler(Thing thing) {
        super(thing);
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not supported
    }

    private void updateChannel(String channelId, String value) {
        Channel channel = getThing().getChannel(channelId);
        if (channel == null) {
            return;
        }
        updateState(channel.getUID(), new StringType(value));
    }

    public void updateChannels(GardenConfiguration gardenConfiguration,
            @Nullable GardenLocationStatusResponse gardenLocationStatusResponse) {
        logger.info("update sensor device");
        updatenChannels(gardenConfiguration);
        if (gardenLocationStatusResponse != null) {
            updatenChannels(gardenLocationStatusResponse);
        }
    }

    private void updatenChannels(Object responseObject) {
        Arrays.stream(responseObject.getClass().getDeclaredFields()).forEach(field -> {
            SerializedName serializedName = field.getDeclaredAnnotation(SerializedName.class);
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(responseObject);
                if (fieldValue != null) {
                    updateChannel(serializedName.value(), fieldValue.toString());
                }
            } catch (IllegalArgumentException | IllegalAccessException exception) {
                logger.error("update channel failed", exception);
            }
        });
    }
}