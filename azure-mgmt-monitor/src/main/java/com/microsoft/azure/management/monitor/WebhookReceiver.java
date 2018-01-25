/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 * Changes may cause incorrect behavior and will be lost if the code is
 * regenerated.
 */

package com.microsoft.azure.management.monitor;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A webhook receiver.
 */
public class WebhookReceiver {
    /**
     * The name of the webhook receiver. Names must be unique across all
     * receivers within an action group.
     */
    @JsonProperty(value = "name", required = true)
    private String name;

    /**
     * The URI where webhooks should be sent.
     */
    @JsonProperty(value = "serviceUri", required = true)
    private String serviceUri;

    /**
     * Get the name value.
     *
     * @return the name value
     */
    public String name() {
        return this.name;
    }

    /**
     * Set the name value.
     *
     * @param name the name value to set
     * @return the WebhookReceiver object itself.
     */
    public WebhookReceiver withName(String name) {
        this.name = name;
        return this;
    }

    /**
     * Get the serviceUri value.
     *
     * @return the serviceUri value
     */
    public String serviceUri() {
        return this.serviceUri;
    }

    /**
     * Set the serviceUri value.
     *
     * @param serviceUri the serviceUri value to set
     * @return the WebhookReceiver object itself.
     */
    public WebhookReceiver withServiceUri(String serviceUri) {
        this.serviceUri = serviceUri;
        return this;
    }

}
