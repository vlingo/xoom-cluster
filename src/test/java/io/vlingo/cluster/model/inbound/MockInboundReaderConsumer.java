// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.util.ArrayList;
import java.util.List;

import io.vlingo.common.message.RawMessage;

public class MockInboundReaderConsumer implements InboundReaderConsumer {
  public int consumeCount;
  public List<String> messages = new ArrayList<>();
  
  @Override
  public void consume(final RawMessage message, final InboundClientChannel clientChannel) {
    ++consumeCount;
    messages.add(message.asTextMessage());
  }
}
