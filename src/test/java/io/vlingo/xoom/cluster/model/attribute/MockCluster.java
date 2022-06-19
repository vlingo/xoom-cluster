package io.vlingo.xoom.cluster.model.attribute;

import io.scalecube.cluster.Cluster;
import io.scalecube.cluster.Member;
import io.scalecube.cluster.transport.api.Message;
import io.scalecube.net.Address;
import io.vlingo.xoom.cluster.model.ClusterConfiguration;
import io.vlingo.xoom.cluster.model.node.Registry;
import io.vlingo.xoom.wire.node.Node;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class MockCluster {
  final Cluster cluster;
  public final Map<Address, Integer> sentMessages;

  MockCluster(Node localNode, ClusterConfiguration configuration) {
    this.cluster = Mockito.mock(Cluster.class);
    this.sentMessages = new HashMap<>();
    for (Node otherNode : configuration.allNodes()) {
      Address address = Address.create(otherNode.operationalAddress().hostName(), otherNode.operationalAddress().port());
      Member member = new Member(otherNode.id().valueString(), otherNode.name().value(), address, "namespace");

      sentMessages.put(address, 0);

      when(cluster.member(address))
              .thenReturn(Optional.of(member));

      when(cluster.send(eq(member), any(Message.class)))
              .then(val -> {
                Integer counter = this.sentMessages.get(address);
                this.sentMessages.put(address, counter + 1);
                return Mono.empty();
              });
    }
  }

  Registry mockHealthyRegistry(Node localNode) {
    Registry mockRegistry = Mockito.mock(Registry.class);

    when(mockRegistry.isClusterHealthy())
            .thenReturn(true);

    when(mockRegistry.localNode())
            .thenReturn(localNode);

    return mockRegistry;
  }

  int messagesTo(Node node) {
    Address address = Address.create(node.operationalAddress().hostName(), node.operationalAddress().port());
    return sentMessages.get(address);
  }
}
