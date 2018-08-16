/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.v2.management.graphrbac.implementation;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.List;

/**
 * Active Directory service principal information.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "objectType")
@JsonTypeName("ServicePrincipal")
public final class ServicePrincipalInner extends DirectoryObjectInner {
    /**
     * The display name of the service principal.
     */
    @JsonProperty(value = "displayName")
    private String displayName;

    /**
     * The application ID.
     */
    @JsonProperty(value = "appId")
    private String appId;

    /**
     * A collection of service principal names.
     */
    @JsonProperty(value = "servicePrincipalNames")
    private List<String> servicePrincipalNames;

    /**
     * Get the displayName value.
     *
     * @return the displayName value.
     */
    public String displayName() {
        return this.displayName;
    }

    /**
     * Set the displayName value.
     *
     * @param displayName the displayName value to set.
     * @return the ServicePrincipalInner object itself.
     */
    public ServicePrincipalInner withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * Get the appId value.
     *
     * @return the appId value.
     */
    public String appId() {
        return this.appId;
    }

    /**
     * Set the appId value.
     *
     * @param appId the appId value to set.
     * @return the ServicePrincipalInner object itself.
     */
    public ServicePrincipalInner withAppId(String appId) {
        this.appId = appId;
        return this;
    }

    /**
     * Get the servicePrincipalNames value.
     *
     * @return the servicePrincipalNames value.
     */
    public List<String> servicePrincipalNames() {
        return this.servicePrincipalNames;
    }

    /**
     * Set the servicePrincipalNames value.
     *
     * @param servicePrincipalNames the servicePrincipalNames value to set.
     * @return the ServicePrincipalInner object itself.
     */
    public ServicePrincipalInner withServicePrincipalNames(List<String> servicePrincipalNames) {
        this.servicePrincipalNames = servicePrincipalNames;
        return this;
    }
}