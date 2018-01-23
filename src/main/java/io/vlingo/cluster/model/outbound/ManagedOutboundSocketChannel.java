// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.outbound;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import io.vlingo.actors.Logger;
import io.vlingo.cluster.model.node.Address;
import io.vlingo.cluster.model.node.Node;

public class ManagedOutboundSocketChannel implements ManagedOutboundChannel {
  private SocketChannel channel;
  private final Address address;
  private final Node node;
  private final Logger logger;

  public ManagedOutboundSocketChannel(final Node node, final Address address, final Logger logger) {
    this.node = node;
    this.address = address;
    this.logger = logger;
    this.channel = null;
  }

  public void close() {
    if (channel != null) {
      try {
        channel.close();
      } catch (Exception e) {
        logger.log("vlingo/cluster: Close of channel to " + node.id() + " failed for because: " + e.getMessage(), e);
        // TODO: log
      }
    }
    channel = null;
  }

  public void write(final ByteBuffer buffer) {
    final SocketChannel preparedChannel = preparedChannel();

    if (preparedChannel != null) {
      try {
        while (buffer.hasRemaining()) {
          preparedChannel.write(buffer);
        }
      } catch (Exception e) {
        logger.log("vlingo/cluster: Write to " + node + " failed because: " + e.getMessage(), e);
        e.printStackTrace();
        // TODO: log
        close();
      }
    }
  }

  private SocketChannel preparedChannel() {
    try {
      if (channel != null) {
        if (channel.isConnected()) {
          return channel;
        } else {
          close();
        }
      } else {
        channel = SocketChannel.open();
        channel.connect(new InetSocketAddress(address.host(), address.port()));
        return channel;
      }
    } catch (Exception e) {
      close();
    }
    return null;
  }
}
