// Copyright © 2012-2022 VLINGO LABS. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.xoom.cluster.model.node;

import io.vlingo.xoom.actors.Logger;
import io.vlingo.xoom.wire.node.Node;

final class IdleState extends LiveNodeState {
  IdleState(final Node node, final LiveNodeMaintainer liveNodeMaintainer, final Logger logger) {
    super(node, liveNodeMaintainer, Type.IDLE, logger);
  }
}
