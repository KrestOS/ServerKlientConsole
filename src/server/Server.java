package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Server implements Runnable {
    private final int PORT = 9090;

    public void run() {
        DataInputStream in;
        DataOutputStream out;

        try (ServerSocket server = new ServerSocket(PORT)) {
            System.out.println("Server Start");
            try (Socket socket = server.accept()) {
                System.out.println("Client connected");


                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());

                Thread inMsg;
                inMsg = new Thread() {
                    public void run() {
                        while (true) {
                            String msg = null;
                            try {
                                msg = in.readUTF();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (msg.equals("/end")) {
                                System.out.println("Client disconnected");
                                try {
                                    socket.close();
                                    server.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                System.exit(0);

                            }
                            System.out.println("Client talk : " + msg);
                        }
                    }
                };

                Thread sendMsgS;
                sendMsgS = new Thread() {
                    Scanner say = new Scanner(System.in);

                    public void run() {
                        while (true) {
                            String msg = say.nextLine();

                            try {
                                out.writeUTF(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            System.out.println("Server talk : " + msg);

                        }
                    }
                };

                sendMsgS.start();
                inMsg.start();


                try {
                    inMsg.join();
                    sendMsgS.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } finally {
                server.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
