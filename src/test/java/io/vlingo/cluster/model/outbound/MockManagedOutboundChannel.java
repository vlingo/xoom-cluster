// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.vlingo.actors.testkit.TestUntil;
import io.vlingo.wire.fdx.outbound.ManagedOutboundChannel;
import io.vlingo.wire.message.RawMessage;
import io.vlingo.wire.node.Id;

public class MockManagedOutboundChannel implements ManagedOutboundChannel {
  public final Id id;
  public final List<String> writes = new ArrayList<>();
  public TestUntil until = TestUntil.happenings(0);
  
  public MockManagedOutboundChannel(final Id id) {
    this.id = id;
  }

  @Override
  public void close() {
    writes.clear();
  }

  @Override
  public void write(final ByteBuffer buffer) {
    final RawMessage message = RawMessage.readFromWithHeader(buffer);
    final String textMessage = message.asTextMessage();
    writes.add(textMessage);
    until.happened();
    //System.out.println("====================\nOUTBOUND " + id + ": WRITES(" + writes.size() + "): MESSAGE: " + textMessage + "\n--------------------");
  }
}
