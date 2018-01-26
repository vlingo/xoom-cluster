// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.wire.node.Address;
import io.vlingo.wire.node.AddressType;
import io.vlingo.wire.node.Host;
import io.vlingo.wire.node.Id;
import io.vlingo.wire.node.Name;
import io.vlingo.wire.node.Node;

public class LocalRegistryTest extends AbstractClusterTest {

  @Test
  public void testNoLiveNodes() {
    final LocalRegistry registry = new LocalRegistry(config.nodeMatching(Id.of(3)), config, testWorld.defaultLogger());
    assertTrue(registry.liveNodes().isEmpty());
  }
  
  @Test
  public void testCleanTimedOutNodes() {
    final LocalRegistry registry = join3Nodes();
    
    assertEquals(3, registry.liveNodes().size());
    
    registry.updateLastHealthIndication(Id.of(1));
    
    registry.registeredNodeStatusOf(Id.of(1)).setLastHealthIndication(System.currentTimeMillis() - 70001);
    
    registry.cleanTimedOutNodes();
    
    assertEquals(2, registry.liveNodes().size());
  }
  
  @Test
  public void testConfirmAllLiveNodesByLeader() {
    final LocalRegistry registry = join3Nodes();
    
    registry.confirmAllLiveNodesByLeader();
    
    for (final Node node : registry.liveNodes()) {
      assertTrue(registry.isConfirmedByLeader(node.id()));
    }
    
    assertFalse(registry.isConfirmedByLeader(idOf(4)));
  }
  
  @Test
  public void testDeclareLeaderHasLeader() {
    final LocalRegistry registry = join3Nodes();
    
    final Id id1 = idOf(1);
    final Id id2 = idOf(2);
    final Id id3 = idOf(3);
    
    registry.declareLeaderAs(id3);
    
    assertTrue(registry.hasLeader());
    
    assertFalse(registry.isLeader(id1));
    assertFalse(registry.isLeader(id2));
    assertTrue(registry.isLeader(id3));
    assertFalse(registry.isLeader(idOf(4)));
  }
  
  @Test
  public void testDemoteLeaderHasLeader() {
    final LocalRegistry registry = join3Nodes();
    
    final Id id3 = idOf(3);
    
    registry.declareLeaderAs(id3);
    
    assertTrue(registry.hasLeader());
    assertTrue(registry.isLeader(id3));
    
    registry.demoteLeaderOf(id3);
    
    assertFalse(registry.hasLeader());
    assertFalse(registry.isLeader(id3));
  }
  
  @Test
  public void testHasLeader() {
    final LocalRegistry registry = join3Nodes();
    
    final Id id3 = idOf(3);
    
    registry.declareLeaderAs(id3);
    assertTrue(registry.hasLeader());

    registry.demoteLeaderOf(id3);
    assertFalse(registry.hasLeader());
  }
  
  @Test
  public void testIsLeader() {
    final LocalRegistry registry = join3Nodes();
    
    final Id id3 = idOf(3);
    
    registry.declareLeaderAs(id3);
    assertTrue(registry.isLeader(id3));
    assertFalse(registry.isLeader(idOf(2)));
  }
  
  @Test
  public void testHasMember() {
    final LocalRegistry registry = join3Nodes();
    
    assertTrue(registry.hasMember(idOf(1)));
    assertTrue(registry.hasMember(idOf(2)));
    assertTrue(registry.hasMember(idOf(3)));
    assertFalse(registry.hasMember(idOf(4)));
  }
  
  @Test
  public void testHasQuorum() {
    final LocalRegistry registry = join3Nodes();
    assertTrue(registry.hasQuorum());
    
    registry.leave(idOf(2));
    assertTrue(registry.hasQuorum());
    
    registry.leave(idOf(3));
    assertFalse(registry.hasQuorum());
  }
  
  @Test
  public void testJoin() {
    final LocalRegistry registry = join3Nodes();
    
    registry.join(nodeOf(4));
    
    assertEquals(4, registry.liveNodes().size());
    
    assertTrue(registry.hasMember(idOf(1)));
    assertTrue(registry.hasMember(idOf(2)));
    assertTrue(registry.hasMember(idOf(3)));
    assertTrue(registry.hasMember(idOf(4)));
  }

