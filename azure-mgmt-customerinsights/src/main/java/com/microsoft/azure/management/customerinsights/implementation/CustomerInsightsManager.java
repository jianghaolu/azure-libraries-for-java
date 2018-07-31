/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 * Code generated by Microsoft (R) AutoRest Code Generator.
 */

package com.microsoft.azure.management.customerinsights.implementation;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.AzureResponseBuilder;
import com.microsoft.azure.credentials.AzureTokenCredentials;
import com.microsoft.azure.management.apigeneration.Beta;
import com.microsoft.azure.management.resources.fluentcore.arm.AzureConfigurable;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.AzureConfigurableImpl;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.Manager;
import com.microsoft.azure.management.resources.fluentcore.utils.ProviderRegistrationInterceptor;
import com.microsoft.azure.management.resources.fluentcore.utils.ResourceManagerThrottlingRequestPolicyFactory;
import com.microsoft.azure.serializer.AzureJacksonAdapter;
import com.microsoft.rest.RestClient;

/**
 * Entry point to Azure CustomerInsights resource management.
 */
@Beta(Beta.SinceVersion.V1_2_0)
public final class CustomerInsightsManager extends Manager<CustomerInsightsManager, CustomerInsightsManagementClientImpl> {
    /**
    * Get a Configurable instance that can be used to create CustomerInsightsManager with optional configuration.
    *
    * @return the instance allowing configurations
    */
    public static Configurable configure() {
        return new CustomerInsightsManager.ConfigurableImpl();
    }
    /**
    * Creates an instance of CustomerInsightsManager that exposes CustomerInsights resource management API entry points.
    *
    * @param credentials the credentials to use
    * @param subscriptionId the subscription UUID
    * @return the CustomerInsightsManager
    */
    public static CustomerInsightsManager authenticate(AzureTokenCredentials credentials, String subscriptionId) {
        return new CustomerInsightsManager(new RestClient.Builder()
                .withBaseUrl(credentials.environment(), AzureEnvironment.Endpoint.RESOURCE_MANAGER)
                .withCredentials(credentials)
                .withSerializerAdapter(new AzureJacksonAdapter())
                .withResponseBuilderFactory(new AzureResponseBuilder.Factory())
                .withInterceptor(new ProviderRegistrationInterceptor(credentials))
                .withInterceptor(new ResourceManagerThrottlingRequestPolicyFactory())
                .build(), subscriptionId);
    }
    /**
    * Creates an instance of CustomerInsightsManager that exposes CustomerInsights resource management API entry points.
    *
    * @param restClient the RestClient to be used for API calls.
    * @param subscriptionId the subscription UUID
    * @return the CustomerInsightsManager
    */
    public static CustomerInsightsManager authenticate(RestClient restClient, String subscriptionId) {
        return new CustomerInsightsManager(restClient, subscriptionId);
    }
    /**
    * The interface allowing configurations to be set.
    */
    public interface Configurable extends AzureConfigurable<Configurable> {
        /**
        * Creates an instance of CustomerInsightsManager that exposes CustomerInsights management API entry points.
        *
        * @param credentials the credentials to use
        * @param subscriptionId the subscription UUID
        * @return the interface exposing CustomerInsights management API entry points that work across subscriptions
        */
        CustomerInsightsManager authenticate(AzureTokenCredentials credentials, String subscriptionId);
    }
    /**
    * The implementation for Configurable interface.
    */
    private static final class ConfigurableImpl extends AzureConfigurableImpl<Configurable> implements Configurable {
        public CustomerInsightsManager authenticate(AzureTokenCredentials credentials, String subscriptionId) {
           return CustomerInsightsManager.authenticate(buildRestClient(credentials), subscriptionId);
        }
     }
    private CustomerInsightsManager(RestClient restClient, String subscriptionId) {
        super(
            restClient,
            subscriptionId,
            new CustomerInsightsManagementClientImpl(restClient).withSubscriptionId(subscriptionId));
    }
}
