package dev.xdmeow.core.methods.impl;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class NullPing implements IMethod {

    private byte[] handshake = new Handshake(BanKai.protocol, BanKai.srvRecord, BanKai.port, 69).getWrappedPacket();

        public void accept(Channel channel, Proxy proxy) {
            channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake));
            ++NettyBootstrap.currentConnections;
            ++NettyBootstrap.totalConnections;
            channel.close();
        }
    }
