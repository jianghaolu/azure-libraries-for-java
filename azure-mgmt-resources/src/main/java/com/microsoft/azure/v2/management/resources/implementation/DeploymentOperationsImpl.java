/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */

package com.microsoft.azure.v2.management.resources.implementation;

import com.microsoft.azure.v2.PagedList;
import com.microsoft.azure.management.resources.fluentcore.arm.collection.implementation.ReadableWrappersImpl;
import com.microsoft.azure.v2.management.resources.Deployment;
import com.microsoft.azure.v2.management.resources.DeploymentOperation;
import com.microsoft.azure.v2.management.resources.DeploymentOperations;
import com.microsoft.rest.v2.ServiceCallback;
import com.microsoft.rest.v2.ServiceFuture;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import rx.functions.Func1;

/**
 * The implementation of {@link DeploymentOperations}.
 */
final class DeploymentOperationsImpl
        extends ReadableWrappersImpl<DeploymentOperation, DeploymentOperationImpl, DeploymentOperationInner>
        implements DeploymentOperations {
    private final DeploymentOperationsInner client;
    private final Deployment deployment;

    DeploymentOperationsImpl(final DeploymentOperationsInner client,
                                    final Deployment deployment) {
        this.client = client;
        this.deployment = deployment;
    }

    @Override
    public PagedList<DeploymentOperation> list() {
        return wrapList(client.listByResourceGroup(deployment.resourceGroupName(), deployment.name()));
    }

    @Override
    public DeploymentOperation getById(String operationId) {
        return getByIdAsync(operationId).blockingGet();
    }

    @Override
    public Maybe<DeploymentOperation> getByIdAsync(String operationId) {
        return client.getAsync(deployment.resourceGroupName(), deployment.name(), operationId)
                .map(this::wrapModel);
    }

    @Override
    public ServiceFuture<DeploymentOperation> getByIdAsync(String id, ServiceCallback<DeploymentOperation> callback) {
        return ServiceFuture.fromBody(getByIdAsync(id), callback);
    }

    @Override
    protected DeploymentOperationImpl wrapModel(DeploymentOperationInner inner) {
        if (inner == null) {
            return null;
        }
        return new DeploymentOperationImpl(inner, this.client);
    }

    @Override
    public Observable<DeploymentOperation> listAsync() {
        return wrapPageAsync(this.client.listByResourceGroupAsync(deployment.resourceGroupName(), deployment.name()));
    }
}
