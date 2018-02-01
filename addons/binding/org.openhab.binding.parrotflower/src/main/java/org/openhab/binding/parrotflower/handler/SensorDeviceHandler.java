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
import org.openhab.binding.parrotflower.internal.api.AnalysisStatus;
import org.openhab.binding.parrotflower.internal.api.Battery;
import org.openhab.binding.parrotflower.internal.api.Firmware;
import org.openhab.binding.parrotflower.internal.api.GardenConfiguration;
import org.openhab.binding.parrotflower.internal.api.GardenLocation;
import org.openhab.binding.parrotflower.internal.api.GaugeValues;
import org.openhab.binding.parrotflower.internal.api.Sensor;
import org.openhab.binding.parrotflower.internal.api.SensorConfig;
import org.openhab.binding.parrotflower.internal.api.Watering;
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

    public void updateChannels(GardenConfiguration gardenConfiguration, @Nullable GardenLocation gardenLocation) {
        logger.info("update sensor device");
        updatenChannels(gardenConfiguration);
        SensorConfig sensorSonfig = gardenConfiguration.getSensor();
        if (sensorSonfig != null) {
            updatenChannels(sensorSonfig);
        }
        if (gardenLocation != null) {
            updateGardenLocationChannels(gardenLocation);
        }

    }

    private void updateGardenLocationChannels(GardenLocation gardenLocation) {
        updatenChannels(gardenLocation);
        Battery battery = gardenLocation.getBattery();
        if (battery != null) {
            GaugeValues batteryGaugeValues = battery.getGaugeValues();
            if (batteryGaugeValues != null) {
                updatenChannels("battery_", batteryGaugeValues);
            }
        }

        Sensor sensor = gardenLocation.getSensor();
        if (sensor != null) {
            updatenChannels(sensor);
            Firmware firmware = sensor.getFirmwareUpdate();
            if (firmware != null) {
                updatenChannels(firmware);
            }
        }

        Watering watering = gardenLocation.getWatering();
        if (watering != null) {
            updatenChannels("watering_", watering);
            updateAnalysisStatusChannel("watering_", watering.getSoilMoisture());
        }

        updateAnalysisStatusChannel("air_temperature_", gardenLocation.getAirTemperature());
        updateAnalysisStatusChannel("light_", gardenLocation.getLight());
        updateAnalysisStatusChannel("fertilizer_", gardenLocation.getFertilizer());
    }

    private void updateAnalysisStatusChannel(String channelPrefix, @Nullable AnalysisStatus analysisStatus) {
        if (analysisStatus == null) {
            return;
        }
        updatenChannels(channelPrefix, analysisStatus);
        GaugeValues gaugeValues = analysisStatus.getGaugeValues();
        if (gaugeValues != null) {
            updatenChannels(channelPrefix, gaugeValues);
        }

    }

    private void updatenChannels(Object responseObject) {
        updatenChannels("", responseObject);
    }

    private void updatenChannels(String channelPrefix, Object responseObject) {
        Arrays.stream(responseObject.getClass().getDeclaredFields()).forEach(field -> {
            SerializedName serializedName = field.getDeclaredAnnotation(SerializedName.class);
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(responseObject);
                updateChannel(channelPrefix + serializedName.value(), fieldValue == null ? "" : fieldValue.toString());

            } catch (IllegalArgumentException | IllegalAccessException exception) {
                logger.error("update channel failed", exception);
            }
        });
    }
}