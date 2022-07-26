package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.wire.fdx.outbound.rsocket.ManagedOutboundRSocketChannelProvider;
import io.vlingo.xoom.wire.node.Node;

class OutboundChannelInterest implements ClusterMembershipInterest {
  private final ManagedOutboundRSocketChannelProvider outboundChannelProvider;

  OutboundChannelInterest(ManagedOutboundRSocketChannelProvider outboundChannelProvider) {
    this.outboundChannelProvider = outboundChannelProvider;
  }

  @Override
  public void nodeAdded(Node node, boolean isClusterHealthy) {
    // outboundChannelProvider is creating channels on demand; still, initiate the channel creation:
    outboundChannelProvider.channelFor(node);
  }

  @Override
  public void nodeLeft(Node node, boolean isClusterHealthy) {
    outboundChannelProvider.close(node.id());
  }

  @Override
  public void informClusterIsHealthy(boolean isClusterHealthy) {
    // nothing for now
  }
}
