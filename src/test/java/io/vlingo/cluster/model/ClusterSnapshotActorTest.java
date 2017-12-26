// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.actors.testkit.TestWorld;
import io.vlingo.cluster.model.inbound.InboundStreamInterest;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.MergeResult;
import io.vlingo.cluster.model.node.Node;
import io.vlingo.cluster.model.node.RegistryInterest;
import io.vlingo.common.message.RawMessage;

public class ClusterSnapshotActorTest extends AbstractClusterTest {
  private ClusterSnapshotInitializer intializer;
  private TestWorld world;
  
  @Test
  public void testClusterSnapshot() throws Exception {
    final TestActor<ClusterSnapshot> snapshot =
            world.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    ClusterSnapshot.class);
    
    snapshot.actor().quorumAchieved();
    assertEquals(1, application.informQuorumAchieved);
    
    snapshot.actor().quorumLost();
    assertEquals(1, application.informQuorumLost);
  }

  @Test
  public void testClusterSnapshotControl() throws Exception {
    final TestActor<ClusterSnapshotControl> control =
            world.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    ClusterSnapshotControl.class);
    
    control.actor().shutDown();
    assertEquals(1, application.stop);
  }

  @Test
  public void testInboundStreamInterest() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            world.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    InboundStreamInterest.class);

    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, new RawMessage(32), null);
    assertEquals(0, application.handleApplicationMessage);
    
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.APP, new RawMessage(32), null);
    assertEquals(1, application.handleApplicationMessage);
  }

  @Test
  public void testRegistryInterest() throws Exception {
    final TestActor<RegistryInterest> registryInterest =
            world.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    RegistryInterest.class);
    
    registryInterest.actor().informAllLiveNodes(config.allConfiguredNodes(), true);
    assertEquals(1, application.allLiveNodes);
    
    registryInterest.actor().informConfirmedByLeader(config.configuredNodeMatching(Id.of(1)), true);
    assertEquals(1, application.informNodeIsHealthy);
    
    registryInterest.actor().informCurrentLeader(config.configuredNodeMatching(Id.of(3)), true);
    assertEquals(1, application.informLeaderElected);
    
    List<Node> nodes = new ArrayList<>();
    nodes.add(config.configuredNodeMatching(Id.of(3)));
    List<MergeResult> mergeResult = new ArrayList<>();
    mergeResult.add(new MergeResult(config.configuredNodeMatching(Id.of(2)), true));
    mergeResult.add(new MergeResult(config.configuredNodeMatching(Id.of(1)), false));
    registryInterest.actor().informMergedAllDirectoryEntries(nodes, mergeResult, true);
    assertEquals(1, application.informNodeJoinedCluster);
    assertEquals(1, application.informNodeLeftCluster);
    
    registryInterest.actor().informLeaderDemoted(config.configuredNodeMatching(Id.of(2)), true);
    assertEquals(1, application.informLeaderLost);
    
    registryInterest.actor().informNodeIsHealthy(config.configuredNodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeIsHealthy);
    
    registryInterest.actor().informNodeJoinedCluster(config.configuredNodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeJoinedCluster);
    
    registryInterest.actor().informNodeLeftCluster(config.configuredNodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeLeftCluster);
    
    registryInterest.actor().informNodeTimedOut(config.configuredNodeMatching(Id.of(2)), true);
    assertEquals(3, application.informNodeLeftCluster);
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    world = TestWorld.start("test-cluster-snapshot-actor");
    
    intializer = new ClusterSnapshotInitializer("node1", properties);
  }
}
