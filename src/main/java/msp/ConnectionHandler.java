package msp;

import msp.UserManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Logger;

public class ConnectionHandler implements Runnable {

    public static final String CLASS_NAME = ConnectionHandler.class.getSimpleName();
    public static final Logger LOGGER = Logger.getLogger(CLASS_NAME);


    private UserManager users;
    private Socket clientSocket = null;

    private BufferedReader input;
    private PrintWriter output;


    public ConnectionHandler(UserManager u, Socket s) {
        users = u;
        clientSocket = s;

        try {
            input = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            output = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            LOGGER.severe(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        String buffer = null ;
        while (true) {
            try {
                buffer = input.readLine();
            } catch (IOException e) {
                LOGGER.severe(e.getMessage());
                e.printStackTrace();
            }
            String command = buffer.trim();
            if( command.startsWith("CONNECT") ) {
                String userName = command.substring(command.indexOf(' ')  ).trim();
                System.out.println(userName);
                boolean isConnected =  users.connect(userName,clientSocket);
                if( isConnected ) {
                    output.println("OK");
                } else {
                    output.println("FAIL");
                }
            }




            // SEND #<mensaje>@<usuario>
            if( command.startsWith("SEND") ) {
                String message = command.substring(command.indexOf('#')+1,
                        command.indexOf('@') );

                while (message.length() >= 140){


                    System.out.println(message);
                    String userName = command.substring(command.indexOf('@')+1 ).trim();
                    System.out.println(userName);
                    users.send(message);
                }

                //output.println(message);
            }

            if( command.startsWith("DISCONNECT") ) {
                String UserName = command.substring(command.indexOf(" ")  ).trim();
                boolean isDisconnected =  users.disconnect(UserName);
                if( isDisconnected ) {
                    output.println("User " + UserName + " has been disconnected from the current server");
                } else {
                    output.println("SOmethings wrong");
                }
            }

            /*
            if( command.startsWith("DISCONNECT") ) {
                String UserName = command.substring(command.indexOf(" ")  ).trim();
                System.out.println(message);
                    String userName = command.substring(command.indexOf('@')+1 ).trim();
                    System.out.println("USer disconnected");
                }
             */



            if( command.startsWith("SHOW") ) {
                users.VisualUsers();
            }

        }


    }
}
