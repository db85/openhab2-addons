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
package org.openhab.binding.hp1000;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.smarthome.core.thing.ThingTypeUID;

/**
 * The {@link HP1000BindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Daniel Bauer - Initial contribution
 */
@NonNullByDefault
public class HP1000BindingConstants {

    private static final String BINDING_ID = "hp1000";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_SAMPLE = new ThingTypeUID(BINDING_ID, "weatherstation");

    // List of all Channel ids
    public static final String CHANNEL_1 = "channel1";

    // Webhook path
    public static final String SERVLET_BINDING_ALIAS = "/weatherstation";
    public static final String WEBHOOL_PATH = "/updateweatherstation.php";

    // Config parameters
    public static final String CONFIG_PARAMETER_HOST_NAME = "hostname";
}
