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
import org.openhab.binding.parrotflower.handler.UserProfileHandler;
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
            .of(THING_TYPE_BRIDGE, THING_TYPE_USER_PROFILE).collect(Collectors.toSet());

    private List<ParrotFlowerBridgeHandler> bridgeList = new ArrayList<>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();
        if (thingTypeUID.equals(THING_TYPE_BRIDGE)) {
            ParrotFlowerBridgeHandler bridge = new ParrotFlowerBridgeHandler((Bridge) thing);
            bridgeList.add(bridge);
            return bridge;
        } else if (thingTypeUID.equals(THING_TYPE_USER_PROFILE)) {
            return new UserProfileHandler(thing);
        }
        return null;
    }

    public List<ParrotFlowerBridgeHandler> getBridgeList() {
        return bridgeList;
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        bridgeList.removeAll(bridgeList.stream()
                .filter(handler -> handler.getThing().getUID().equals(thingHandler.getThing().getUID()))
                .collect(Collectors.toList()));
        super.removeHandler(thingHandler);
    }
}
