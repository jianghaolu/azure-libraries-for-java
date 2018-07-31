/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.management.resources.fluentcore.utils;

import com.microsoft.azure.management.resources.fluentcore.arm.ResourceUtils;
import com.microsoft.rest.v2.http.HttpRequest;
import com.microsoft.rest.v2.http.HttpResponse;
import com.microsoft.rest.v2.policy.RequestPolicy;
import com.microsoft.rest.v2.policy.RequestPolicyFactory;
import com.microsoft.rest.v2.policy.RequestPolicyOptions;
import io.reactivex.Single;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.ReentrantLock;
/**
 * An interceptor for automatic retry when Azure Resource Manager is throttling because of too many read/write requests.
 * <p>
 * For each subscription and tenant, Azure Resource Manager limits read requests to 15,000 per hour and
 *   write requests to 1,200 per hour. These limits apply to each Azure Resource Manager instance.
 */
public class ResourceManagerThrottlingRequestPolicyFactory implements RequestPolicyFactory {
    private static final String LOGGING_HEADER = "x-ms-logging-context";
    private static final ConcurrentMap<String, ReentrantLock> REENTRANT_LOCK_MAP = new ConcurrentHashMap<>();

    @Override
    public RequestPolicy create(RequestPolicy requestPolicy, RequestPolicyOptions requestPolicyOptions) {
        return new ResourceManagerThrottlingRequestPolicy(requestPolicy);
    }

    private final class ResourceManagerThrottlingRequestPolicy implements RequestPolicy {
        private RequestPolicy next;

        private ResourceManagerThrottlingRequestPolicy(RequestPolicy next) {
            this.next = next;
        }

        @Override
        public Single<HttpResponse> sendAsync(HttpRequest httpRequest) {
            String subscriptionId = ResourceUtils.extractFromResourceId(httpRequest.url().getPath(), "subscriptions");
            // TODO Implement this asynchronously
            return next.sendAsync(httpRequest);
        }
    }

//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        // Gate control
//        String subscriptionId = ResourceUtils.extractFromResourceId(chain.request().url().url().getPath(), "subscriptions");
//        if (subscriptionId == null) {
//            subscriptionId = "global";
//        }
//        REENTRANT_LOCK_MAP.putIfAbsent(subscriptionId, new ReentrantLock());
//        try {
//            synchronized (REENTRANT_LOCK_MAP.get(subscriptionId)) {
//                if (REENTRANT_LOCK_MAP.get(subscriptionId).isLocked()) {
//                    REENTRANT_LOCK_MAP.get(subscriptionId).wait();
//                }
//            }
//        } catch (InterruptedException e) {
//            throw new IOException(e);
//        }
//        Response response = chain.proceed(chain.request());
//        if (response.code() != 429) {
//            return response;
//        }
//
//        try {
//            synchronized (REENTRANT_LOCK_MAP.get(subscriptionId)) {
//                if (REENTRANT_LOCK_MAP.get(subscriptionId).isLocked()) {
//                    REENTRANT_LOCK_MAP.get(subscriptionId).wait();
//                    return chain.proceed(chain.request());
//                } else {
//                    REENTRANT_LOCK_MAP.get(subscriptionId).lock();
//                }
//            }
//        } catch (InterruptedException e) {
//            throw new IOException(e);
//        }
//
//        try {
//            String retryAfterHeader = response.header("Retry-After");
//            int retryAfter = 0;
//            if (retryAfterHeader != null) {
//                retryAfter = Integer.parseInt(retryAfterHeader);
//            }
//            if (retryAfter <= 0) {
//                Pattern pattern = Pattern.compile("try again after '([0-9]*)' minutes", Pattern.CASE_INSENSITIVE);
//                Matcher matcher = pattern.matcher(content(response.body()));
//                if (matcher.find()) {
//                    retryAfter = (int) TimeUnit.MINUTES.toSeconds(Integer.parseInt(matcher.group(1)));
//                } else {
//                    pattern = Pattern.compile("try again after '([0-9]*)' seconds", Pattern.CASE_INSENSITIVE);
//                    matcher = pattern.matcher(content(response.body()));
//                    if (matcher.find()) {
//                        retryAfter = Integer.parseInt(matcher.group(1));
//                    }
//                }
//            }
//            if (retryAfter > 0) {
//                String context = chain.request().header(LOGGING_HEADER);
//                if (context == null) {
//                    context = "";
//                }
//                LoggerFactory.getLogger(context)
//                    .info("Azure Resource Manager read/write per hour limit reached. Will retry in: " + retryAfter + " seconds");
//                SdkContext.sleep((int) (TimeUnit.SECONDS.toMillis(retryAfter) + 100));
//            }
//            return chain.proceed(chain.request());
//        } catch (Throwable t) {
//            throw new IOException(t);
//        } finally {
//            synchronized (REENTRANT_LOCK_MAP.get(subscriptionId)) {
//                REENTRANT_LOCK_MAP.get(subscriptionId).unlock();
//                REENTRANT_LOCK_MAP.get(subscriptionId).notifyAll();
//            }
//        }
//    }
//
//    private String content(ResponseBody responseBody) throws IOException {
//        if (responseBody == null) {
//            return null;
//        }
//        BufferedSource source = responseBody.source();
//        source.request(Long.MAX_VALUE); // Buffer the entire body.
//        Buffer buffer = source.buffer();
//        return buffer.readUtf8();
//    }
}
