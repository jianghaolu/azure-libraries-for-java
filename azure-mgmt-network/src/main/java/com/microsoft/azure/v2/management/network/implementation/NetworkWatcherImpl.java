/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.v2.management.network.implementation;

import com.microsoft.azure.management.apigeneration.LangDefinition;
import com.microsoft.azure.v2.management.network.FlowLogSettings;
import com.microsoft.azure.v2.management.network.NetworkWatcher;
import com.microsoft.azure.v2.management.network.SecurityGroupView;
import com.microsoft.azure.v2.management.network.model.AppliableWithTags;
import com.microsoft.azure.v2.management.resources.fluentcore.arm.models.implementation.GroupableResourceImpl;
import com.microsoft.rest.v2.ServiceCallback;
import com.microsoft.rest.v2.ServiceFuture;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Implementation for Network Watcher and its create and update interfaces.
 */
@LangDefinition
class NetworkWatcherImpl
        extends GroupableResourceImpl<
        NetworkWatcher,
                NetworkWatcherInner,
                NetworkWatcherImpl,
                NetworkManager>
        implements
        NetworkWatcher,
        NetworkWatcher.Definition,
        NetworkWatcher.Update,
        AppliableWithTags<NetworkWatcher> {

    private PacketCapturesImpl packetCaptures;
    private ConnectionMonitorsImpl connectionMonitors;

    NetworkWatcherImpl(String name,
                final NetworkWatcherInner innerModel,
                final NetworkManager networkManager) {
        super(name, innerModel, networkManager);
        this.packetCaptures = new PacketCapturesImpl(networkManager.inner().packetCaptures(), this);
        this.connectionMonitors = new ConnectionMonitorsImpl(networkManager.inner().connectionMonitors(), this);
    }

    public PacketCapturesImpl packetCaptures() {
        return packetCaptures;
    }

    @Override
    public ConnectionMonitorsImpl connectionMonitors() {
        return connectionMonitors;
    }

    // Verbs

    @Override
    public TopologyImpl topology() {
        return new TopologyImpl(this);
    }

    @Override
    public SecurityGroupView getSecurityGroupView(String vmId) {
        SecurityGroupViewResultInner securityGroupViewResultInner = this.manager().inner().networkWatchers()
                .getVMSecurityRules(this.resourceGroupName(), this.name(), vmId);
        return new SecurityGroupViewImpl(this, securityGroupViewResultInner, vmId);
    }

    @Override
    public Observable<SecurityGroupView> getSecurityGroupViewAsync(final String vmId) {
        return this.manager().inner().networkWatchers()
                .getVMSecurityRulesAsync(this.resourceGroupName(), this.name(), vmId)
                .map(inner -> (SecurityGroupView) new SecurityGroupViewImpl(NetworkWatcherImpl.this, inner, vmId))
                .toObservable();
    }

    public FlowLogSettings getFlowLogSettings(String nsgId) {
        FlowLogInformationInner flowLogInformationInner = this.manager().inner().networkWatchers()
                .getFlowLogStatus(this.resourceGroupName(), this.name(), nsgId);
        return new FlowLogSettingsImpl(this, flowLogInformationInner, nsgId);
    }

    @Override
    public Observable<FlowLogSettings> getFlowLogSettingsAsync(final String nsgId) {
        return this.manager().inner().networkWatchers()
                .getFlowLogStatusAsync(this.resourceGroupName(), this.name(), nsgId)
                .map(inner -> (FlowLogSettings) new FlowLogSettingsImpl(NetworkWatcherImpl.this, inner, nsgId))
                .toObservable();
    }

    public NextHopImpl nextHop() {
        return new NextHopImpl(this);
    }

    @Override
    public VerificationIPFlowImpl verifyIPFlow() {
        return new VerificationIPFlowImpl(this);
    }

    @Override
    public ConnectivityCheckImpl checkConnectivity() {
        return new ConnectivityCheckImpl(this);
    }

    @Override
    public TroubleshootingImpl troubleshoot() {
        return new TroubleshootingImpl(this);
    }

    @Override
    public AvailableProvidersImpl availableProviders() {
        return new AvailableProvidersImpl(this);
    }

    @Override
    public AzureReachabilityReportImpl azureReachabilityReport() {
        return new AzureReachabilityReportImpl(this);
    }

    @Override
    public Observable<NetworkWatcher> createResourceAsync() {
        return this.manager().inner().networkWatchers().createOrUpdateAsync(
                this.resourceGroupName(), this.name(), this.inner())
                .map(innerToFluentMap(this))
                .toObservable();
    }

    @Override
    protected Maybe<NetworkWatcherInner> getInnerAsync() {
        return this.manager().inner().networkWatchers().getByResourceGroupAsync(this.resourceGroupName(), this.name());
    }

    @Override
    public NetworkWatcherImpl updateTags() {
        return this;
    }

    @Override
    public NetworkWatcher applyTags() {
        return applyTagsAsync().blockingLast();
    }

    @Override
    public Observable<NetworkWatcher> applyTagsAsync() {
        return this.manager().inner().networkWatchers().updateTagsAsync(resourceGroupName(), name(), inner().getTags())
                .map(inner -> {
                    setInner(inner);
                    return (NetworkWatcher) NetworkWatcherImpl.this;
                })
                .toObservable();
    }

    @Override
    public ServiceFuture<NetworkWatcher> applyTagsAsync(ServiceCallback<NetworkWatcher> callback) {
        return ServiceFuture.fromBody(applyTagsAsync().lastElement(), callback);
    }
}