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
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Channel;
import org.eclipse.smarthome.core.thing.ChannelUID;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingStatus;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.eclipse.smarthome.core.types.State;
import org.eclipse.smarthome.core.types.UnDefType;
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

    private State buildChannelState(Channel channel, String value) {
        String acceptedItemType = channel.getAcceptedItemType();
        if (acceptedItemType == null) {
            return UnDefType.NULL;
        }
        switch (acceptedItemType) {
            case "Number":
                Double doubleValue = StateConverterUtils.parseDouble(value);
                return StateConverterUtils.toDecimalType(doubleValue);
            case "String":
                return StateConverterUtils.toStringType(value);
            case "DateTime":
                return StateConverterUtils.toDateTimeType(value);
            default:
                logger.error("accepted item type {} not handeled", channel.getAcceptedItemType());
        }
        return UnDefType.NULL;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        // not supported
    }

    @Override
    public void initialize() {
        updateStatus(ThingStatus.ONLINE);
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

    private void updateChannel(String channelId, String value) {
        Channel channel = getThing().getChannel(channelId);
        if (channel == null) {
            return;
        }

        updateState(channel.getUID(), buildChannelState(channel, value));
    }

    public void updateChannels(GardenConfiguration gardenConfiguration, @Nullable GardenLocation gardenLocation) {
        updatenChannels("general#", gardenConfiguration);
        SensorConfig sensorSonfig = gardenConfiguration.getSensor();
        if (sensorSonfig != null) {
            updatenChannels("general#", sensorSonfig);
        }
        if (gardenLocation != null) {
            updateGardenLocationChannels(gardenLocation);
        }

    }

    private void updateGardenLocationChannels(GardenLocation gardenLocation) {
        updatenChannels("general#", gardenLocation);
        Battery battery = gardenLocation.getBattery();
        if (battery != null) {
            GaugeValues batteryGaugeValues = battery.getGaugeValues();
            if (batteryGaugeValues != null) {
                updatenChannels("battery#", batteryGaugeValues);
            }
        }

        Sensor sensor = gardenLocation.getSensor();
        if (sensor != null) {
            updatenChannels("general#", sensor);
            Firmware firmware = sensor.getFirmwareUpdate();
            if (firmware != null) {
                updatenChannels("general#", firmware);
            }
        }

        Watering watering = gardenLocation.getWatering();
        if (watering != null) {
            updatenChannels("watering#", watering);
            updateAnalysisStatusChannel("watering#", watering.getSoilMoisture());
        }

        updateAnalysisStatusChannel("air_temperature#", gardenLocation.getAirTemperature());
        updateAnalysisStatusChannel("light#", gardenLocation.getLight());
        updateAnalysisStatusChannel("fertilizer#", gardenLocation.getFertilizer());
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