/**
 * Copyright (c) 2010-2018 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.parrotflower.internal.discovery;

import static org.openhab.binding.parrotflower.ParrotFlowerBindingConstants.THING_TYPE_USER_PROFILE;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.smarthome.config.discovery.AbstractDiscoveryService;
import org.eclipse.smarthome.config.discovery.DiscoveryResultBuilder;
import org.eclipse.smarthome.core.thing.ThingTypeUID;
import org.eclipse.smarthome.core.thing.ThingUID;
import org.openhab.binding.parrotflower.handler.ParrotFlowerBridgeHandler;
import org.openhab.binding.parrotflower.internal.ParrotFlowerBridgeConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.reactivex.disposables.CompositeDisposable;

/**
 * The {@link ParrotFlowerBridgeConfiguration} class handle the device discovery
 *
 * @author Daniel Bauer - Initial contribution
 */
public class ParrotFlowerDiscoveryService extends AbstractDiscoveryService
        implements ParrotFlowerBridgeHandler.DiscoveryListener {

    private static final int DISCOVER_TIMEOUT_SECONDS = 10;
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES = Collections.singleton(THING_TYPE_USER_PROFILE);

    private ParrotFlowerBridgeHandler bridgeHandler;
    private final Logger logger = LoggerFactory.getLogger(ParrotFlowerDiscoveryService.class);
    private CompositeDisposable scanDisposeable;

    public ParrotFlowerDiscoveryService(ParrotFlowerBridgeHandler bridgeHandler) {
        super(SUPPORTED_THING_TYPES, DISCOVER_TIMEOUT_SECONDS, false);
        this.bridgeHandler = bridgeHandler;
        bridgeHandler.setDiscoveryListener(this);
    }

    @Override
    public synchronized void abortScan() {
        if (scanDisposeable != null && !scanDisposeable.isDisposed()) {
            scanDisposeable.dispose();
        }
        super.abortScan();
    }

    @Override
    public void onDeviceDiscovered(ThingTypeUID thingTypeUID, String identifier, String label) {
        ThingUID sensorThing = new ThingUID(thingTypeUID, identifier.replaceAll("[^a-zA-Z0-9_]", ""));
        Map<String, Object> properties = new HashMap<>();
        properties.put("identifier", identifier);

        thingDiscovered(DiscoveryResultBuilder.create(sensorThing).withBridge(bridgeHandler.getThing().getBridgeUID())
                .withLabel(label).withProperties(properties).build());
    }

    @Override
    protected void startScan() {
        if (scanDisposeable != null && !scanDisposeable.isDisposed()) {
            scanDisposeable.dispose();
        }
        scanDisposeable = new CompositeDisposable();

        scanDisposeable.add(bridgeHandler.loadData().subscribe(() -> {
            logger.debug("scan success");
        }, (throwable) -> {
            logger.warn("scan failed {}", throwable);
        }));

    }
}