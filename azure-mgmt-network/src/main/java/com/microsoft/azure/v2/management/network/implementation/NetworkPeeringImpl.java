/**
 * Copyright (c) Microsoft Corporation. All rights reserved.
 * Licensed under the MIT License. See License.txt in the project root for
 * license information.
 */
package com.microsoft.azure.v2.management.network.implementation;

import com.microsoft.azure.v2.SubResource;
import com.microsoft.azure.management.apigeneration.LangDefinition;
import com.microsoft.azure.v2.management.network.NetworkPeeringGatewayUse;
import com.microsoft.azure.v2.management.network.Network;
import com.microsoft.azure.v2.management.network.NetworkPeering;
import com.microsoft.azure.v2.management.network.NetworkPeering.DefinitionStages.WithCreate;
import com.microsoft.azure.v2.management.network.VirtualNetworkPeeringState;
import com.microsoft.azure.v2.management.resources.fluentcore.arm.ResourceUtils;
import com.microsoft.azure.v2.management.resources.fluentcore.arm.models.IndependentChild;
import com.microsoft.azure.v2.management.resources.fluentcore.arm.models.implementation.IndependentChildImpl;
import com.microsoft.azure.v2.management.resources.fluentcore.utils.SdkContext;
import com.microsoft.azure.v2.management.resources.fluentcore.utils.Utils;
import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

import java.util.Collections;
import java.util.List;

/**
 *  Implementation for network peering.
 */
