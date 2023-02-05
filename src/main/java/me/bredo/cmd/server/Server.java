package me.bredo.cmd.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;

/**
 * The `Server` class represents a simple server that can handle incoming client connections.
 */
public final class Server {
    private int port;
    private boolean debugMode;
    private boolean autoStartListening;
    private ServerSocket serverSocket;
    private final ClientListeningHandler clientListeningHandler;
    private final HashSet<ServerClientConnection> serverClientMatrix;
    private IOServerHandling ioServerHandling;

    /**
     * Constructor to create a new instance of the `Server` class.
     *
     * @param port      the port the server will listen on
     * @param debugMode whether the server will be in debug mode
     */
    public Server(final int port, final boolean debugMode, final boolean autoStartListening) {
        setPort(port);
        setDebugMode(debugMode);
        setAutoStartListening(autoStartListening);
        this.clientListeningHandler = new ClientListeningHandler(this);
        this.serverClientMatrix = new HashSet<>();
    }

    /**
     * Initializes the server.
     */
    public void initialize() {
        if (debugMode()) print("Initializing Server");
        initializeServerSocket();

        if (isAutoStartListening()) getClientListeningHandler().startListening();
    }

    /**
     * Initializes the server socket.
     */
    private void initializeServerSocket() {
        try {
            if (debugMode()) print("Initializing Server Socket on port: '" + getPort() + "'");
            this.serverSocket = new ServerSocket(getPort());
        } catch (final IOException exception) {
            warning("Could not create server socket for port: '" + getPort() + "'");
            exception.printStackTrace();
        }
    }

    public void shutdown() {
        try {
            getServerSocket().close();
        } catch (final IOException exception) {
            warning("Could not close server socket");
            exception.printStackTrace();
        }
    }

    /**
     * Returns whether the server is running.
     *
     * @return whether the server is running
     */
    public boolean running() {
        return !getServerSocket().isClosed();
    }

    /**
     * Prints a message to the console with a prefix.
     *
     * @param message the message to print
     */
    void print(final Object message) {
        System.out.println("[Server]: " + message.toString());
    }

    /**
     * Prints a warning message to the console with a prefix.
     *
     * @param message the warning message to print
     */
    void warning(final Object message) {
        System.err.println("[Server]: " + message.toString());
    }

    /**
     * Returns whether the server is in debug mode.
     *
     * @return whether the server is in debug mode
     */
    public boolean debugMode() {
        return this.debugMode;
    }

    /**
     * Sets whether the server is in debug mode.
     *
     * @param debugMode whether the server is in debug mode
     */
    public void setDebugMode(final boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Returns the value of the port the server is running on
     *
     * @return the value of the port the server is running on
     */
    public int getPort() {
        return this.port;
    }

    /**
     * Sets the value of the port the server is running on
     *
     * @param port the new value of the port the server should run on
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Returns the instance of the ServerSocket used by the server
     *
     * @return the instance of the ServerSocket used by the server
     */
    public ServerSocket getServerSocket() {
        return this.serverSocket;
    }

    /**
     * Returns the instance of the IOServerHandling interface used by the server
     *
     * @return the instance of the IOServerHandling interface used by the server
     */
    public IOServerHandling getIoServerHandling() {
        return ioServerHandling;
    }

    /**
     * Sets the instance of the IOServerHandling interface to be used by the server
     *
     * @param ioServerHandling the new instance of the IOServerHandling interface to be used by the server
     */
    public void setIoServerHandling(final IOServerHandling ioServerHandling) {
        this.ioServerHandling = ioServerHandling;
    }

    /**
     * Returns the instance of the ClientListeningHandler used by the server
     *
     * @return the instance of the ClientListeningHandler used by the server
     */
    public ClientListeningHandler getClientListeningHandler() {
        return this.clientListeningHandler;
    }

    /**
     * Returns the HashSet of ServerClientConnection objects representing the clients connected to the server
     *
     * @return the HashSet of ServerClientConnection objects representing the clients connected to the server
     */
    public HashSet<ServerClientConnection> getServerClientMatrix() {
        return this.serverClientMatrix;
    }

    public boolean isAutoStartListening() {
        return autoStartListening;
    }

    public void setAutoStartListening(final boolean autoStartListening) {
        this.autoStartListening = autoStartListening;
    }
}
