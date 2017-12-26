// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.cluster.model.AbstractClusterTest;
import io.vlingo.cluster.model.node.AddressType;
import io.vlingo.cluster.model.node.Id;

public class ManagedOutboundSocketChannelProviderTest extends AbstractClusterTest {
  private ManagedOutboundSocketChannelProvider provider;
  
  @Test
  public void testProviderProvides() throws Exception {
    assertEquals(2, provider.allOtherNodeChannels().size());
    
    assertNotNull(provider.channelFor(Id.of(2)));
    
    assertNotNull(provider.channelFor(Id.of(3)));
    
    assertEquals(2, provider.channelsFor(config.allOtherConfiguredNodes(Id.of(1))).size());
  }

  @Test
  public void testProviderCloseAllReopen() throws Exception {
    provider.close();
    
    assertNotNull(provider.channelFor(Id.of(3)));
    assertNotNull(provider.channelFor(Id.of(2)));
    assertNotNull(provider.channelFor(Id.of(1)));
    
    assertEquals(2, provider.allOtherNodeChannels().size());
  }

  @Test
  public void testProviderCloseOneChannelReopen() throws Exception {
    provider.close(Id.of(3));
    
    assertNotNull(provider.channelFor(Id.of(3)));
    
    assertEquals(2, provider.allOtherNodeChannels().size());
  }

  @Before
  public void setUp() throws Exception {
    super.setUp();
    
    provider = new ManagedOutboundSocketChannelProvider(config.configuredNodeMatching(Id.of(1)), AddressType.OP, config);
  }
}
