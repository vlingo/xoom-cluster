# vlingo-cluster
This is a very early stage release of the vlingo platform.

The vlingo-cluster supports scaling a JVM tool or application with fault tolerance.

To build and test, make sure that you also build and install the following in the order shown:

consule:vlingo-common> mvn install

consule:vlingo-actors> mvn install

consule:vlingo-wire> mvn install

consule:vlingo-cluster> mvn install

The cluster is pre-configured for three nodes. To run a three-node cluster, start three different console windows and run the following, one in each console:

consule1:vlingo-cluster> mvn exec:java -Dexec.args=node1

consule2:vlingo-cluster> mvn exec:java -Dexec.args=node2

consule3:vlingo-cluster> mvn exec:java -Dexec.args=node3

Each node will start up and join the cluster. It is likely that node3 will become the leader, but it's possible for another node to take the lead depending on timing. Which node is the leader isn't really important, but fairly predictive, again assuming that node3 isn't started "long" after node2 and node1.

To play around with the nodes, go into one consult window and type ^C (interrupt). This will bring down the given node and
the cluster will adjust. If you bring down the leader the cluster will elect a new leader. Starting the downed node again will
enable it to re-join the cluster.

You can develop JVM applications with vlingo-cluster. See the following package and sample ClusterApplication:

```  io.vlingo.cluster.model.application
    ClusterApplication
    ClusterApplicationAdapter
    FakeClusterApplicationActor```

Try it out. As the cluster changes states all of the activity and health conditions will be reported to the registered ClusterApplication. You can see how to configure your own ClusterApplication by changing the vlingo-cluster.properties file:

```  cluster.app.class = io.vlingo.cluster.model.application.FakeClusterApplicationActor```

Have fun!


License (See LICENSE file for full license)
-------------------------------------------
Copyright © 2012-2018 Vaughn Vernon. All rights reserved.

This Source Code Form is subject to the terms of the
Mozilla Public License, v. 2.0. If a copy of the MPL
was not distributed with this file, you can obtain
one at https://mozilla.org/MPL/2.0/.
