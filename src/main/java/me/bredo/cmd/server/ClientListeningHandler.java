package me.bredo.cmd.server;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * ClientListeningHandler is a class in the package `me.bredo.cmd.server` responsible for
 * listening for incoming client connections to the server.
 * <p>
 * It has the following responsibilities:
 * - Start a thread to listen for incoming client connections
 * - Continuously listen for incoming client connections and accept them
 * - Create and start a ServerClientConnection for each accepted connection
 * <p>
 * It has the following properties:
 * - A reference to a Server instance.
 * - A Thread instance for listening for incoming client connections.
 */
public final class ClientListeningHandler {

    private final Server server;
    private final Thread thread;

    /**
     * Constructs a new ClientListeningHandler instance with the given Server.
     *
     * @param server The Server instance that this ClientListeningHandler is associated with.
     */
    public ClientListeningHandler(final Server server) {
        this.server = server;
        this.thread = new Thread(this::listening, "Client-Listening-Thread");
    }

    /**
     * Starts the thread for listening for incoming client connections.
     */
    public void startListening() {
        if (getServer().debugMode()) getServer().print("Starting client listening thread");
        getThread().start();
    }

    /**
     * Runs the thread for listening for incoming client connections.
     */
    public void runListening() {
        if (getServer().debugMode()) getServer().print("Running client listening thread");
        getThread().run();
    }

    /**
     * The method that is executed in the thread to continuously listen for incoming client connections.
     */
    private void listening() {
        while (getServer().running()) {
            if (getServer().debugMode()) getServer().print("Listening for new client socket connection...");
            final Socket socket = listeningForSocket();
            if (getServer().debugMode()) getServer().print("Connection from client '" + socket.getInetAddress() + "'");
            setSocketSettings(socket);
            final ServerClientConnection serverClientConnection = new ServerClientConnection(getServer(), socket);
            getServer().getServerClientMatrix().add(serverClientConnection);
            serverClientConnection.startConnection();
        }
    }

    private void setSocketSettings(final Socket socket) {
        try {
            socket.setTcpNoDelay(getServer().isNoTcpDelay());
            if (getServer().getConnectionSoTimeout() > 0) socket.setSoTimeout(getServer().getConnectionSoTimeout());
        } catch (final SocketException exception) {
            getServer().warning("Could not set socket settings");
            exception.printStackTrace();
        }
    }

    /**
     * Waits for an incoming client connection and returns the corresponding Socket.
     *
     * @return The Socket of the incoming client connection.
     */
    private Socket listeningForSocket() {
        try {
            return getServer().getServerSocket().accept();
        } catch (final IOException exception) {
            getServer().warning("Could not accept new client socket connection");
            exception.printStackTrace();
        }
        return null;
    }

    /**
     * Gets the Server instance that this ClientListeningHandler is associated with.
     *
     * @return The Server instance that this ClientListeningHandler is associated with.
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Gets the Thread instance for listening for incoming client connections.
     *
     * @return The Thread instance for listening for incoming client connections.
     */
    public Thread getThread() {
        return thread;
    }
}
