/**
 * Copyright (c) 2014,2018 by the respective copyright holders.
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.parrotflower.internal;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.eclipse.smarthome.core.thing.type.ChannelTypeRegistry;
import org.openhab.binding.parrotflower.handler.ParrotFlowerBridgeHandler;
import org.openhab.binding.parrotflower.handler.SensorDeviceHandler;
import org.openhab.binding.parrotflower.handler.UserProfileHandler;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

/**
 * The {@link ParrotFlowerHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Daniel Bauer - Initial contribution
 */

@Component(service = { ThingHandlerFactory.class,
        ParrotFlowerHandlerFactory.class }, immediate = true, configurationPid = "binding.parrotflower")
@NonNullByDefault
public class ParrotFlowerHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Stream
            .of(THING_TYPE_BRIDGE, THING_TYPE_USER_PROFILE, THING_TYPE_SENSOR_DEVICE).collect(Collectors.toSet());

    private List<ParrotFlowerBridgeHandler> bridgeHandlerList = new ArrayList<>();
    @Nullable
    private ChannelTypeRegistry channelTypeRegistry;
    private List<SensorDeviceHandler> sensorDeviceHandlerList = new ArrayList<>();

    private List<UserProfileHandler> userProfileHandlerList = new ArrayList<>();

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(THING_TYPE_BRIDGE)) {
            ParrotFlowerBridgeHandler bridgeHandler = new ParrotFlowerBridgeHandler((Bridge) thing);
            bridgeHandlerList.add(bridgeHandler);
            return bridgeHandler;
        } else if (thingTypeUID.equals(THING_TYPE_USER_PROFILE)) {
            UserProfileHandler userProfileHandlern = new UserProfileHandler(thing);
            userProfileHandlerList.add(userProfileHandlern);
            return userProfileHandlern;
        } else if (thingTypeUID.equals(THING_TYPE_SENSOR_DEVICE) && channelTypeRegistry != null) {
            SensorDeviceHandler sensorDeviceHandler = new SensorDeviceHandler(channelTypeRegistry, thing);
            sensorDeviceHandlerList.add(sensorDeviceHandler);
            return sensorDeviceHandler;
        }
        return null;
    }

    public List<ParrotFlowerBridgeHandler> getBridgeHandlerList() {
        return bridgeHandlerList;
    }

    public List<SensorDeviceHandler> getSensorDeviceHandlerList() {
        return sensorDeviceHandlerList;
    }

    public List<UserProfileHandler> getUserProfileHandlerList() {
        return userProfileHandlerList;
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        if (thingHandler.getThing().getThingTypeUID().equals(THING_TYPE_BRIDGE)) {
            bridgeHandlerList.removeAll(bridgeHandlerList.stream()
                    .filter(handler -> handler.getThing().getUID().equals(thingHandler.getThing().getUID()))
                    .collect(Collectors.toList()));

        } else if (thingHandler.getThing().getThingTypeUID().equals(THING_TYPE_USER_PROFILE)) {
            userProfileHandlerList.removeAll(userProfileHandlerList.stream()
                    .filter(handler -> handler.getThing().getUID().equals(thingHandler.getThing().getUID()))
                    .collect(Collectors.toList()));

        } else if (thingHandler.getThing().getThingTypeUID().equals(THING_TYPE_SENSOR_DEVICE)) {
            sensorDeviceHandlerList.removeAll(sensorDeviceHandlerList.stream()
                    .filter(handler -> handler.getThing().getUID().equals(thingHandler.getThing().getUID()))
                    .collect(Collectors.toList()));

        }

        super.removeHandler(thingHandler);
    }

    @Reference(cardinality = ReferenceCardinality.OPTIONAL, policy = ReferencePolicy.DYNAMIC)
    protected void setChannelTypeRegistry(ChannelTypeRegistry channelTypeRegistry) {
        this.channelTypeRegistry = channelTypeRegistry;
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    protected void unsetChannelTypeRegistry(ChannelTypeRegistry channelTypeRegistry) {
        this.channelTypeRegistry = null;
    }

}
