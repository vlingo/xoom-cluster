// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import io.vlingo.actors.Definition;
import io.vlingo.actors.testkit.TestActor;
import io.vlingo.cluster.model.message.MessageConverters;
import io.vlingo.cluster.model.message.Pulse;
import io.vlingo.cluster.model.node.MergeResult;
import io.vlingo.cluster.model.node.RegistryInterest;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.message.Converters;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Node;

public class ClusterSnapshotActorTest extends AbstractClusterTest {
  private RawMessage opMessage;
  private ClusterSnapshotInitializer intializer;
  
  @Test
  public void testClusterSnapshot() throws Exception {
    final TestActor<ClusterSnapshot> snapshot =
            testWorld.actorFor(
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
            testWorld.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    ClusterSnapshotControl.class);
    
    control.actor().shutDown();
    assertEquals(1, application.stop);
  }

  @Test
  public void testInboundStreamInterest() throws Exception {
    final TestActor<InboundStreamInterest> inboundStreamInterest =
            testWorld.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    InboundStreamInterest.class);

    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.OP, opMessage, null);
    assertEquals(0, application.handleApplicationMessage);
    
    final RawMessage appMessage = RawMessage.from(1, 0, "app-test");
    inboundStreamInterest.actor().handleInboundStreamMessage(AddressType.APP, appMessage, null);
    assertEquals(1, application.handleApplicationMessage);
  }

  @Test
  public void testRegistryInterest() throws Exception {
    final TestActor<RegistryInterest> registryInterest =
            testWorld.actorFor(
                    Definition.has(ClusterSnapshotActor.class, Definition.parameters(intializer, application)),
                    RegistryInterest.class);
    
    registryInterest.actor().informAllLiveNodes(config.allNodes(), true);
    assertEquals(1, application.allLiveNodes);
    
    registryInterest.actor().informConfirmedByLeader(config.nodeMatching(Id.of(1)), true);
    assertEquals(1, application.informNodeIsHealthy);
    
    registryInterest.actor().informCurrentLeader(config.nodeMatching(Id.of(3)), true);
    assertEquals(1, application.informLeaderElected);
    
    List<Node> nodes = new ArrayList<>();
    nodes.add(config.nodeMatching(Id.of(3)));
    List<MergeResult> mergeResult = new ArrayList<>();
    mergeResult.add(new MergeResult(config.nodeMatching(Id.of(2)), true));
    mergeResult.add(new MergeResult(config.nodeMatching(Id.of(1)), false));
    registryInterest.actor().informMergedAllDirectoryEntries(nodes, mergeResult, true);
    assertEquals(1, application.informNodeJoinedCluster);
    assertEquals(1, application.informNodeLeftCluster);
    
    registryInterest.actor().informLeaderDemoted(config.nodeMatching(Id.of(2)), true);
    assertEquals(1, application.informLeaderLost);
    
    registryInterest.actor().informNodeIsHealthy(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeIsHealthy);
    
    registryInterest.actor().informNodeJoinedCluster(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeJoinedCluster);
    
    registryInterest.actor().informNodeLeftCluster(config.nodeMatching(Id.of(2)), true);
    assertEquals(2, application.informNodeLeftCluster);
    
    registryInterest.actor().informNodeTimedOut(config.nodeMatching(Id.of(2)), true);
    assertEquals(3, application.informNodeLeftCluster);
  }
  
  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    intializer = new ClusterSnapshotInitializer("node1", properties, testWorld.defaultLogger());
    
    final ByteBuffer messageBuffer = ByteBuffer.allocate(4096);
    final Pulse pulse = new Pulse(Id.of(1));
    MessageConverters.messageToBytes(pulse, messageBuffer);
    opMessage = Converters.toRawMessage(Id.of(1).value(), messageBuffer);
  }
  
  @After
  public void tearDown() {
    super.tearDown();
  }
}
