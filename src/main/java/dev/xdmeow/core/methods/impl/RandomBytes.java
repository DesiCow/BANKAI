package dev.xdmeow.core.methods.impl;

import java.security.SecureRandom;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;

public class RandomBytes implements IMethod{

    private static final SecureRandom RANDOM = new SecureRandom();
    
    @Override
    public void accept(Channel channel, Proxy proxy) {
        byte[] bytes = new byte[4 + RANDOM.nextInt(128)];
        RANDOM.nextBytes(bytes);
        channel.writeAndFlush(Unpooled.buffer().writeBytes(bytes));
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
        
            if (RANDOM.nextBoolean()) {
                channel.config().setOption(ChannelOption.SO_LINGER, Integer.valueOf(1));
            }

          channel.close();
    }
    
}
