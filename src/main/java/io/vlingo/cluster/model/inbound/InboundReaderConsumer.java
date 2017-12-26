// Copyright © 2012-2017 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import io.vlingo.common.message.RawMessage;

public interface InboundReaderConsumer {
  void consume(final RawMessage message, final InboundClientChannel clientChannel);
}
