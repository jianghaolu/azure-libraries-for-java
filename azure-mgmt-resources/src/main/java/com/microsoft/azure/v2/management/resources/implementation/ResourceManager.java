/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.microsoft.azure.management.resources.fluentcore.arm.AzureConfigurable;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.AzureConfigurableImpl;
import com.microsoft.azure.management.resources.fluentcore.arm.implementation.ManagerBase;
import com.microsoft.azure.management.resources.fluentcore.model.HasInner;
import com.microsoft.azure.management.resources.fluentcore.utils.ProviderRegistrationRequestPolicyFactory;
import com.microsoft.azure.management.resources.fluentcore.utils.ResourceManagerThrottlingRequestPolicyFactory;
import com.microsoft.azure.v2.credentials.AzureTokenCredentials;
import com.microsoft.azure.v2.management.resources.Deployments;
import com.microsoft.azure.v2.management.resources.Features;
import com.microsoft.azure.v2.management.resources.GenericResources;
import com.microsoft.azure.v2.management.resources.PolicyAssignments;
import com.microsoft.azure.v2.management.resources.PolicyDefinitions;
import com.microsoft.azure.v2.management.resources.Providers;
import com.microsoft.azure.v2.management.resources.ResourceGroups;
import com.microsoft.azure.v2.management.resources.Subscriptions;
import com.microsoft.azure.v2.management.resources.Tenants;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.http.HttpPipelineBuilder;
import com.microsoft.rest.v2.http.NettyClient;
import com.microsoft.rest.v2.policy.CookiePolicyFactory;
import com.microsoft.rest.v2.policy.CredentialsPolicyFactory;
import com.microsoft.rest.v2.policy.DecodingPolicyFactory;
import com.microsoft.rest.v2.policy.RetryPolicyFactory;
import io.reactivex.annotations.NonNull;

/**
 * Entry point to Azure resource management.
 */
public final class ResourceManager extends ManagerBase implements HasInner<ResourceManagementClientImpl> {
    // The sdk clients
    private final ResourceManagementClientImpl resourceManagementClient;
    private final FeatureClientImpl featureClient;
    private final PolicyClientImpl policyClient;
    // The collections
    private ResourceGroups resourceGroups;
    private GenericResources genericResources;
    private Deployments deployments;
    private Features features;
    private Providers providers;
    private PolicyDefinitions policyDefinitions;
    private PolicyAssignments policyAssignments;

    /**
     * Creates an instance of ResourceManager that exposes resource management API entry points.
     *
     * @param credentials the credentials to use
     * @return the ResourceManager instance
     */
    public static ResourceManager.Authenticated authenticate(AzureTokenCredentials credentials) {
        return new AuthenticatedImpl(new HttpPipelineBuilder()
                .withHttpClient(new NettyClient.Factory().create(null))
                .withRequestPolicy(new CredentialsPolicyFactory(credentials))
                .withRequestPolicy(new RetryPolicyFactory())
                .withRequestPolicy(new DecodingPolicyFactory())
                .withRequestPolicy(new CookiePolicyFactory())
                .withRequestPolicy(new ProviderRegistrationRequestPolicyFactory(credentials))
                .withRequestPolicy(new ResourceManagerThrottlingRequestPolicyFactory())
                .build());
    }

    /**
     * Creates an instance of ResourceManager that exposes resource management API entry points.
     *
     * @param httpPipeline the HttpPipeline to be used for API calls
     * @return the interface exposing resource management API entry points that work across subscriptions
     */
    public static ResourceManager.Authenticated authenticate(HttpPipeline httpPipeline) {
        return new AuthenticatedImpl(httpPipeline);
    }

    /**
     * Get a Configurable instance that can be used to create ResourceManager with optional configuration.
     *
     * @return the instance allowing configurations
     */
    public static Configurable configure() {
        return new ResourceManager.ConfigurableImpl();
    }

    /**
     * The interface allowing configurations to be set.
     */
    public interface Configurable extends AzureConfigurable<Configurable> {
        /**
         * Creates an instance of ResourceManager that exposes resource management API entry points.
         *
         * @param credentials the credentials to use
         * @return the interface exposing resource management API entry points that work across subscriptions
         */
        ResourceManager.Authenticated authenticate(AzureTokenCredentials credentials);
    }