@LangDefinition
class NetworkPeeringImpl
    extends IndependentChildImpl<
        NetworkPeering,
        Network,
        VirtualNetworkPeeringInner,
        NetworkPeeringImpl,
        NetworkManager>
    implements
        NetworkPeering,
        NetworkPeering.Definition,
        NetworkPeering.Update,
        IndependentChild.DefinitionStages.WithParentResource<NetworkPeering, Network> {

    private NetworkImpl parent;
    private Network remoteNetwork;
    private Boolean remoteAccess;                   // Controls the allowAccess setting on the remote peering (null means no change)
    private Boolean remoteForwarding;               // Controls the trafficForwarding setting on the remote peering (null means no change)
    private Boolean startGatewayUseByRemoteNetwork; // Controls the UseGateway setting on the remote network (null means no change)
    private Boolean allowGatewayUseOnRemoteNetwork; // Controls the AllowGatewayTransit setting on the remote network (null means no change)

    NetworkPeeringImpl(VirtualNetworkPeeringInner inner, NetworkImpl parent) {
        super(inner.name(), inner, parent.manager());
        this.parent = parent;
        this.remoteAccess = null;
        this.remoteForwarding = null;
    }

    // Getters

    @Override
    public boolean isSameSubscription() {
        if (this.remoteNetworkId() == null) {
            return false;
        }
        String localSubscriptionId = ResourceUtils.subscriptionFromResourceId(this.id());
        String remoteSubscriptionId = ResourceUtils.subscriptionFromResourceId(this.remoteNetworkId());
        return localSubscriptionId.equalsIgnoreCase(remoteSubscriptionId);
    }

    @Override
    public VirtualNetworkPeeringState state() {
        return this.inner().peeringState();
    }

    @Override
    public String name() {
        return this.inner().name();
    }

    @Override
    public String id() {
        return this.inner().id();
    }

    @Override
    public String networkId() {
        return this.parent.id();
    }

    @Override
    public List<String> remoteAddressSpaces() {
        if (inner().remoteAddressSpace() == null || inner().remoteAddressSpace().addressPrefixes() == null) {
            return null;
        }
        return Collections.unmodifiableList(inner().remoteAddressSpace().addressPrefixes());
    }

    @Override
    public String remoteNetworkId() {
        return (this.inner().remoteVirtualNetwork() != null) ? this.inner().remoteVirtualNetwork().id() : null;
    }

    @Override
    public boolean isTrafficForwardingFromRemoteNetworkAllowed() {
        return Utils.toPrimitiveBoolean(this.inner().allowForwardedTraffic());
    }

    private boolean isAccessFromRemoteNetworkAllowed() {
        return Utils.toPrimitiveBoolean(this.inner().allowVirtualNetworkAccess());
    }

    // Fluent setters

    private NetworkPeeringImpl withoutAccessFromRemoteNetwork() {
        this.inner().withAllowVirtualNetworkAccess(false);
        return this;
    }

    private NetworkPeeringImpl withAccessFromRemoteNetwork() {
        this.inner().withAllowVirtualNetworkAccess(true);
        return this;
    }

    @Override
    public NetworkPeeringImpl withAccessBetweenBothNetworks() {
        return this.withAccessFromRemoteNetwork().withAccessToRemoteNetwork();
    }

    @Override
    public NetworkPeeringImpl withoutAccessFromEitherNetwork() {
        return this.withoutAccessFromRemoteNetwork().withoutAccessToRemoteNetwork();
    }

    @Override
    public NetworkPeeringImpl withRemoteNetwork(String resourceId) {
        SubResource networkRef = new SubResource().withId(resourceId);
        this.inner().withRemoteVirtualNetwork(networkRef);
        return this;
    }

    @Override
    public NetworkPeeringImpl withRemoteNetwork(Network network) {
        if (network != null) {
            this.remoteNetwork = network;
            return this.withRemoteNetwork(network.id());
        }
        return this;
    }

    @Override
    public NetworkPeeringImpl withTrafficForwardingFromRemoteNetwork() {
        this.inner().withAllowForwardedTraffic(true);
        return this;
    }

    @Override
    public NetworkPeeringImpl withoutTrafficForwardingFromRemoteNetwork() {
        this.inner().withAllowForwardedTraffic(false);
        return this;
    }

    @Override
    public NetworkPeeringImpl withoutTrafficForwardingFromEitherNetwork() {
        return this.withoutTrafficForwardingFromRemoteNetwork().withoutTrafficForwardingToRemoteNetwork();
    }

    @Override
    public NetworkPeeringImpl withTrafficForwardingToRemoteNetwork() {
        this.remoteForwarding = true;
        return this;
    }

    @Override
    public NetworkPeeringImpl withoutTrafficForwardingToRemoteNetwork() {
        this.remoteForwarding = false;
        return this;
    }

    @Override
    public NetworkPeeringImpl withTrafficForwardingBetweenBothNetworks() {
        return this.withTrafficForwardingFromRemoteNetwork().withTrafficForwardingToRemoteNetwork();
    }

    private NetworkPeeringImpl withoutAccessToRemoteNetwork() {
        this.remoteAccess = false;
        return this;
    }

    private NetworkPeeringImpl withAccessToRemoteNetwork() {
        this.remoteAccess = true;
        return this;
    }

    @Override
    public NetworkPeeringImpl withGatewayUseByRemoteNetworkAllowed() {
        this.inner()
            .withAllowGatewayTransit(true)
            .withUseRemoteGateways(false);
        this.startGatewayUseByRemoteNetwork = null;
        this.allowGatewayUseOnRemoteNetwork = false;
        return this;
    }

    @Override
    public NetworkPeeringImpl withGatewayUseByRemoteNetworkStarted() {
        this.startGatewayUseByRemoteNetwork = true;
        this.allowGatewayUseOnRemoteNetwork = false;
        return this.withGatewayUseByRemoteNetworkAllowed();
    }

    @Override
    public NetworkPeeringImpl withGatewayUseOnRemoteNetworkStarted() {
        this.inner()
            .withAllowGatewayTransit(false)
            .withUseRemoteGateways(true);
        this.startGatewayUseByRemoteNetwork = false;
        this.allowGatewayUseOnRemoteNetwork = true;
        return this;
    }

    @Override
    public NetworkPeeringImpl withoutAnyGatewayUse() {
        this.inner()
            .withAllowGatewayTransit(false);
        return this
            .withoutGatewayUseOnRemoteNetwork()
            .withoutGatewayUseByRemoteNetwork();
    }

    @Override
    public NetworkPeeringImpl withoutGatewayUseByRemoteNetwork() {
        this.startGatewayUseByRemoteNetwork = false;
        this.allowGatewayUseOnRemoteNetwork = false;
        return this;
    }

    @Override
    public NetworkPeeringImpl withoutGatewayUseOnRemoteNetwork() {
        this.inner().withUseRemoteGateways(false);
        return this;
    }

    // Actions

    @Override
    public boolean checkAccessBetweenNetworks() {
        if (!Utils.toPrimitiveBoolean(this.inner().allowVirtualNetworkAccess())) {
            // If network access is disabled on this peering, then it's disabled for both networks, regardless of what the remote peering says
            return false;
        }

        // Check the access setting on the remote peering
        NetworkPeering remotePeering = this.getRemotePeering();
        if (remotePeering == null) {
            return false;
        } else {
            // Access is enabled on local peering, so up to the remote peering to determine whether it's enabled or disabled overall
            return Utils.toPrimitiveBoolean(remotePeering.inner().allowVirtualNetworkAccess());
        }
    }

    @Override
    protected Observable<NetworkPeering> createChildResourceAsync() {
        final NetworkPeeringImpl localPeering = this;
        final String networkName = ResourceUtils.nameFromResourceId(this.networkId());
        //
        return this.manager().inner().virtualNetworkPeerings()
                .createOrUpdateAsync(this.parent.resourceGroupName(), networkName, this.name(), this.inner())
                // After successful creation, update the inner
                .map(inner ->  {
                    setInner(inner);
                    return inner;
                })
                // Then get the remote network to update it if needed and in the same subscription
                .flatMap(inner -> {
                    SubResource networkRef = inner.remoteVirtualNetwork();
                    if (localPeering.isSameSubscription()) {
                        // Update the remote network only if it is in the same subscription
                        return localPeering.manager().networks().getByIdAsync(networkRef.id());
                    } else {
                        // Otherwise, skip this
                        return Maybe.empty();
                    }
                })
                // Then update the existing remote network if needed
                .flatMap(remoteNetwork -> {
                    // Check if any peering is already pointing at this network
                    return remoteNetwork.peerings().listAsync().filter(remotePeering -> (remotePeering != null && remotePeering.remoteNetworkId() != null && remotePeering.remoteNetworkId().equalsIgnoreCase(localPeering.parent.id())))
                            .firstElement()
                            // Depending on the existence of a matching remote peering, create one or update existing
                            .flatMap(remotePeering -> {
                                // Matching peering exists, so update as needed
                                Update remotePeeringUpdate = remotePeering.update();
                                boolean isUpdateNeeded = false;

                                // Update traffic forwarding on the remote peering if needed
                                if (localPeering.remoteForwarding == null) {
                                    // No traffic forwarding change, so ignore
                                } else if (localPeering.remoteForwarding.booleanValue() && !remotePeering.isTrafficForwardingFromRemoteNetworkAllowed()) {
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate = remotePeeringUpdate.withTrafficForwardingFromRemoteNetwork();
                                } else if (!localPeering.remoteForwarding.booleanValue() && remotePeering.isTrafficForwardingFromRemoteNetworkAllowed()) {
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate = remotePeeringUpdate.withoutTrafficForwardingFromRemoteNetwork();
                                }

                                // Update network access on the remote peering if needed
                                if (localPeering.remoteAccess == null) {
                                    // No access change, so ignore
                                } else if (localPeering.remoteAccess.booleanValue() && !((NetworkPeeringImpl) remotePeering).isAccessFromRemoteNetworkAllowed()) {
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate = ((NetworkPeeringImpl) remotePeeringUpdate).withAccessFromRemoteNetwork();
                                } else if (!localPeering.remoteAccess.booleanValue() && ((NetworkPeeringImpl) remotePeering).isAccessFromRemoteNetworkAllowed()) {
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate = ((NetworkPeeringImpl) remotePeeringUpdate).withoutAccessFromRemoteNetwork();
                                }

                                // Update gateway use permission on the remote peering if needed
                                if (localPeering.allowGatewayUseOnRemoteNetwork == null) {
                                    // No change, so ignore
                                } else if (localPeering.allowGatewayUseOnRemoteNetwork.booleanValue() && remotePeering.gatewayUse() != NetworkPeeringGatewayUse.BY_REMOTE_NETWORK) {
                                    // Allow gateway use on remote network
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate.withGatewayUseByRemoteNetworkAllowed();
                                } else if (!localPeering.allowGatewayUseOnRemoteNetwork.booleanValue() && remotePeering.gatewayUse() == NetworkPeeringGatewayUse.BY_REMOTE_NETWORK) {
                                    // Disallow gateway use on remote network
                                    isUpdateNeeded = true;
                                    remotePeeringUpdate.withoutGatewayUseByRemoteNetwork();
                                }

                                // Update gateway use start on the remote peering if needed
                                if (localPeering.startGatewayUseByRemoteNetwork == null) {
                                    // No change, so ignore
                                } else if (localPeering.startGatewayUseByRemoteNetwork.booleanValue() && remotePeering.gatewayUse() != NetworkPeeringGatewayUse.ON_REMOTE_NETWORK) {
                                    remotePeeringUpdate.withGatewayUseOnRemoteNetworkStarted();
                                    isUpdateNeeded = true;
                                } else if (!localPeering.startGatewayUseByRemoteNetwork.booleanValue() && remotePeering.gatewayUse() == NetworkPeeringGatewayUse.ON_REMOTE_NETWORK) {
                                    remotePeeringUpdate.withoutGatewayUseOnRemoteNetwork();
                                    isUpdateNeeded = true;
                                }

                                if (isUpdateNeeded) {
                                    localPeering.remoteForwarding = null;
                                    localPeering.remoteAccess = null;
                                    localPeering.startGatewayUseByRemoteNetwork = null;
                                    localPeering.allowGatewayUseOnRemoteNetwork = null;
                                    return remotePeeringUpdate.applyAsync().lastElement();
                                } else {
                                    return Maybe.just(localPeering);
                                }
                            })
                            // No matching remote peering, so create one on the remote network
                            .switchIfEmpty(Maybe.defer(() -> {
                                String peeringName = SdkContext.randomResourceName("peer", 15);

                                WithCreate remotePeeringDefinition = remoteNetwork.peerings().define(peeringName)
                                        .withRemoteNetwork(localPeering.parent.id());

                                // Process remote network's UseRemoteGateways setting
                                if (localPeering.startGatewayUseByRemoteNetwork == null) {
                                    // Do nothing
                                } else if (localPeering.startGatewayUseByRemoteNetwork.booleanValue()) {
                                    // Start gateway use on this network by the remote network
                                    remotePeeringDefinition.withGatewayUseOnRemoteNetworkStarted();
                                }

                                // Process remote network's AllowGatewayTransit setting
                                if (localPeering.allowGatewayUseOnRemoteNetwork == null) {
                                    // Do nothing
                                } else if (localPeering.allowGatewayUseOnRemoteNetwork.booleanValue()) {
                                    // Allow gateway use on remote network
                                    remotePeeringDefinition.withGatewayUseByRemoteNetworkAllowed();
                                }

                                if (localPeering.remoteAccess != null && !localPeering.remoteAccess) {
                                    ((NetworkPeeringImpl) remotePeeringDefinition).withoutAccessFromRemoteNetwork(); // Assumes by default access is on for new peerings
                                }

                                if (localPeering.remoteForwarding != null && localPeering.remoteForwarding.booleanValue()) {
                                    remotePeeringDefinition.withTrafficForwardingFromRemoteNetwork(); // Assumes by default forwarding is off for new peerings
                                }

                                localPeering.remoteAccess = null;
                                localPeering.remoteForwarding = null;
                                localPeering.startGatewayUseByRemoteNetwork = null;
                                localPeering.allowGatewayUseOnRemoteNetwork = null;
                                return remotePeeringDefinition
                                        .createAsync()
                                        .lastElement().map(indexable -> (NetworkPeering) indexable);
                            }));
                })
                // Then refresh the parent local network, if available
                .flatMapCompletable(remotePeering -> {
                    if (localPeering.parent != null) {
                        return localPeering.parent.refreshAsync().flatMapCompletable(o -> Completable.complete());
                    } else {
                        return Completable.complete();
                    }
                })
                .andThen(Completable.defer(() -> {
                    if (localPeering.remoteNetwork != null && localPeering.isSameSubscription()) {
                        return localPeering.remoteNetwork.refreshAsync().flatMapCompletable(o -> Completable.complete());
                    } else {
                        return Completable.complete();
                    }
                }))
                .andThen(Maybe.just((NetworkPeering) localPeering))
                .toObservable();
    }

    @Override
    protected Maybe<VirtualNetworkPeeringInner> getInnerAsync() {
        this.remoteNetwork = null;
        return this.manager().inner().virtualNetworkPeerings().getAsync(
                this.resourceGroupName(),
                ResourceUtils.nameFromResourceId(this.networkId()), this.inner().name());
    }

    @Override
    public Network getRemoteNetwork() {
        return this.getRemoteNetworkAsync().blockingLast();
    }

    @Override
    public Observable<Network> getRemoteNetworkAsync() {
        final NetworkPeeringImpl self = this;
        if (self.remoteNetwork != null) {
            return Observable.just(self.remoteNetwork);
        } else if (this.isSameSubscription()) {
            // Fetch the remote network if within the same subscription
            return this.manager().networks().getByIdAsync(this.remoteNetworkId())
                    .map(network -> {
                        self.remoteNetwork = network;
                        return network;
                    })
                    .toObservable();
        } else {
            // Otherwise bail out
            self.remoteNetwork = null;
            return Observable.empty();
        }
    }

    @Override
    public NetworkPeering getRemotePeering() {
        Network network = this.getRemoteNetwork();
        return (network != null) ? network.peerings().getByRemoteNetwork(this.networkId()) : null;
    }

    @Override
    public Observable<NetworkPeering> getRemotePeeringAsync() {
        final NetworkPeeringImpl self = this;
        return this.getRemoteNetworkAsync()
                .flatMap(remoteNetwork -> remoteNetwork.peerings().getByRemoteNetworkAsync(self.networkId()));
    }

    @Override
    public NetworkPeeringGatewayUse gatewayUse() {
        if (Utils.toPrimitiveBoolean(this.inner().allowGatewayTransit())) {
            return NetworkPeeringGatewayUse.BY_REMOTE_NETWORK;
        } else if (Utils.toPrimitiveBoolean(this.inner().useRemoteGateways())) {
            return NetworkPeeringGatewayUse.ON_REMOTE_NETWORK;
        } else {
            return NetworkPeeringGatewayUse.NONE;
        }
    }
}