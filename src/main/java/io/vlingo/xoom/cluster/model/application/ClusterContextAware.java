package io.vlingo.xoom.cluster.model.application;

import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Node;

import java.util.Collection;

public interface ClusterContextAware {
  void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster);
  void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster);
  void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster);
  void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster);
  void informClusterIsHealthy(final boolean isHealthyCluster);
}
