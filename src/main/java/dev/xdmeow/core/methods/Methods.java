package dev.xdmeow.core.methods;

import dev.xdmeow.core.methods.impl.BigHandshake;
import dev.xdmeow.core.methods.impl.BotJoiner;
import dev.xdmeow.core.methods.impl.NullPing;

import java.util.HashMap;

public class Methods {
    public static final HashMap<String, IMethod> METHODS = new HashMap<String,IMethod>();

    public static IMethod getByID(int i) {
        return (IMethod)METHODS.getOrDefault(i, (c, p) -> {
            c.close();
            System.err.println("invalid method id: " + i);
        });
    }

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
    }
    public static IMethod getMethod(String methodID) {
        return (IMethod)METHODS.getOrDefault(methodID, new BotJoiner());
    }
}
