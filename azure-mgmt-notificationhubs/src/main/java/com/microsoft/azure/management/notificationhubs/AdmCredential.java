/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 *
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.notificationhubs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.microsoft.rest.serializer.JsonFlatten;

/**
 * Description of a NotificationHub AdmCredential.
 */
@JsonFlatten
public class AdmCredential {
    /**
     * The client identifier.
     */
    @JsonProperty(value = "properties.clientId")
    private String clientId;

    /**
     * The credential secret access key.
     */
    @JsonProperty(value = "properties.clientSecret")
    private String clientSecret;

    /**
     * The URL of the authorization token.
     */
    @JsonProperty(value = "properties.authTokenUrl")
    private String authTokenUrl;

    /**
     * Get the clientId value.
     *
     * @return the clientId value
     */
    public String clientId() {
        return this.clientId;
    }

    /**
     * Set the clientId value.
     *
     * @param clientId the clientId value to set
     * @return the AdmCredential object itself.
     */
    public AdmCredential withClientId(String clientId) {
        this.clientId = clientId;
        return this;
    }

    /**
     * Get the clientSecret value.
     *
     * @return the clientSecret value
     */
    public String clientSecret() {
        return this.clientSecret;
    }

    /**
     * Set the clientSecret value.
     *
     * @param clientSecret the clientSecret value to set
     * @return the AdmCredential object itself.
     */
    public AdmCredential withClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
        return this;
    }

    /**
     * Get the authTokenUrl value.
     *
     * @return the authTokenUrl value
     */
    public String authTokenUrl() {
        return this.authTokenUrl;
    }

    /**
     * Set the authTokenUrl value.
     *
     * @param authTokenUrl the authTokenUrl value to set
     * @return the AdmCredential object itself.
     */
    public AdmCredential withAuthTokenUrl(String authTokenUrl) {
        this.authTokenUrl = authTokenUrl;
        return this;
    }

}