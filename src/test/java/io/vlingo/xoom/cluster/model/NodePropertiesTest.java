// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import org.junit.Assert;
import org.junit.Test;

public class NodePropertiesTest {
  @Test
  public void serializationTest() {
    final String node1Text = "1:node1:localhost:17171:17172:false";
    final String node2Text = "2:node2:localhost:17173:17174:true";

    NodeProperties node1Properties = NodeProperties.from(node1Text);
    Assert.assertEquals(node1Text, node1Properties.asText());

    NodeProperties node2Properties = NodeProperties.from(node2Text);
    Assert.assertEquals(node2Text, node2Properties.asText());
  }
}
