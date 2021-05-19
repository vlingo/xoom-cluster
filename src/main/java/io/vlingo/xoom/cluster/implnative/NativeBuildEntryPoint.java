package io.vlingo.xoom.cluster.implnative;

import io.vlingo.xoom.actors.World;
import io.vlingo.xoom.cluster.model.ClusterConfiguration;
import org.graalvm.nativeimage.c.function.CEntryPoint;
import org.graalvm.nativeimage.c.type.CCharPointer;
import org.graalvm.nativeimage.c.type.CTypeConversion;

public final class NativeBuildEntryPoint {
  @CEntryPoint(name = "Java_io_vlingo_xoom_clusternative_Native_start")
  public static int start(@CEntryPoint.IsolateThreadContext long isolateId, CCharPointer name) {
    final String nameString = CTypeConversion.toJavaString(name);
    World world = World.start(nameString);
    new ClusterConfiguration(null, world.defaultLogger());

    return 0;
  }
}
