package com.baotruongtuan.RdpServer.socket;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class RDPSocket {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(7000);
        System.out.println("Server started");

        Socket socket = server.accept();
        DataInputStream in = new DataInputStream(socket.getInputStream());
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

        while (true) {
            String st = null;
            try {
                st = in.readUTF();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            System.out.println("Client gui chuoi:" + st);
            out.writeUTF(st);
        }
    }
}
