/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.notificationhubs.implementation;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.AzureResponseBuilder;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.management.apigeneration.Beta;
import com.microsoft.azure.management.apigeneration.Beta.SinceVersion;
import com.microsoft.azure.management.resources.fluentcore.arm.AzureConfigurable;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.AzureConfigurableImpl;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.Manager;
import com.microsoft.azure.management.resources.fluentcore.utils.ProviderRegistrationInterceptor;
import com.microsoft.azure.management.resources.fluentcore.utils.ResourceManagerThrottlingRequestPolicyFactory;
import com.microsoft.azure.serializer.AzureJacksonAdapter;
import com.microsoft.rest.RestClient;

/**
 * Entry point to Azure NotificationHubs resource management.
 */
@Beta(SinceVersion.V1_2_0)
public final class NotificationHubsManager extends Manager<NotificationHubsManager, NotificationHubsManagementClientImpl> {
    /**
    * Get a Configurable instance that can be used to create NotificationHubsManager with optional configuration.
    *
    * @return the instance allowing configurations
    */
    public static Configurable configure() {
        return new NotificationHubsManager.ConfigurableImpl();
    }
    /**
    * Creates an instance of NotificationHubsManager that exposes NotificationHubs resource management API entry points.
    *
    * @param credentials the credentials to use
    * @param subscriptionId the subscription UUID
    * @return the NotificationHubsManager
    */
    public static NotificationHubsManager authenticate(AzureTokenCredentials credentials, String subscriptionId) {
        return new NotificationHubsManager(new RestClient.Builder()
                .withBaseUrl(credentials.environment(), AzureEnvironment.Endpoint.RESOURCE_MANAGER)
                .withCredentials(credentials)
                .withSerializerAdapter(new AzureJacksonAdapter())
                .withResponseBuilderFactory(new AzureResponseBuilder.Factory())
                .withInterceptor(new ProviderRegistrationInterceptor(credentials))
                .withInterceptor(new ResourceManagerThrottlingRequestPolicyFactory())
                .build(), subscriptionId);
    }
    /**
    * Creates an instance of NotificationHubsManager that exposes NotificationHubs resource management API entry points.
    *
    * @param restClient the RestClient to be used for API calls.
    * @param subscriptionId the subscription UUID
    * @return the NotificationHubsManager
    */
    public static NotificationHubsManager authenticate(RestClient restClient, String subscriptionId) {
        return new NotificationHubsManager(restClient, subscriptionId);
    }
    /**
    * The interface allowing configurations to be set.
    */
    public interface Configurable extends AzureConfigurable<Configurable> {
        /**
        * Creates an instance of NotificationHubsManager that exposes NotificationHubs management API entry points.
        *
        * @param credentials the credentials to use
        * @param subscriptionId the subscription UUID
        * @return the interface exposing NotificationHubs management API entry points that work across subscriptions
        */
        NotificationHubsManager authenticate(AzureTokenCredentials credentials, String subscriptionId);
    }
    /**
    * The implementation for Configurable interface.
    */
    private static final class ConfigurableImpl extends AzureConfigurableImpl<Configurable> implements Configurable {
        public NotificationHubsManager authenticate(AzureTokenCredentials credentials, String subscriptionId) {
           return NotificationHubsManager.authenticate(buildRestClient(credentials), subscriptionId);
        }
     }
    private NotificationHubsManager(RestClient restClient, String subscriptionId) {
        super(
            restClient,
            subscriptionId,
            new NotificationHubsManagementClientImpl(restClient).withSubscriptionId(subscriptionId));
    }
}
