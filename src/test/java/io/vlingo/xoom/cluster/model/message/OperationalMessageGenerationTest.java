// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.message;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import io.vlingo.xoom.wire.message.ByteBufferAllocator;
import io.vlingo.xoom.wire.message.Converters;
import io.vlingo.xoom.wire.node.Address;
import io.vlingo.xoom.wire.node.AddressType;
import io.vlingo.xoom.wire.node.Id;
import io.vlingo.xoom.wire.node.Name;
import io.vlingo.xoom.wire.node.Node;

public class OperationalMessageGenerationTest {
  private static final int BYTES = 4096;
  private ByteBuffer expectedBuffer = ByteBufferAllocator.allocate(BYTES);
  private ByteBuffer messageBuffer = ByteBufferAllocator.allocate(BYTES);

  @Test
  public void testGenerateApplicationSaidMessage() {
    final Id id = Id.of(1);
    final Name name = new Name("node1");
    final String payload = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Morbi sagittis risus quis nulla blandit, a euismod massa egestas. Vivamus facilisis.";

    final ApplicationSays app = ApplicationSays.from(id, name, payload);
    final String raw = OperationalMessage.APP + "\n" + "id=1 nm=node1 si=" + app.saysId + "\n" + payload;
    expectedBuffer.put(Converters.textToBytes(raw));
    MessageConverters.messageToBytes(app, messageBuffer);
    assertArrayEquals(expectedBuffer.array(), messageBuffer.array());

    assertEquals(app, ApplicationSays.from(raw));
  }

  @Before
  public void setUp() {
    expectedBuffer.clear();
    messageBuffer.clear();

    for (int idx = 0; idx < BYTES; ++idx) {
      expectedBuffer.put(idx, (byte) 0);
      messageBuffer.put(idx, (byte) 0);
    }
  }
}
