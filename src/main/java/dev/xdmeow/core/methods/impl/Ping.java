package dev.xdmeow.core.methods.impl;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.mcutil.ext.PingPacket;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class Ping implements IMethod{

    private byte[] handshake = new Handshake(BanKai.protocol, BanKai.srvRecord, BanKai.port, 1).getWrappedPacket();

    @Override
    public void accept(Channel channel, Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake));
        channel.writeAndFlush(Unpooled.buffer().writeBytes(new byte[] { 1, 0 }));
        channel.writeAndFlush(Unpooled.buffer().writeBytes((new PingPacket(System.currentTimeMillis())).getWrappedPacket()));
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
        channel.close();
    }
    
}
