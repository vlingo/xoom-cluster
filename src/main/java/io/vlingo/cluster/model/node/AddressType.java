// Copyright © 2012-2018 Vaughn Vernon. All rights reserved.
//
// This Source Code Form is subject to the terms of the
// Mozilla Public License, v. 2.0. If a copy of the MPL
// was not distributed with this file, You can obtain
// one at https://mozilla.org/MPL/2.0/.

package io.vlingo.cluster.model.node;

public enum AddressType {
  OP {
    public boolean isOperational() {
      return true;
    }
  },
  APP {
    public boolean isApplication() {
      return true;
    }
  },
  NONE {
    public boolean isNone() {
      return true;
    }
  };

  public boolean isApplication() {
    return false;
  }

  public boolean isOperational() {
    return false;
  }

  public boolean isNone() {
    return false;
  }
}
