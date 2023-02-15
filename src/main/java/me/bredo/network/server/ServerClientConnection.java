package me.bredo.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

/**
 * Represents a single connection to a client in a server.
 */
public final class ServerClientConnection {

    private final Server server;
    private final Socket socket;
    private boolean paused;
    private final Thread thread;

    public final HashMap<String, Object> userData;

    /**
     * Creates a new ServerClientConnection instance.
     *
     * @param server the server instance.
     * @param socket the socket for this connection.
     */
    public ServerClientConnection(final Server server, final Socket socket) {
        this.server = server;
        this.socket = socket;
        this.thread = new Thread(this::connection, "Client[" + getIP() + "]");
        this.userData = new HashMap<>();
    }

    /**
     * Establishes the connection and handles incoming and outgoing data.
     */
    public void connection() {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());

            while (isConnected()) {
                try {
                    inputStream(dataInputStream);
                    outputStream(dataOutputStream);
                } catch (final IOException exception) {
                    getServer().warning("Lost connection to server");
                    exception.printStackTrace();
                    stopConnection();
                }
            }

        } catch (final IOException exception) {
            getServer().warning("Could not create IO stream for client[" + getIP() + "]");
            exception.printStackTrace();
        }
    }

    /**
     * Handles incoming data from the input stream.
     *
     * @param dataInputStream the input stream for this connection.
     * @throws IOException if there is an error reading from the input stream.
     */
    private void inputStream(final DataInputStream dataInputStream) throws IOException {
        dataInputStream.readByte();
        if (getServer().getIoServerHandling() == null) return;
        getServer().getIoServerHandling().inputStream(dataInputStream, this);
    }

    /**
     * Writes outgoing data to the output stream.
     *
     * @param dataOutputStream the output stream for this connection.
     * @throws IOException if there is an error writing to the output stream.
     */
    private void outputStream(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte((byte) (0));
        if (getServer().getIoServerHandling() == null) return;
        getServer().getIoServerHandling().outputStream(dataOutputStream, this);
    }

    /**
     * Checks if the connection is still active.
     *
     * @return true if the connection is still active, false otherwise.
     */
    public boolean isConnected() {
        return !getSocket().isClosed();
    }

    /**
     * Starts the connection.
     */
    public void startConnection() {
        if (getServer().debugMode()) getServer().print("Starting connection for Client[" + getIP() + "]");
        getThread().start();
    }

    /**
     * Stops the connection for the client.
     * <p>
     * If debug mode is enabled, a message is printed to indicate that the connection for the client has been stopped.
     * The client is then removed from the server's client matrix and the connection is closed.
     * </p>
     */
    public void stopConnection() {
        if (getServer().debugMode()) getServer().print("Stopping connection for Client[" + getIP() + "]");
        getServer().getServerClientMatrix().remove(this);
        closeConnection();
    }

    /**
     * Closes the connection for the client.
     * <p>
     * Attempts to close the socket for the client. If it fails, a warning message is printed indicating that the connection could not be closed.
     * </p>
     */
    private void closeConnection() {
        try {
            getSocket().close();
        } catch (final IOException exception) {
            getServer().warning("Could not close Client[" + getIP() + "]");
            exception.printStackTrace();
        }
    }

    /**
     * Pauses or unpauses the connection for the client.
     * <p>
     * If debug mode is enabled, a message is printed to indicate if the connection is being paused or unpaused.
     * </p>
     *
     * @param pause True to pause the connection, false to unpause.
     */
    public void pauseConnection(final boolean pause) {
        this.paused = pause;
        if (getServer().debugMode()) getServer().print((pause ? "Pausing" : "UnPausing") + " connection for Client[" + getIP() + "] ");
    }

    /**
     * Returns the IP address of the client.
     *
     * @return The IP address of the client.
     */
    public InetAddress getIP() {
        return getSocket().getInetAddress();
    }

    /**
     * Returns whether the connection for the client is paused or not.
     *
     * @return True if the connection is paused, false otherwise.
     */
    public boolean isPaused() {
        return this.paused;
    }

    /**
     * Returns the server object associated with this client connection.
     *
     * @return The server object.
     */
    public Server getServer() {
        return this.server;
    }

    /**
     * Returns the socket object associated with this client connection.
     *
     * @return The socket object.
     */
    public Socket getSocket() {
        return this.socket;
    }

    /**
     * Returns the thread object associated with this client connection.
     *
     * @return The thread object.
     */
    public Thread getThread() {
        return thread;
    }

}
