// Copyright © 2012-2021 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model;

import io.vlingo.xoom.actors.Stage;
import io.vlingo.xoom.cluster.model.outbound.OperationalOutboundStream;
import io.vlingo.xoom.wire.fdx.inbound.InboundStream;
import io.vlingo.xoom.wire.fdx.inbound.InboundStreamInterest;
import io.vlingo.xoom.wire.fdx.outbound.ApplicationOutboundStream;
import io.vlingo.xoom.wire.node.Configuration;
import io.vlingo.xoom.wire.node.Node;

public interface CommunicationsHub {
  void close();
  void open(final Stage stage, final Node node, final InboundStreamInterest interest, final Configuration configuration) throws Exception;
  InboundStream applicationInboundStream();
  ApplicationOutboundStream applicationOutboundStream();
  InboundStream operationalInboundStream();
  OperationalOutboundStream operationalOutboundStream();
  void start();
}
