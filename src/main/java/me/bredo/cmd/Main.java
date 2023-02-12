package me.bredo.cmd;

import me.bredo.cmd.client.Client;
import me.bredo.cmd.server.Server;

public class Main {


    public final static boolean isServer = false;

    public Main() {
        final Server server = new Server(5050, true, true);
        final Client client = new Client("localhost", 5050, true);

        if (isServer) {
            server.initialize();
        } else {
            client.initialize();
        }
    }


    public static void main(String[] args) {
        new Main();
    }
}