    /**
     * The implementation for Configurable interface.
     */
    private static class ConfigurableImpl extends AzureConfigurableImpl<Configurable> implements Configurable {
        public ResourceManager.Authenticated authenticate(AzureTokenCredentials credentials) {
            return ResourceManager.authenticate(buildHttpPipeline(credentials));
        }
    }

    /**
     * The interface exposing resource management API entry points that work across subscriptions.
     */
    public interface Authenticated {
        /**
         * @return the entry point to tenant management API.
         */
        Tenants tenants();

        /**
         * @return the entry point to subscription management API.
         */
        Subscriptions subscriptions();

        /**
         * Specifies a subscription to expose resource management API entry points that work in a subscription.
         *
         * @param subscriptionId the subscription UUID
         * @return the ResourceManager instance with entry points that work in a subscription
         */
        ResourceManager withSubscription(String subscriptionId);
    }

    /**
     * The implementation for Authenticated interface.
     */
    private static final class AuthenticatedImpl implements Authenticated {
        private HttpPipeline httpPipeline;
        private SubscriptionClientImpl subscriptionClient;
        // The subscription less collections
        private Subscriptions subscriptions;
        private Tenants tenants;

        AuthenticatedImpl(@NonNull HttpPipeline httpPipeline) {
            this.httpPipeline = httpPipeline;
            this.subscriptionClient = new SubscriptionClientImpl(httpPipeline);
        }

        public Subscriptions subscriptions() {
            if (subscriptions == null) {
                subscriptions = new SubscriptionsImpl(subscriptionClient.subscriptions());
            }
            return subscriptions;
        }

        public Tenants tenants() {
            if (tenants == null) {
                tenants = new TenantsImpl(subscriptionClient.tenants());
            }
            return tenants;
        }

        @Override
        public ResourceManager withSubscription(String subscriptionId) {
           return new ResourceManager(httpPipeline, subscriptionId);
        }
    }

    private ResourceManager(HttpPipeline httpPipeline, String subscriptionId) {
        super(null, subscriptionId);
        super.setResourceManager(this);
        this.resourceManagementClient = new ResourceManagementClientImpl(httpPipeline);
        this.resourceManagementClient.withSubscriptionId(subscriptionId);
        this.featureClient = new FeatureClientImpl(httpPipeline);
        this.featureClient.withSubscriptionId(subscriptionId);
        this.policyClient = new PolicyClientImpl(httpPipeline);
        this.policyClient.withSubscriptionId(subscriptionId);
    }

    /**
     * @return the resource group management API entry point
     */
    public ResourceGroups resourceGroups() {
        if (resourceGroups == null) {
            resourceGroups = new ResourceGroupsImpl(resourceManagementClient);
        }
        return resourceGroups;
    }

    /**
     * @return the generic resource management API entry point
     */
    public GenericResources genericResources() {
        if (genericResources == null) {
            genericResources = new GenericResourcesImpl(this);
        }
        return genericResources;
    }

    /**
     * @return the deployment management API entry point
     */
    public Deployments deployments() {
        if (deployments == null) {
            deployments = new DeploymentsImpl(this);
        }
        return deployments;
    }

    /**
     * @return the feature management API entry point
     */
    public Features features() {
        if (features == null) {
            features = new FeaturesImpl(featureClient.features());
        }
        return features;
    }

    /**
     * @return the resource provider management API entry point
     */
    public Providers providers() {
        if (providers == null) {
            providers = new ProvidersImpl(resourceManagementClient.providers());
        }
        return providers;
    }

    /**
     * @return the policy definition management API entry point
     */
    public PolicyDefinitions policyDefinitions() {
        if (policyDefinitions == null) {
            policyDefinitions = new PolicyDefinitionsImpl(policyClient.policyDefinitions());
        }
        return policyDefinitions;
    }

    /**
     * @return the policy assignment management API entry point
     */
    public PolicyAssignments policyAssignments() {
        if (policyAssignments == null) {
            policyAssignments = new PolicyAssignmentsImpl(policyClient.policyAssignments());
        }
        return policyAssignments;
    }

    @Override
    public ResourceManagementClientImpl inner() {
        return this.resourceManagementClient;
    }
}
