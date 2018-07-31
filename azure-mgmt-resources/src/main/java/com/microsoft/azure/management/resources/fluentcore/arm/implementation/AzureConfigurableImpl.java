/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.fluentcore.arm.implementation;

import com.microsoft.azure.management.resources.fluentcore.arm.AzureConfigurable;
import com.microsoft.azure.management.resources.fluentcore.utils.ProviderRegistrationRequestPolicyFactory;
import com.microsoft.azure.management.resources.fluentcore.utils.ResourceManagerThrottlingRequestPolicyFactory;
import com.microsoft.azure.v2.credentials.AzureTokenCredentials;
import com.microsoft.rest.v2.http.HttpClient;
import com.microsoft.rest.v2.http.HttpClientConfiguration;
import com.microsoft.rest.v2.http.HttpPipeline;
import com.microsoft.rest.v2.http.HttpPipelineBuilder;
import com.microsoft.rest.v2.http.NettyClient;
import com.microsoft.rest.v2.policy.CookiePolicyFactory;
import com.microsoft.rest.v2.policy.CredentialsPolicyFactory;
import com.microsoft.rest.v2.policy.DecodingPolicyFactory;
import com.microsoft.rest.v2.policy.HttpLogDetailLevel;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;
import com.microsoft.rest.v2.policy.RetryPolicyFactory;

import java.net.Proxy;

/**
 * The implementation for {@link AzureConfigurable<T>} and the base class for
 * configurable implementations.
 *
 * @param <T> the type of the configurable interface
 */
public class AzureConfigurableImpl<T extends AzureConfigurable<T>>
        implements AzureConfigurable<T> {
    protected HttpClientConfiguration httpClientConfiguration;
    protected HttpPipelineBuilder httpPipelineBuilder;

    protected AzureConfigurableImpl() {
        this.httpPipelineBuilder = new HttpPipelineBuilder();
    }

    @SuppressWarnings("unchecked")
    @Override
    public T withLogLevel(HttpLogDetailLevel level) {
        this.httpPipelineBuilder = httpPipelineBuilder.withHttpLoggingPolicy(level);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T withRequestPolicy(RequestPolicyFactory requestPolicyFactory) {
        this.httpPipelineBuilder = this.httpPipelineBuilder.withRequestPolicy(requestPolicyFactory);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T withUserAgent(String userAgent) {
        this.httpPipelineBuilder = this.httpPipelineBuilder.withUserAgentPolicy(userAgent);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public T withProxy(Proxy proxy) {
        this.httpClientConfiguration = new HttpClientConfiguration(proxy);
        return (T) this;
    }

    protected HttpPipeline buildHttpPipeline(AzureTokenCredentials credentials) {
        HttpClient client = new NettyClient.Factory().create(httpClientConfiguration);
        return httpPipelineBuilder
                .withHttpClient(client)
                .withRequestPolicy(new CredentialsPolicyFactory(credentials))
                .withRequestPolicy(new RetryPolicyFactory())
                .withRequestPolicy(new DecodingPolicyFactory())
                .withRequestPolicy(new CookiePolicyFactory())
                .withRequestPolicy(new ProviderRegistrationRequestPolicyFactory(credentials))
                .withRequestPolicy(new ResourceManagerThrottlingRequestPolicyFactory())
                .build();
    }
}
