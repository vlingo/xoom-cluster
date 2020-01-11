// Copyright © 2012-2020 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model;

import io.vlingo.actors.Stage;
import io.vlingo.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.wire.fdx.inbound.InboundStream;
import io.vlingo.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.wire.node.Configuration;
import io.vlingo.wire.node.Node;

public interface CommunicationsHub {
  void close();
  void open(final Stage stage, final Node node, final InboundStreamInterest interest, final Configuration configuration) throws Exception;
  InboundStream applicationInboundStream();
  ApplicationOutboundStream applicationOutboundStream();
  InboundStream operationalInboundStream();
  OperationalOutboundStream operationalOutboundStream();
  void start();
}
