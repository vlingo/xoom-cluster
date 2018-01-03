// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.vlingo.cluster.model.node.Id;
import io.vlingo.common.message.RawMessage;

public class MockManagedOutboundChannel implements ManagedOutboundChannel {
  public final Id id;
  public final List<String> writes = new ArrayList<>();
  
  public MockManagedOutboundChannel(final Id id) {
    this.id = id;
  }

  @Override
  public void close() {
    writes.clear();
  }

  @Override
  public void write(final ByteBuffer buffer) {
    final RawMessage message = RawMessage.from(buffer);
    writes.add(message.asTextMessage());
  }
}
