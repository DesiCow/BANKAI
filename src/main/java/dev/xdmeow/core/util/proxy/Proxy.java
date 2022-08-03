package dev.xdmeow.core.util.proxy;

import java.net.InetSocketAddress;
public class Proxy {

    public final InetSocketAddress address;

    public String email = null;

    public String pw = null;

    public Proxy(InetSocketAddress address) {
        this.address = address;
    }

    public Proxy(InetSocketAddress address, String email, String pw) {
        this.address = address;
        this.email = email;
        this.pw = pw;
    }
}

