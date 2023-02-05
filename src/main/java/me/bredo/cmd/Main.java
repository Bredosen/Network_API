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
    public static void main(String[] args) {
        final Server server = new Server(8080, true, true);
        final Client client = new Client("35.204.112.176", 5050, true);

        serverIOHandling(server);

        clientIOHandling(client);

        if (args.length != 1) return;

        if (args[0].equalsIgnoreCase("-Client")) client.initialize();
        else if (args[0].equalsIgnoreCase("-Server")) server.initialize();
        else System.err.println("Please specify valid argument");
    }


    public static void clientIOHandling(final Client client) {
        client.setIoClientHandling(new IOClientHandling() {
            @Override
            public void inputStream(DataInputStream dataInputStream, ClientConnection clientConnection) throws IOException {
            }

            @Override
            public void outputStream(DataOutputStream dataOutputStream, ClientConnection clientConnection) throws IOException {
            }
        });
    }

    public static void serverIOHandling(final Server server) {
        server.setIoServerHandling(new IOServerHandling() {
            @Override
            public void inputStream(DataInputStream dataInputStream, ServerClientConnection serverClientConnection) throws IOException {
                byte[] data = new byte[1024];
                final int dataLength = dataInputStream.read(data);
                final String message = new String(data, 0, dataLength);
                System.out.println(message);
            }

            @Override
            public void outputStream(DataOutputStream dataOutputStream, ServerClientConnection serverClientConnection) throws IOException {

            }
        });
    }
}