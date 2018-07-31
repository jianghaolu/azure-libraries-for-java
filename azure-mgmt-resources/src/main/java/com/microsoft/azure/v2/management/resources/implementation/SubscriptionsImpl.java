/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.microsoft.azure.v2.PagedList;
import com.microsoft.azure.management.resources.fluentcore.arm.collection.implementation.ReadableWrappersImpl;
import com.microsoft.azure.management.resources.fluentcore.arm.collection.implementation.SupportsGettingByIdImpl;
import com.microsoft.azure.management.resources.fluentcore.utils.PagedListConverter;
import com.microsoft.azure.v2.management.resources.Subscription;
import com.microsoft.azure.v2.management.resources.Subscriptions;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * The implementation of Subscriptions.
 */
final class SubscriptionsImpl
        extends SupportsGettingByIdImpl<Subscription>
        implements Subscriptions {
    private final SubscriptionsInner client;

    SubscriptionsImpl(final SubscriptionsInner client) {
        this.client = client;
    }

    @Override
    public PagedList<Subscription> list() {
        PagedListConverter<SubscriptionInner, Subscription> converter = new PagedListConverter<SubscriptionInner, Subscription>() {
            @Override
            public Observable<Subscription> typeConvertAsync(SubscriptionInner subscriptionInner) {
                return Observable.just((Subscription) wrapModel(subscriptionInner));
            }
        };
        return converter.convert(client.list());
    }


    @Override
    public Maybe<Subscription> getByIdAsync(String id) {
        return client.getAsync(id).map(this::wrapModel);
    }

    @Override
    public Observable<Subscription> listAsync() {
        return ReadableWrappersImpl.convertPageToInnerAsync(client.listAsync())
                .map(this::wrapModel);
    }

    private Subscription wrapModel(SubscriptionInner subscriptionInner) {
        if (subscriptionInner == null) {
            return null;
        }
        return new SubscriptionImpl(subscriptionInner, client);
    }
}
