// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import io.vlingo.cluster.model.node.Address;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;
import io.vlingo.cluster.model.node.Name;
import io.vlingo.cluster.model.node.Node;

public class ClusterConfigurationTest extends AbstractClusterTest {
  @Test
  public void testAllConfiguredNodes() throws Exception {
    final Collection<Node> all = config.allConfiguredNodes();

    assertEquals(3, all.size());

    final Set<Node> nodes = new HashSet<>();
    nodes.add(new Node(Id.of(1), new Name("node1"), Address.from("localhost", 37371, AddressType.OP), Address.from("localhost", 37372, AddressType.APP)));
    nodes.add(new Node(Id.of(2), new Name("node2"), Address.from("localhost", 37373, AddressType.OP), Address.from("localhost", 37374, AddressType.APP)));
    nodes.add(new Node(Id.of(3), new Name("node3"), Address.from("localhost", 37375, AddressType.OP), Address.from("localhost", 37376, AddressType.APP)));

    for (final Node node : all) {
      nodes.remove(node);
    }

    assertEquals(0, nodes.size());
  }

  @Test
  public void testAllConfiguredNodeNames() throws Exception {
    final Collection<String> all = config.allConfiguredNodeNames();

    assertEquals(3, all.size());

    final Set<String> allNames = new HashSet<>();
    allNames.add("node1");
    allNames.add("node2");
    allNames.add("node3");

    for (final String nodeName : all) {
      allNames.remove(nodeName);
    }

    assertEquals(0, allNames.size());
  }
  
  @Test
  public void testAllOtherConfiguredNodes() throws Exception {
    assertEquals(2, config.allOtherConfiguredNodes(Id.of(1)).size());
    assertEquals(2, config.allOtherConfiguredNodes(Id.of(2)).size());
    assertEquals(2, config.allOtherConfiguredNodes(Id.of(3)).size());
    assertEquals(3, config.allOtherConfiguredNodes(Id.of(4)).size());
  }
  
  @Test
  public void testAllGreaterConfiguredNodes() throws Exception {
    assertEquals(3, config.allGreaterConfiguredNodes(Id.of(0)).size());
    assertEquals(2, config.allGreaterConfiguredNodes(Id.of(1)).size());
    assertEquals(1, config.allGreaterConfiguredNodes(Id.of(2)).size());
    assertEquals(0, config.allGreaterConfiguredNodes(Id.of(3)).size());
  }
  
  @Test
  public void testConfiguredNodeMatching() throws Exception {
    assertEquals(Node.NO_NODE, config.configuredNodeMatching(Id.of(0)));
    assertEquals("node1", config.configuredNodeMatching(Id.of(1)).name().value());
    assertEquals("node2", config.configuredNodeMatching(Id.of(2)).name().value());
    assertEquals("node3", config.configuredNodeMatching(Id.of(3)).name().value());
    assertEquals(Node.NO_NODE, config.configuredNodeMatching(Id.of(4)));
  }
  
  @Test
  public void testGreatestConfiguredNodeId() throws Exception {
    assertEquals(Id.of(3), config.greatestConfiguredNodeId());
  }
  
  @Test
  public void testHasConfiguredNode() throws Exception {
    assertFalse(config.hasConfiguredNode(Id.of(0)));
    assertTrue(config.hasConfiguredNode(Id.of(1)));
    assertTrue(config.hasConfiguredNode(Id.of(2)));
    assertTrue(config.hasConfiguredNode(Id.of(3)));
    assertFalse(config.hasConfiguredNode(Id.of(4)));
  }
  
  @Test
  public void testTotalConfiguredNodes() throws Exception {
    assertEquals(3, config.totalConfiguredNodes());
  }
}
