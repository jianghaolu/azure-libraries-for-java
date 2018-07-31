/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.v2.management.resources;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Identity for the resource.
 */
public final class Identity {
    /**
     * The principal ID of resource identity.
     */
    @JsonProperty(value = "principalId", access = JsonProperty.Access.WRITE_ONLY)
    private String principalId;

    /**
     * The tenant ID of resource.
     */
    @JsonProperty(value = "tenantId", access = JsonProperty.Access.WRITE_ONLY)
    private String tenantId;

    /**
     * The identity type. Possible values include: 'SystemAssigned'.
     */
    @JsonProperty(value = "type")
    private ResourceIdentityType type;

    /**
     * Get the principalId value.
     *
     * @return the principalId value.
     */
    public String principalId() {
        return this.principalId;
    }

    /**
     * Get the tenantId value.
     *
     * @return the tenantId value.
     */
    public String tenantId() {
        return this.tenantId;
    }

    /**
     * Get the type value.
     *
     * @return the type value.
     */
    public ResourceIdentityType type() {
        return this.type;
    }

    /**
     * Set the type value.
     *
     * @param type the type value to set.
     * @return the Identity object itself.
     */
    public Identity withType(ResourceIdentityType type) {
        this.type = type;
        return this;
    }
}
