package dev.xdmeow.core.methods.impl;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class EmptyPacket implements IMethod{

    @Override
    public void accept(Channel channel, Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(new byte[1])); 
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
        channel.close();       
    }
    
}
