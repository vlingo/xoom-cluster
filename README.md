# xoom-cluster

[![Javadocs](http://javadoc.io/badge/io.vlingo.xoom/xoom-cluster.svg?color=brightgreen)](http://javadoc.io/doc/io.vlingo.xoom/xoom-cluster) [![Build](https://github.com/vlingo/xoom-cluster/workflows/Build/badge.svg)](https://github.com/vlingo/xoom-cluster/actions?query=workflow%3ABuild) [![Download](https://img.shields.io/maven-central/v/io.vlingo.xoom/xoom-cluster?label=maven)](https://search.maven.org/artifact/io.vlingo.xoom/xoom-cluster) [![Gitter chat](https://badges.gitter.im/gitterHQ/gitter.png)](https://gitter.im/vlingo-platform-java/cluster)

The VLINGO XOOM platform SDK cluster management for Reactive, scalable resiliency of JVM tools and applications running on XOOM LATTICE and XOOM ACTORS.

Docs: https://docs.vlingo.io/xoom-cluster

### Installation

```xml
  <dependencies>
    <dependency>
      <groupId>io.vlingo.xoom</groupId>
      <artifactId>xoom-cluster</artifactId>
      <version>1.11.1</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```

```gradle
dependencies {
    compile 'io.vlingo.xoom:xoom-cluster:1.11.1'
}
```

The XOOM CLUSTER supports scaling a JVM-based DDD Bounded Context (business-driven microservice) with fault tolerance. 

Normally you would use `xoom-lattice` to get these clustering features, but you may run the build-in example cluster application to see how it works. To do so, reference your `xoom-*` dependecies, build, and run. The cluster is pre-configured for three nodes. To run a three-node cluster, start three different console windows and run the following, one in each console:

```
console1:xoom-cluster> mvn exec:java -Dexec.args=node1

console2:xoom-cluster> mvn exec:java -Dexec.args=node2

console3:xoom-cluster> mvn exec:java -Dexec.args=node3
```

Each node will start up and join the cluster. It is likely that node3 will become the leader, but it's possible for another node to take the lead depending on timing. Which node is the leader isn't really important, but fairly predictive, again assuming that node3 isn't started "long" after node2 and node1.

To play around with the nodes, go into one consult window and type `^C` (interrupt). This will bring down the given node and
the cluster will adjust. If you bring down the leader the cluster will elect a new leader. Starting the downed node again will
enable it to re-join the cluster.

See the following package and sample `ClusterApplication`:

```java
  io.vlingo.xoom.cluster.model.application
    ClusterApplication
    ClusterApplicationAdapter
    FakeClusterApplicationActor
```

Try it out. As the cluster changes states all of the activity and health conditions will be reported to the registered `ClusterApplication`. You can see how to configure your own `ClusterApplication` by changing the `xoom-cluster.properties` file:

```java
  cluster.app.class = io.vlingo.xoom.cluster.model.application.FakeClusterApplicationActor
```

Have fun!

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2023 VLINGO LABS. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
