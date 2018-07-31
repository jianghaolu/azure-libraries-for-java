/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.core;

import com.microsoft.azure.v2.management.resources.implementation.ResourceGroupInner;
import com.microsoft.azure.v2.serializer.AzureJacksonAdapter;
import com.microsoft.rest.v2.http.HttpMethod;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;
import com.microsoft.rest.v2.policy.RequestPolicyOptions;
import com.microsoft.rest.v2.util.FlowableUtil;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.nio.ByteBuffer;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.HashMap;
import java.util.Map;

/**
 * An interceptor for tagging resource groups created in tests.
 */
public class ResourceGroupTaggingRequestPolicyFactory implements RequestPolicyFactory {
    private static final String LOGGING_CONTEXT = "com.microsoft.azure.v2.management.resources.ResourceGroups createOrUpdate";
    private AzureJacksonAdapter adapter = new AzureJacksonAdapter();

    @Override
    public RequestPolicy create(RequestPolicy requestPolicy, RequestPolicyOptions requestPolicyOptions) {
        return new ResourceGroupTaggingRequestPolicy(requestPolicy);
    }

    private final class ResourceGroupTaggingRequestPolicy implements RequestPolicy {
        private RequestPolicy next;

        private ResourceGroupTaggingRequestPolicy(RequestPolicy next) {
            this.next = next;
        }

        @Override
        public Single<HttpResponse> sendAsync(HttpRequest httpRequest) {
            if (HttpMethod.PUT.equals(httpRequest.httpMethod()) && httpRequest.url().toString().contains("/resourcegroups/") &&
                    LOGGING_CONTEXT.equals(httpRequest.headers().value("x-ms-logging-context"))) {

                bodyToString(httpRequest)
                        .map(body -> {
                            ResourceGroupInner rg = adapter.deserialize(body, ResourceGroupInner.class);
                            if (rg == null) {
                                throw new RuntimeException("Failed to deserialize " + body);
                            }
                            Map<String, String> tags = rg.tags();
                            if (tags == null) {
                                tags = new HashMap<>();
                            }
                            tags.put("product", "javasdk");
                            tags.put("cause", "automation");
                            tags.put("date", OffsetDateTime.now(ZoneOffset.UTC).toString());
                            if (System.getenv("ENV_JOB_NAME") != null) {
                                tags.put("job", System.getenv("ENV_JOB_NAME"));
                            }
                            rg.withTags(tags);

                            String newBody = adapter.serialize(rg);
                            HttpRequest newRequest = new HttpRequest(
                                    "resourceGroupTagging",
                                    HttpMethod.PUT,
                                    httpRequest.url(),
                                    httpRequest.headers().set("Content-Length", String.valueOf(newBody.length())),
                                    Flowable.fromArray(ByteBuffer.wrap(newBody.getBytes())),
                                    httpRequest.responseDecoder());
                            return next.sendAsync(newRequest);
                        });

            }
            return next.sendAsync(httpRequest);
        }

        private Single<String> bodyToString(final HttpRequest request) {
            final Flowable<ByteBuffer> body = request.buffer().body();
            return FlowableUtil.collectBytesInArray(body).map(String::new);
        }

    }
}