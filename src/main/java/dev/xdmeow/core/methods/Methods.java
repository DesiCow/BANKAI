package dev.xdmeow.core.methods;

import dev.xdmeow.core.methods.impl.BigHandshake;
import dev.xdmeow.core.methods.impl.BotJoiner;
import dev.xdmeow.core.methods.impl.EmptyPacket;
import dev.xdmeow.core.methods.impl.LocalHost;
import dev.xdmeow.core.methods.impl.LongName;
import dev.xdmeow.core.methods.impl.MaxsizePacket;
import dev.xdmeow.core.methods.impl.NullPing;
import dev.xdmeow.core.methods.impl.Ping;
import dev.xdmeow.core.methods.impl.Queue;
import dev.xdmeow.core.methods.impl.QuitExceptions;
import dev.xdmeow.core.methods.impl.RamFucker;
import dev.xdmeow.core.methods.impl.RandomBytes;
import dev.xdmeow.core.methods.impl.SmartBot;
import dev.xdmeow.core.methods.impl.UltraJoin;
import dev.xdmeow.core.methods.impl.WaterfallBypass;

import java.util.HashMap;

public class Methods {

    public static final HashMap<String, IMethod> METHODS = new HashMap<String,IMethod>();

    public static IMethod getByID(int i) {
        return (IMethod)METHODS.getOrDefault(i, (c, p) -> {
            c.close();
            System.err.println("invalid method id: " + i);
        });
    }

    /**
     * @param name
     * @param method
     */
    private static void registerMethod(String name, IMethod m) {
        if (METHODS.containsKey(name)) {
            throw new IllegalStateException("Method with id " + name + " already exists");
        } else {
            METHODS.put(name, m);
        }
    }

    public static void setupMethods() {
        registerMethod("botjoiner", new BotJoiner());
        registerMethod("nullping", new NullPing());
        registerMethod("bighandshake", new BigHandshake());
        registerMethod("ramfucker", new RamFucker());
        registerMethod("maxsizepacket", new MaxsizePacket());
        registerMethod("longname", new LongName());
        registerMethod("localhost", new LocalHost());
        registerMethod("queue", new Queue());
        registerMethod("ultrajoin", new UltraJoin());
        registerMethod("ping", new Ping());
        registerMethod("waterfallbypass", new WaterfallBypass());
        registerMethod("smartbot", new SmartBot());
        registerMethod("emptypacket", new EmptyPacket());
        registerMethod("quitexceptions", new QuitExceptions());
        registerMethod("randombytes", new RandomBytes());
    }
    
    public static IMethod getMethod(String methodID) {
        return (IMethod)METHODS.getOrDefault(methodID, new BotJoiner());
    }
}
