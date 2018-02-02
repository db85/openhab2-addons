/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.config.discovery.DiscoveryService;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.parrotflower.handler.ParrotFlowerBridgeHandler;
import org.openhab.binding.parrotflower.handler.SensorDeviceHandler;
import org.openhab.binding.parrotflower.handler.UserProfileHandler;
import org.openhab.binding.parrotflower.internal.discovery.ParrotFlowerDiscoveryService;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Component;

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

    private final Map<ThingUID, ServiceRegistration<?>> discoveryServiceList = new HashMap<>();

    private List<SensorDeviceHandler> sensorDeviceHandlerList = new ArrayList<>();
    private List<UserProfileHandler> userProfileHandlerList = new ArrayList<>();

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(THING_TYPE_BRIDGE)) {
            ParrotFlowerBridgeHandler bridgeHandler = new ParrotFlowerBridgeHandler(this, (Bridge) thing);
            registerDiscoveryService(bridgeHandler);
            return bridgeHandler;
        } else if (thingTypeUID.equals(THING_TYPE_USER_PROFILE)) {
            UserProfileHandler userProfileHandlern = new UserProfileHandler(thing);
            userProfileHandlerList.add(userProfileHandlern);
            return userProfileHandlern;
        } else if (thingTypeUID.equals(THING_TYPE_SENSOR_DEVICE)) {
            SensorDeviceHandler sensorDeviceHandler = new SensorDeviceHandler(thing);
            sensorDeviceHandlerList.add(sensorDeviceHandler);
            return sensorDeviceHandler;
        }
        return null;
    }

    public List<SensorDeviceHandler> getSensorDeviceHandlerList() {
        return sensorDeviceHandlerList;
    }

    public List<UserProfileHandler> getUserProfileHandlerList() {
        return userProfileHandlerList;
    }

    private void registerDiscoveryService(ParrotFlowerBridgeHandler bridgeHandler) {
        ParrotFlowerDiscoveryService discoveryService = new ParrotFlowerDiscoveryService(bridgeHandler);
        this.discoveryServiceList.put(bridgeHandler.getThing().getUID(), getBundleContext()
                .registerService(DiscoveryService.class.getName(), discoveryService, new Hashtable<String, Object>()));
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        if (thingHandler.getThing().getThingTypeUID().equals(THING_TYPE_BRIDGE)) {
            unregisterDiscoveryService((ParrotFlowerBridgeHandler) thingHandler);

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

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    private void unregisterDiscoveryService(ParrotFlowerBridgeHandler bridgeHandler) {
        ServiceRegistration<?> serviceReg = discoveryServiceList.get(bridgeHandler.getThing().getUID());

        ParrotFlowerDiscoveryService service = (ParrotFlowerDiscoveryService) getBundleContext()
                .getService(serviceReg.getReference());
        if (service != null) {
            service.deactivate();
        }
        serviceReg.unregister();
        discoveryServiceList.remove(bridgeHandler.getThing().getUID());
    }

}
