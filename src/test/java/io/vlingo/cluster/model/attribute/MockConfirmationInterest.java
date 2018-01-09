// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.attribute;

import io.vlingo.cluster.model.attribute.message.ApplicationMessageType;

public class MockConfirmationInterest implements ConfirmationInterest {
  public String attributeName;
  public String attributeSetName;
  public int confirmed;
  public ApplicationMessageType type;
  
  @Override
  public void confirm(final String attributeSetName, final String attributeName, final ApplicationMessageType type) {
    this.attributeSetName = attributeSetName;
    this.attributeName = attributeName;
    this.type = type;
    ++confirmed;
  }
}
