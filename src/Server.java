

import java.io.*;
import java.net.*;


public class Server {
    public static void main (String[] args) throws Exception{
        ServerSocket sScoket = new ServerSocket(4444);
        String adress;
        PrintStream printer;
        while(true){
            final Socket connection = sScoket.accept();
            printer = new PrintStream(connection.getOutputStream());
            printer.println("white");
            adress = connection.getRemoteSocketAddress().toString();
            System.out.printf("user: " + adress + "connected\n");
            PrintStream printt = new PrintStream(connection.getOutputStream());
            final Socket connection2 = sScoket.accept();
            printer = new PrintStream(connection2.getOutputStream());
            printer.println("black");
            adress = connection2.getRemoteSocketAddress().toString();
            PrintStream printt2 = new PrintStream(connection2.getOutputStream());
            System.out.printf("user: " + adress + "connected\n");
            Thread nit = new Thread(() -> {
                Socket x = connection;
                Socket f = connection2;
                String ip,ip2;
                ip = x.getRemoteSocketAddress().toString();
                ip2 = f.getRemoteSocketAddress().toString();
                System.out.printf("starting communication with " + ip + "and "+ ip2);
                String porukica;
                String porukica2;
                InputStreamReader iReader = null;
                BufferedReader bufferedReader = null;
                PrintStream print = null;
                InputStreamReader iReader2 = null;
                BufferedReader bufferedReader2 = null;
                PrintStream print2 = null;
                try {
                    iReader = new InputStreamReader(x.getInputStream());
                    bufferedReader = new BufferedReader(iReader);
                    print = new PrintStream(x.getOutputStream());
                    iReader2 = new InputStreamReader(f.getInputStream());
                    bufferedReader2 = new BufferedReader(iReader2);
                    print2 = new PrintStream(f.getOutputStream());
                    while(true){
                        porukica = bufferedReader.readLine();
                        porukica2 = bufferedReader.readLine();
                        print2.println(porukica);
                        print2.println(porukica2);

                        porukica = bufferedReader2.readLine();
                        porukica2 = bufferedReader2.readLine();
                        print.println(porukica);
                        print.println(porukica2);
                    }
                }catch (Exception y){}
            });
            nit.start();
        }
    }
}
