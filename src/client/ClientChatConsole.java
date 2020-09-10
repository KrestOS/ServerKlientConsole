package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientChatConsole {
    private static final int PORT = 9090;
    private static final String IP_ADDRESS = "localhost";
    private static Socket socket;
    static Scanner say;
    static DataOutputStream out;
    static DataInputStream in;

    public static void main(String[] args) {
        Client();

    }

    public static void Client() {


        try {
            socket = new Socket(IP_ADDRESS, PORT);
            say = new Scanner(System.in);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());

            Thread sendMsg;
            sendMsg = new Thread() {

                public void run() {
                    while (true) {
                        String msg = say.nextLine();

                        if (msg.equals("/end")) {
                            try {
                                out.writeUTF(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("You disconnected");
                            System.exit(0);

                        }

                        try {
                            out.writeUTF(msg);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("You talk : " + msg);

                    }
                }
            };
            Thread inMsg;
            inMsg = new Thread() {
                public void run() {
                    while (true) {
                        String inMsg = null;
                        try {
                            inMsg = in.readUTF();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println("Server talk: " + inMsg);
                    }
                }
            };
            sendMsg.start();
            inMsg.start();
            try {
                sendMsg.join();
                inMsg.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


