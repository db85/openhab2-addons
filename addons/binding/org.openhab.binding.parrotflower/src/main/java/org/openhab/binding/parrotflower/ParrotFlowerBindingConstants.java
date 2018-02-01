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
package org.openhab.binding.parrotflower;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link ParrotFlowerBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class ParrotFlowerBindingConstants {

    private static final String BINDING_ID = "parrotflower";

    // List of Bridge Type UIDs
    public static final ThingTypeUID THING_TYPE_BRIDGE = new ThingTypeUID(BINDING_ID, "parrotflowerapi");

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_USER_PROFILE = new ThingTypeUID(BINDING_ID, "userprofile");
    public static final ThingTypeUID THING_TYPE_SENSOR_DEVICE = new ThingTypeUID(BINDING_ID, "sensordevice");

    // List of all Channel ids
    public static final String CHANNEL_1 = "channel1";

    // Api URL
    public static final String API_URL = "https://api-flower-power-pot.parrot.com/";

    public static final String DATE_TIME_FORMAT = "yyyy-M-d H:m:s";

}
