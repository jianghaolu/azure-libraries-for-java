/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.fluentcore.arm;

import com.microsoft.rest.v2.policy.HttpLogDetailLevel;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;

import java.net.Proxy;

/**
 * The base interface for allowing configurations to be made on the HTTP client.
 *
 * @param <T> the actual type of the interface extending this interface
 */
public interface AzureConfigurable<T extends AzureConfigurable<T>> {
    /**
     * Set the logging level on the HTTP client.
     *
     * @param level the OkHttp logging level
     * @return the configurable object itself
     */
    T withLogLevel(HttpLogDetailLevel level);

    /**
     * Plug in a request policy factory into the HTTP pipeline.
     *
     * @param requestPolicyFactory the request policy factory to plug in
     * @return the configurable object itself
     */
    T withRequestPolicy(RequestPolicyFactory requestPolicyFactory);

    /**
     * Specify the user agent header.
     *
     * @param userAgent the user agent to use
     * @return the configurable object itself
     */
    T withUserAgent(String userAgent);

    /**
     * Sets the proxy for the HTTP client.
     *
     * @param proxy the proxy to use
     * @return the configurable object itself for chaining
     */
    T withProxy(Proxy proxy);
}
