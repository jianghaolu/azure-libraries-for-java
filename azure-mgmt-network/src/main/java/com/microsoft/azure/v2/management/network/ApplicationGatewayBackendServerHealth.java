/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.v2.management.network;

import com.microsoft.azure.management.apigeneration.Fluent;
import com.microsoft.azure.management.apigeneration.Method;
import com.microsoft.azure.v2.management.network.implementation.ApplicationGatewayBackendHealthServerInner;
import com.microsoft.azure.v2.management.resources.fluentcore.arm.models.HasParent;
import com.microsoft.azure.v2.management.resources.fluentcore.model.HasInner;
import com.microsoft.rest.v2.annotations.Beta;

/**
 * A client-side representation of the health information of an application gateway backend server.
 */
@Fluent
@Beta(since = "V1_4_0")
public interface ApplicationGatewayBackendServerHealth extends
    HasInner<ApplicationGatewayBackendHealthServerInner>,
    HasParent<ApplicationGatewayBackendHttpConfigurationHealth> {

    /**
     * @return IP address of the server this health information pertains to
     */
    String ipAddress();

    /**
     * Gets the IP configuration of the network interface this health information pertains to.
     * @return a network interface IP configuration
     */
    @Method
    NicIPConfiguration getNetworkInterfaceIPConfiguration();

    /**
     * @return the health status of the server
     */
    ApplicationGatewayBackendHealthStatus status();
}