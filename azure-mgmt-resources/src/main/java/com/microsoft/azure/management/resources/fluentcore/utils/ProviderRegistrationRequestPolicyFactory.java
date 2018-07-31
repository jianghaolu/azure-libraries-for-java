/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.fluentcore.utils;

import com.microsoft.azure.v2.CloudError;
import com.microsoft.azure.v2.credentials.AzureTokenCredentials;
import com.microsoft.azure.v2.management.resources.implementation.ResourceManager;
import com.microsoft.azure.v2.serializer.AzureJacksonAdapter;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;
import com.microsoft.rest.v2.policy.RequestPolicyOptions;
import io.reactivex.Single;

import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An interceptor for automatic provider registration in Azure.
 */
public final class ProviderRegistrationRequestPolicyFactory implements RequestPolicyFactory {
    private final AzureTokenCredentials credentials;

    @Override
    public RequestPolicy create(RequestPolicy requestPolicy, RequestPolicyOptions requestPolicyOptions) {
        return null;
    }

    /**
     * Initialize a provider registration interceptor with a credential that's authorized
     * to register the provider.
     * @param credentials the credential for provider registration
     */
    public ProviderRegistrationRequestPolicyFactory(AzureTokenCredentials credentials) {
        this.credentials = credentials;
    }

    private final class ProviderRegistrationRequestPolicy implements RequestPolicy {
        private RequestPolicy next;

        private ProviderRegistrationRequestPolicy(RequestPolicy next) {
            this.next = next;
        }

        @Override
        public Single<HttpResponse> sendAsync(HttpRequest httpRequest) {
            return this.next.sendAsync(httpRequest)
                    .flatMap(r -> {
                        if (r.statusCode() <= 299) {
                            return Single.just(r);
                        } else {
                            return r.buffer().bodyAsString()
                                    .flatMap(body -> {
                                        AzureJacksonAdapter jacksonAdapter = new AzureJacksonAdapter();
                                        CloudError cloudError = jacksonAdapter.deserialize(body, CloudError.class);
                                        if (cloudError != null && "MissingSubscriptionRegistration".equals(cloudError.code())) {
                                            Pattern pattern = Pattern.compile("/subscriptions/([\\w-]+)/", Pattern.CASE_INSENSITIVE);
                                            Matcher matcher = pattern.matcher(httpRequest.url().toString());
                                            matcher.find();

                                            ResourceManager manager = ResourceManager.authenticate(credentials)
                                                    .withSubscription(matcher.group(1));
                                            pattern = Pattern.compile(".*'(.*)'");
                                            matcher = pattern.matcher(cloudError.message());
                                            matcher.find();

                                            final String namespace = matcher.group(1);

                                            return manager.providers().registerAsync(namespace)
                                                    .concatWith(manager.providers().getByNameAsync(namespace).delay(5, TimeUnit.SECONDS))
                                                    .takeUntil(p -> !p.registrationState().equalsIgnoreCase("Unregistered")
                                                            && !p.registrationState().equalsIgnoreCase("Registering"))
                                                    .lastOrError()
                                                    .flatMap(p -> next.sendAsync(httpRequest));
                                        } else {
                                            return Single.just(r);
                                        }
                                    });
                        }
                    });
        }
    }
}
