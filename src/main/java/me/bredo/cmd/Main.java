package me.bredo.cmd;

import me.bredo.cmd.client.Client;
import me.bredo.cmd.client.ClientConnection;
import me.bredo.cmd.client.IOClientHandling;
import me.bredo.cmd.server.IOServerHandling;
import me.bredo.cmd.server.Server;
import me.bredo.cmd.server.ServerClientConnection;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Main {


    public final static boolean isServer = false;

    public Main() {
        final Server server = new Server(5050, true, true);
        server.setIoServerHandling(ioServerHandling);
        final Client client = new Client("localhost", 5050, true);
        client.setIoClientHandling(ioClientHandling);

        server.setConnectionSoTimeout(4000);

        if (isServer) {
            server.initialize();
        } else {
            client.initialize();
        }
    }

    public final IOServerHandling ioServerHandling = new IOServerHandling() {
        @Override
        public void inputStream(DataInputStream dataInputStream, ServerClientConnection serverClientConnection) throws IOException {
            dataInputStream.readByte();
        }

        @Override
        public void outputStream(DataOutputStream dataOutputStream, ServerClientConnection serverClientConnection) throws IOException {

        }
    };

    public final IOClientHandling ioClientHandling = new IOClientHandling() {
        @Override
        public void inputStream(DataInputStream dataInputStream, ClientConnection clientConnection) throws IOException {

        }

        @Override
        public void outputStream(DataOutputStream dataOutputStream, ClientConnection clientConnection) throws IOException {

        }
    };


    public static void main(String[] args) {
        new Main();
    }
}
