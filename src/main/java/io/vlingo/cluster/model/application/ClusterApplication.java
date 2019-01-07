// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.application;

import java.util.Collection;

import io.vlingo.actors.Actor;
import io.vlingo.actors.Definition;
import io.vlingo.actors.Stage;
import io.vlingo.actors.Startable;
import io.vlingo.actors.Stoppable;
import io.vlingo.actors.World;
import io.vlingo.cluster.model.Properties;
import io.vlingo.cluster.model.attribute.AttributesProtocol;
import io.vlingo.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public interface ClusterApplication extends Startable, Stoppable {
  public static ClusterApplication instance(final World world, final Node node) {
    final Class<? extends Actor> clusterApplicationActor =
            Properties.instance.clusterApplicationClass();
    
    final Stage applicationStage =
            world.stageNamed(Properties.instance.clusterApplicationStageName());
    
    return applicationStage.actorFor(
            ClusterApplication.class,
            Definition.has(clusterApplicationActor, Definition.parameters(node), "cluster-application"));
  }

  void handleApplicationMessage(final RawMessage message, final ApplicationOutboundStream responder);
  
  void informAllLiveNodes(final Collection<Node> liveNodes, final boolean isHealthyCluster);
  void informLeaderElected(final Id leaderId, final boolean isHealthyCluster, final boolean isLocalNodeLeading);
  void informLeaderLost(final Id lostLeaderId, final boolean isHealthyCluster);
  void informLocalNodeShutDown(final Id nodeId);
  void informLocalNodeStarted(final Id nodeId);
  void informNodeIsHealthy(final Id nodeId, final boolean isHealthyCluster);
  void informNodeJoinedCluster(final Id nodeId, final boolean isHealthyCluster);
  void informNodeLeftCluster(final Id nodeId, final boolean isHealthyCluster);
  void informQuorumAchieved();
  void informQuorumLost();

  void informAttributesClient(final AttributesProtocol client);
  void informAttributeSetCreated(final String attributeSetName);
  void informAttributeAdded(final String attributeSetName, final String attributeName);
  void informAttributeRemoved(final String attributeSetName, final String attributeName);
  void informAttributeSetRemoved(final String attributeSetName);
  void informAttributeReplaced(final String attributeSetName, final String attributeName);
}
