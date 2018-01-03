// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.inbound;

import java.io.IOException;

public interface InboundReader {
  void close();
  String inboundName();
  void openFor(final InboundReaderConsumer consumer) throws IOException;
  void probeChannel();
}
