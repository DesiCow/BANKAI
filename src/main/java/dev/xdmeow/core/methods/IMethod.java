package dev.xdmeow.core.methods;

import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.channel.Channel;
import java.util.function.BiConsumer;

public interface IMethod extends BiConsumer<Channel, Proxy> {

}
