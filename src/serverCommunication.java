import CustomEvents.eventSender;

import java.io.*;
import java.net.*;

public class serverCommunication implements Runnable {
    private final String serverAddress;
    private final int port = 4444;
    Socket connection;
    private int startingPosition,targetPosition;
    private InputStreamReader is;
    private BufferedReader reader;
    private PrintStream printer;

    private eventSender sender = new eventSender();

    serverCommunication(String serverAddress) {
        this.serverAddress = serverAddress;
        try {
            connection = new Socket(serverAddress, port);
            is = new InputStreamReader(connection.getInputStream());
            reader = new BufferedReader(is);
            printer = new PrintStream(connection.getOutputStream());
        }catch (java.io.IOException x){}
    }

    public boolean isWhite(){
        String temp = "";
        try {
            temp = reader.readLine();
        }catch (java.io.IOException x){}
        if (temp.equals("white")){
            return true;
        }else{
            return false;
        }
    }

    public void sendMove(int startingPossition, int targetPossition){
        printer.println(Integer.toString(startingPossition));
        printer.println(Integer.toString(targetPossition));
    }
    public int getMove(){
        int temp = 100;
        boolean got = false;
        while(!got) {
            try {
                if(reader.ready()) {
                    temp = Integer.parseInt(reader.readLine());
                    got = true;
                }
            } catch (java.io.IOException x) {
            }
        }
        return temp;
    }

    @Override
    public void run() {
        while(true){
            startingPosition = getMove();
            targetPosition = getMove();
            startingPosition = (startingPosition - 63) * -1;
            targetPosition = (targetPosition - 63) * -1;
            sender.sendMove(startingPosition,targetPosition);
        }
    }
}
