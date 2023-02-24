package me.bredo.network.client;

import java.io.IOException;
import java.net.Socket;

/**
 * The `Client` class represents a client in a network communication setup.
 * The client can connect to a server using the IP address and port number specified,
 * and communicate with the server through a `Socket` object.
 */
public final class Client {

    /**
     * Object for managing the connection between the client and the server
     */
    private final    ClientConnection clientConnection;
    /**
     * IP address of the server to connect to
     */
    private          String           ip;
    /**
     * Port number of the server to connect to
     */
    private          int              port;
    /**
     * Flag indicating whether to print debug messages
     */
    private          boolean          debugMode;
    /**
     * `Socket` object for communication with the server
     */
    private          Socket           socket;
    /**
     * Object for handling input/output for the client
     */
    private volatile IOClientHandling ioClientHandling;

    /**
     * Constructor for `Client`
     *
     * @param ip        IP address of the server to connect to
     * @param port      Port number of the server to connect to
     * @param debugMode Flag indicating whether to print debug messages
     */
    public Client(final String ip, final int port, final boolean debugMode) {
        setIp(ip);
        setPort(port);
        setDebugMode(debugMode);
        this.clientConnection = new ClientConnection(this);
    }

    /**
     * Initializes the `Client` object, including creating the `Socket` object
     * and starting the connection with the server.
     */
    public void initialize() {
        if (debugMode()) print("Initializing Client");
        initializeClientSocket();

        getClientConnection().startConnection();
    }

    /**
     * Initializes the `Socket` object for communication with the server.
     */
    private void initializeClientSocket() {
        try {
            if (debugMode()) print("Initializing Client Socket on " + getIp() + ":" + getPort());
            this.socket = new Socket(ip, port);
        } catch (final IOException exception) {
            warning("Could not create client socket for " + ip + ":" + port);
            exception.printStackTrace();
        }
    }

    /**
     * Prints a message to the console, with a prefix indicating it's from the client.
     *
     * @param message Object to be printed
     */
    public void print(final Object message) {
        System.out.println("[Client]: " + message.toString());
    }

    /**
     * Prints a warning message to the console, with a prefix indicating it's from the client.
     *
     * @param message Object to be printed
     */
    public void warning(final Object message) {
        System.err.println("[Client]: " + message.toString());
    }

    /**
     * Gets the IP address of the server to connect to.
     *
     * @return IP address of the server
     */
    public String getIp() {
        return ip;
    }

    /**
     * Set the IP address of the server.
     *
     * @param ip The IP address of the server.
     */
    public void setIp(final String ip) {
        this.ip = ip;
    }

    /**
     * Get the port number of the server.
     *
     * @return The port number of the server.
     */
    public int getPort() {
        return port;
    }

    /**
     * Set the port number of the server.
     *
     * @param port The port number of the server.
     */
    public void setPort(final int port) {
        this.port = port;
    }

    /**
     * Check if the `debugMode` is enabled.
     *
     * @return True if `debugMode` is enabled, false otherwise.
     */
    public boolean debugMode() {
        return debugMode;
    }

    /**
     * Get the `IOClientHandling` object.
     *
     * @return The `IOClientHandling` object.
     */
    public IOClientHandling getIoClientHandling() {
        return ioClientHandling;
    }

    /**
     * Set the `IOClientHandling` object.
     *
     * @param ioClientHandling The `IOClientHandling` object.
     */
    public void setIoClientHandling(final IOClientHandling ioClientHandling) {
        this.ioClientHandling = ioClientHandling;
    }

    /**
     * Get the `ClientConnection` object.
     *
     * @return The `ClientConnection` object.
     */
    public ClientConnection getClientConnection() {
        return clientConnection;
    }

    /**
     * Get the `Socket` object.
     *
     * @return The `Socket` object.
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * Set the `debugMode` flag.
     *
     * @param debugMode The `debugMode` flag.
     */
    public void setDebugMode(final boolean debugMode) {
        this.debugMode = debugMode;
    }
}
