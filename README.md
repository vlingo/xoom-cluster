# vlingo-cluster

[![Javadocs](http://javadoc.io/badge/io.vlingo/vlingo-cluster.svg?color=brightgreen)](http://javadoc.io/doc/io.vlingo/vlingo-cluster) [![Build Status](https://travis-ci.org/vlingo/vlingo-cluster.svg?branch=master)](https://travis-ci.org/vlingo/vlingo-cluster) [ ![Download](https://api.bintray.com/packages/vlingo/vlingo-platform-java/vlingo-cluster/images/download.svg) ](https://bintray.com/vlingo/vlingo-platform-java/vlingo-cluster/_latestVersion)

### Bintray

```xml
  <repositories>
    <repository>
      <id>jcenter</id>
      <url>https://jcenter.bintray.com/</url>
    </repository>
  </repositories>
  <dependencies>
    <dependency>
      <groupId>io.vlingo</groupId>
      <artifactId>vlingo-cluster</artifactId>
      <version>0.7.6</version>
      <scope>compile</scope>
    </dependency>
  </dependencies>
```

```gradle
dependencies {
    compile 'io.vlingo:vlingo-cluster:0.7.6'
}

repositories {
    jcenter()
}
```

The **vlingo/cluster** supports scaling a JVM-based DDD Bounded Context (business-driven microservice) with fault tolerance. It has undergone much refinement and has reached version 0.3.3, and is very near minimum-viable feature completion.

To build and test, simply use the **vlingo-platform** build.:

```
console:vlingo> git clone [all available repositories]

console:vlingo> cd vlingo-platform

console:vlingo-platform> mvn install

console:vlingo-platform> cd ../vlingo-cluster

console:vlingo-cluster>
```

The cluster is pre-configured for three nodes. To run a three-node cluster, start three different console windows and run the following, one in each console:

```
console1:vlingo-cluster> mvn exec:java -Dexec.args=node1

console2:vlingo-cluster> mvn exec:java -Dexec.args=node2

console3:vlingo-cluster> mvn exec:java -Dexec.args=node3
```

Each node will start up and join the cluster. It is likely that node3 will become the leader, but it's possible for another node to take the lead depending on timing. Which node is the leader isn't really important, but fairly predictive, again assuming that node3 isn't started "long" after node2 and node1.

To play around with the nodes, go into one consult window and type `^C` (interrupt). This will bring down the given node and
the cluster will adjust. If you bring down the leader the cluster will elect a new leader. Starting the downed node again will
enable it to re-join the cluster.

You can develop JVM applications with **vlingo/cluster**. See the following package and sample ClusterApplication:

```java
  io.vlingo.cluster.model.application
    ClusterApplication
    ClusterApplicationAdapter
    FakeClusterApplicationActor
```

Try it out. As the cluster changes states all of the activity and health conditions will be reported to the registered ClusterApplication. You can see how to configure your own ClusterApplication by changing the vlingo-cluster.properties file:

```java
  cluster.app.class = io.vlingo.cluster.model.application.FakeClusterApplicationActor
```

Have fun!

License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2018 Vaughn Vernon. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, You can obtain
one at https://mozilla.org/MPL/2.0/.
