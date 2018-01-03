// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

import io.vlingo.common.message.RawMessage;
import io.vlingo.common.message.RawMessageBuilder;

public class SocketChannelInboundReader implements InboundReader {
  private final ServerSocketChannel channel;
  private InboundReaderConsumer consumer;
  private final String inboundName;
  private final int maxMessageSize;
  private final int port;
  private final Selector selector;

  public SocketChannelInboundReader(final int port, final String inboundName, final int maxMessageSize) throws Exception {
    this.port = port;
    this.inboundName = inboundName;
    this.channel = ServerSocketChannel.open();
    this.maxMessageSize = maxMessageSize;
    this.selector = Selector.open();
  }

  //=========================================
  // InboundReader
  //=========================================

  @Override
  public void close() {
    try {
      selector.close();
    } catch (Exception e) {
      // TODO: log
    }
    
    try {
      channel.close();
    } catch (Exception e) {
      // TODO: log
    }
  }

  @Override
  public String inboundName() {
    return inboundName;
  }

  @Override
  public void openFor(final InboundReaderConsumer consumer) throws IOException {
    this.consumer = consumer;
    
    channel.socket().bind(new InetSocketAddress(port));
    channel.configureBlocking(false);
    channel.register(selector, SelectionKey.OP_ACCEPT);
  }

  @Override
  public void probeChannel() {
    try {
      if (selector.select(10L) > 0) {
        final Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

        while (iterator.hasNext()) {
          final SelectionKey key = iterator.next();
          iterator.remove();

          if (key.isValid()) {
            if (key.isAcceptable()) {
              accept(key);
            } else if (key.isReadable()) {
              read(key);
            }
          }
        }
      }
    } catch (IOException e) {
      // TODO: log
      e.printStackTrace(System.err);
    }
  }

  //=========================================
  // internal implementation
  //=========================================

  private void accept(final SelectionKey key) throws IOException {
    final ServerSocketChannel serverChannel = (ServerSocketChannel) key.channel();

    if (serverChannel.isOpen()) {
      final SocketChannel clientChannel = serverChannel.accept();
  
      clientChannel.configureBlocking(false);
  
      final SelectionKey clientChannelKey = clientChannel.register(selector, SelectionKey.OP_READ);
  
      clientChannelKey.attach(new InboundChannelInfo(new RawMessageBuilder(maxMessageSize)));
  
      System.out.println(
              "vlingo/cluster: Accepted new connection for '"
              + inboundName
              + "' from: "
              + clientChannel.getRemoteAddress());
    }
  }

  private void closeClient(final SocketChannel clientChannel, final SelectionKey key) throws IOException {
    clientChannel.close();
    key.cancel();
  }

  private void dispatchMessages(final RawMessageBuilder builder, final SocketChannel clientChannel) {
    if (!builder.hasContent()) {
      return;
    }

    builder.prepareContent().sync();

    while (builder.isCurrentMessageComplete()) {
      try {
        final RawMessage message = builder.currentRawMessage();
        consumer.consume(message, new InboundClientSocketChannel(clientChannel));
      } catch (Exception e) {
        // TODO: deal with this
        // TODO: log
        e.printStackTrace(System.err);
      }

      builder.prepareForNextMessage();

      if (builder.hasContent()) {
        builder.sync();
      }
    }
  }

  private void read(final SelectionKey key) throws IOException {
    final SocketChannel clientChannel = (SocketChannel) key.channel();
    final InboundChannelInfo info = (InboundChannelInfo) key.attachment();
    final RawMessageBuilder builder = info.builder();
    
    final boolean continueReading = read(clientChannel, builder);
    
    dispatchMessages(builder, clientChannel);
    
    if (!continueReading) {
      System.out.println("vlingo/cluster: Inbound client stream closed: for '" + inboundName + "'");
      closeClient(clientChannel, key);
    }
  }

  private boolean read(
          final SocketChannel clientChannel,
          final RawMessageBuilder builder)
  throws IOException {
    
    int bytesRead = clientChannel.read(builder.workBuffer());

    while (bytesRead > 0) {
      bytesRead = clientChannel.read(builder.workBuffer());
    }

    return bytesRead != -1;
  }

  private class InboundClientSocketChannel implements InboundClientChannel {
    private final SocketChannel clientChannel;
    
    InboundClientSocketChannel(final SocketChannel clientChannel) {
      this.clientChannel = clientChannel;
    }

    @Override
    public void writeBackResponse(ByteBuffer buffer) throws Exception {
      clientChannel.write(buffer);
    }
  }
}
