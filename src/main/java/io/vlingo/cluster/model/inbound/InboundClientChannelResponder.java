// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.nio.ByteBuffer;

final class InboundClientChannelResponder implements InboundResponder, InboundClientReference {
  private final InboundClientChannel clientChannel;
  private final InboundStream inboundStream;
  
  InboundClientChannelResponder(final InboundStream inboundStream, final InboundClientChannel clientChannel) {
    this.inboundStream = inboundStream;
    this.clientChannel = clientChannel;
  }

  @Override
  public void respondWith(final ByteBuffer buffer) throws Exception {
    inboundStream.respondWith(this, buffer);
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> T reference() {
    return (T) clientChannel;
  }
}
