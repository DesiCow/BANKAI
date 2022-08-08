package dev.xdmeow.core.util.helper;

import dev.xdmeow.BanKai;

import java.util.Scanner;

public class ArgsHelper {
    public static void friendly(){
        Scanner s = new Scanner(System.in);

        System.out.println("Enter server IP: ");
        BanKai.srvIP = s.nextLine();
        System.out.println("Enter server protocol: ");
        BanKai.protocol = Integer.parseInt(s.nextLine());
        System.out.println("Enter Method name: ");
        BanKai.methodID = s.nextLine();
        System.out.println("Enter duration: ");
        BanKai.duration = Integer.parseInt(s.nextLine());
        System.out.println("Enter CPS: ");
        BanKai.CPS = Integer.parseInt(s.nextLine());
        System.out.println("Enter proxy type (socs4/socks5/http): ");
        String pt = s.nextLine();
        if(pt.equals("none")){
            BanKai.proxyType = 0;
        }else if(pt.equalsIgnoreCase("socs5")){
            BanKai.proxyType = 1;
        }else if(pt.equalsIgnoreCase("socks4")){
            BanKai.proxyType = 2;
        }else if(pt.equalsIgnoreCase("http")){
            BanKai.proxyType = 3;
        }
        System.out.println("Enter proxy file name: ");
        BanKai.proxyFilePath = s.nextLine();
        s.close();
    }

    public static void oneline(String args[]){
        if(args.length==7){
            BanKai.srvIP= args[0];
            BanKai.protocol = Integer.parseInt(args[1]);
            BanKai.methodID = args[2];
            BanKai.duration = Integer.parseInt(args[3]);
            BanKai.CPS = Integer.parseInt(args[4]);
            if(args[5].equals("none")){
                BanKai.proxyType = 0;
            }else if(args[5].equals("socks5")){
                BanKai.proxyType = 1;
            }else if(args[5].equals("socks4")){
                BanKai.proxyType = 2;
            }else if(args[5].equals("http")){
                BanKai.proxyType = 3;
            }
            BanKai.proxyFilePath = args[6];
        }else{
            System.out.println("Invalid arguments");
            System.out.println("Usage: java -jar BanKai.jar <server IP> <server protocol> <method name> <duration> <CPS> <proxy type> <proxy file>");
            System.exit(0);
        }
    }
}
