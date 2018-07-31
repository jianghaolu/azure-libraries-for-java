/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.microsoft.azure.management.resources.fluentcore.arm.collection.implementation.ReadableWrappersImpl;
import com.microsoft.azure.management.resources.fluentcore.utils.PagedListConverter;
import com.microsoft.azure.v2.PagedList;
import com.microsoft.azure.v2.management.resources.Tenant;
import com.microsoft.azure.v2.management.resources.Tenants;
import io.reactivex.Observable;

/**
 * Implementation for {@link Tenants}.
 */
final class TenantsImpl
        implements Tenants {
    private final TenantsInner client;

    TenantsImpl(final TenantsInner client) {
        this.client = client;
    }

    @Override
    public PagedList<Tenant> list() {
        PagedListConverter<TenantIdDescriptionInner, Tenant> converter = new PagedListConverter<TenantIdDescriptionInner, Tenant>() {
            @Override
            public Observable<Tenant> typeConvertAsync(TenantIdDescriptionInner tenantInner) {
                return Observable.just((Tenant) new TenantImpl(tenantInner));
            }
        };
        return converter.convert(client.list());
    }

    @Override
    public Observable<Tenant> listAsync() {
        return ReadableWrappersImpl.convertPageToInnerAsync(client.listAsync()).map(TenantImpl::new);
    }
}