  @Test
  public void testLeave() {
    final LocalRegistry registry = join3Nodes();
    
    assertTrue(registry.hasMember(idOf(1)));
    assertTrue(registry.hasMember(idOf(2)));
    assertTrue(registry.hasMember(idOf(3)));
    
    registry.leave(idOf(2));
    
    assertTrue(registry.hasMember(idOf(1)));
    assertFalse(registry.hasMember(idOf(2)));
    assertTrue(registry.hasMember(idOf(3)));
  }
  
  @Test
  public void testMergeAllDirectoryEntries() {
    final LocalRegistry registry = join3Nodes();
    
    final List<Node> leaderRegisteredNodesToMerge = new ArrayList<Node>();
    
    leaderRegisteredNodesToMerge.add(nodeOf(1));
    leaderRegisteredNodesToMerge.add(nodeOf(2));
    leaderRegisteredNodesToMerge.add(nodeOf(4));
    
    final MockRegistryInterest interest = new MockRegistryInterest();
    
    registry.registerRegistryInterest(interest);
    
    registry.mergeAllDirectoryEntries(leaderRegisteredNodesToMerge);
    
    assertEquals(1, interest.informMergedAllDirectoryEntries);
    assertEquals(3, interest.liveNodes.size());  // 1, 2, 4
    assertEquals(2, interest.mergeResults.size()); // 4 joined, 3 left
    
    final Iterator<MergeResult> iterator = interest.mergeResults.iterator();
    
    final MergeResult inspectable1 = iterator.next();
    final MergeResult inspectable2 = iterator.next();
    
    assertEquals(idOf(3), inspectable1.node().id());
    assertTrue(inspectable1.left());
    
    assertEquals(idOf(4), inspectable2.node().id());
    assertTrue(inspectable2.joined());
  }
  
  @Test
  public void testPromoteElectedLeader() {
    final LocalRegistry registry = join3Nodes();
    
    final MockRegistryInterest interest = new MockRegistryInterest();
    
    registry.registerRegistryInterest(interest);

    registry.join(nodeOf(4));
    
    registry.declareLeaderAs(idOf(3));
    
    assertEquals(1, interest.informCurrentLeader);
    
    registry.promoteElectedLeader(idOf(4));
    
    assertEquals(1, interest.informLeaderDemoted);
    assertEquals(3, interest.informCurrentLeader);
    assertEquals(4, registry.currentLeader().id().value());
  }
  
  @Test
  public void testUpdateLastHealthIndication() {
    final LocalRegistry registry = join3Nodes();
    final long minTime = System.currentTimeMillis();
    final long maxTime = minTime + 100L;
    
    for (final Node node : registry.liveNodes()) {
      registry.updateLastHealthIndication(node.id());
    }
    
    for (final Node healthyNode : registry.liveNodes()) {
      final RegisteredNodeStatus status = registry.registeredNodeStatusOf(healthyNode.id());
      final long lastHealthIndication = status.lastHealthIndication();
      assertTrue(lastHealthIndication >= minTime && lastHealthIndication <= maxTime);
    }
  }
  
  private Id idOf(final int idValue) {
    return Id.of(idValue);
  }
  
  // Note: join() is tested by nearly every test
  
  private LocalRegistry join3Nodes() {
    final LocalRegistry registry = new LocalRegistry(config.nodeMatching(Id.of(3)), config, testWorld.defaultLogger());
    final Node node1 = nodeOf(1);
    final Node node2 = nodeOf(2);
    final Node node3 = nodeOf(3);

    registry.join(node1);
    registry.join(node2);
    registry.join(node3);
    
    return registry;
  }
  
  private Node nodeOf(final int idValue) {
    final Id id = Id.of(idValue);
    final Name name = new Name("node" + idValue);
    final Address opAddress = new Address(Host.of("localhost"), 1111+idValue, AddressType.OP);
    final Address appAddress = new Address(Host.of("localhost"), 1111+idValue+1, AddressType.APP);
    final Node node = new Node(id, name, opAddress, appAddress);
    
    return node;
  }
}
