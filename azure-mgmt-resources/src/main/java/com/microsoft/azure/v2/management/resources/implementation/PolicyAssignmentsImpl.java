/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.microsoft.azure.management.resources.fluentcore.arm.ResourceUtils;
import com.microsoft.azure.management.resources.fluentcore.arm.collection.implementation.CreatableWrappersImpl;
import com.microsoft.azure.v2.PagedList;
import com.microsoft.azure.v2.management.resources.PolicyAssignment;
import com.microsoft.azure.v2.management.resources.PolicyAssignments;
import com.microsoft.rest.v2.ServiceCallback;
import com.microsoft.rest.v2.ServiceFuture;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * The implementation for {@link PolicyAssignments} and its parent interfaces.
 */
final class PolicyAssignmentsImpl
        extends CreatableWrappersImpl<PolicyAssignment, PolicyAssignmentImpl, PolicyAssignmentInner>
        implements PolicyAssignments {
    private final PolicyAssignmentsInner client;

    /**
     * Creates an instance of the implementation.
     *
     * @param innerClient the inner policies client
     */
    PolicyAssignmentsImpl(final PolicyAssignmentsInner innerClient) {
        this.client = innerClient;
    }

    @Override
    public PagedList<PolicyAssignment> list() {
        return wrapList(client.list());
    }

    @Override
    public Completable deleteByIdAsync(String id) {
        return Completable.fromObservable(client.deleteByIdAsync(id).toObservable());
    }

    @Override
    public PolicyAssignmentImpl define(String name) {
        return wrapModel(name);
    }

    @Override
    protected PolicyAssignmentImpl wrapModel(String name) {
        return new PolicyAssignmentImpl(name,
                new PolicyAssignmentInner().withDisplayName(name),
                client);
    }

    @Override
    protected PolicyAssignmentImpl wrapModel(PolicyAssignmentInner inner) {
        if (inner == null) {
            return null;
        }
        return new PolicyAssignmentImpl(inner.name(), inner, client);
    }

    @Override
    public PagedList<PolicyAssignment> listByResource(String resourceId) {
        return wrapList(client.listForResource(
                ResourceUtils.groupFromResourceId(resourceId),
                ResourceUtils.resourceProviderFromResourceId(resourceId),
                ResourceUtils.relativePathFromResourceId(ResourceUtils.parentResourceIdFromResourceId(resourceId)),
                ResourceUtils.resourceTypeFromResourceId(resourceId),
                ResourceUtils.nameFromResourceId(resourceId)
        ));
    }

    @Override
    public PolicyAssignment getById(String id) {
        return getByIdAsync(id).blockingGet();
    }

    @Override
    public Maybe<PolicyAssignment> getByIdAsync(String id) {
        return client.getByIdAsync(id).map(this::wrapModel);
    }

    @Override
    public ServiceFuture<PolicyAssignment> getByIdAsync(String id, ServiceCallback<PolicyAssignment> callback) {
        return ServiceFuture.fromBody(getByIdAsync(id), callback);
    }

    @Override
    public PagedList<PolicyAssignment> listByResourceGroup(String resourceGroupName) {
        return wrapList(client.listByResourceGroup(resourceGroupName));
    }

    @Override
    public Observable<PolicyAssignment> listAsync() {
        return wrapPageAsync(this.client.listAsync());
    }

    @Override
    public Observable<PolicyAssignment> listByResourceGroupAsync(String resourceGroupName) {
        return wrapPageAsync(this.client.listByResourceGroupAsync(resourceGroupName));
    }
}
