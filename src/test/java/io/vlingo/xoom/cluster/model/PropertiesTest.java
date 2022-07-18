// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class PropertiesTest extends AbstractClusterTest {

  @Test
  public void testApplicationBufferSize() {
    assertEquals(10240, properties.applicationBufferSize());
  }
  
  @Test
  public void testApplicationOutgoingPooledBuffers() {
    assertEquals(50, properties.applicationOutgoingPooledBuffers());
  }

  @Test
  public void testClusterApplicationClass() {
    assertNotNull(properties.clusterApplicationClassname());
    assertNotNull(properties.clusterApplicationClass());
  }

  @Test
  public void testClusterHealthCheckInterval() {
    assertEquals(2000, properties.clusterHealthCheckInterval());
  }

  @Test
  public void testOperationalBufferSize() {
    assertEquals(4096, properties.operationalBufferSize());
  }

  @Test
  public void testOperationalOutgoingPooledBuffers() {
    assertEquals(20, properties.operationalOutgoingPooledBuffers());
  }

  @Test
  public void testUseSSL() {
    assertFalse(properties.useSSL());
  }
}
