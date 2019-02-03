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
package org.openhab.binding.mopidy.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.thing.Bridge;
import org.eclipse.smarthome.core.thing.Thing;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandler;
import org.eclipse.smarthome.core.thing.binding.BaseThingHandlerFactory;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.thing.binding.ThingHandlerFactory;
import org.openhab.binding.mopidy.internal.handler.MopidyPlaylistHandler;
import org.openhab.binding.mopidy.internal.handler.MopidyServerHandler;
import org.osgi.service.component.annotations.Component;

/**
 * The {@link MopidyHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.mopidy", service = ThingHandlerFactory.class)
public class MopidyHandlerFactory extends BaseThingHandlerFactory {

    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Collections.unmodifiableSet(
            Stream.of(MopidyBindingConstants.THING_TYPE_SERVER, MopidyBindingConstants.THING_TYPE_PLAYLIST)
                    .collect(Collectors.toSet()));

    private static List<BaseThingHandler> things = new ArrayList<BaseThingHandler>();

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (thingTypeUID.equals(MopidyBindingConstants.THING_TYPE_SERVER)) {
            MopidyServerHandler serverHandler = new MopidyServerHandler((Bridge) thing);
            things.add(serverHandler);
            return serverHandler;
        } else if (thingTypeUID.equals(MopidyBindingConstants.THING_TYPE_PLAYLIST)) {
            MopidyPlaylistHandler playlistHandler = new MopidyPlaylistHandler(thing);
            things.add(playlistHandler);
            return playlistHandler;
        }

        return null;
    }

    @Override
    protected void removeHandler(ThingHandler thingHandler) {
        things.remove(thingHandler);
        super.removeHandler(thingHandler);
    }
}
