package me.bredo.cmd.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Class responsible for handling the client connection to a server.
 *
 * @author bredo
 */
public final class ClientConnection {

    /**
     * Reference to the client object.
     */
    private final Client client;

    /**
     * The thread object that is responsible for handling the connection to the server.
     */
    private final Thread thread;

    /**
     * Indicates whether the connection is currently paused or not.
     */
    private boolean paused;

    /**
     * Creates a new instance of the client connection for the given client.
     *
     * @param client The client that this connection belongs to.
     */
    public ClientConnection(final Client client) {
        this.client = client;
        this.thread = new Thread(this::connection, "Client-Thread");
    }

    /**
     * Handles the connection to the server by opening input and output streams.
     */
    public void connection() {
        try {
            final DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());
            final DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());

            while (isConnected()) {
                try {
                    outputStream(dataOutputStream);
                    inputStream(dataInputStream);
                } catch (final IOException exception) {
                    stopConnection();
                }
            }

        } catch (final IOException exception) {
            getClient().warning("Could not create IO stream");
            exception.printStackTrace();
        }
    }

    /**
     * Handles incoming data from the input stream.
     *
     * @param dataInputStream The input stream that the data is coming from.
     * @throws IOException If there is an issue with reading from the input stream.
     */
    private void inputStream(final DataInputStream dataInputStream) throws IOException {
        dataInputStream.readByte();
        if (getClient().getIoClientHandling() == null) return;
        getClient().getIoClientHandling().inputStream(dataInputStream, this);
    }

    /**
     * Writes data to the output stream.
     *
     * @param dataOutputStream The output stream that the data is being written to.
     * @throws IOException If there is an issue with writing to the output stream.
     */
    private void outputStream(final DataOutputStream dataOutputStream) throws IOException {
        dataOutputStream.writeByte((byte) (0));
        if (getClient().getIoClientHandling() == null) return;
        getClient().getIoClientHandling().outputStream(dataOutputStream, this);
    }

    /**
     * Returns true if the connection is still active and connected to the server, false otherwise.
     *
     * @return A boolean indicating the connection status.
     */
    public boolean isConnected() {
        return !getSocket().isClosed();
    }

    /**
     * This method starts the client connection by starting the thread that manages the connection.
     * If the debug mode is enabled, a message will be printed to the console indicating that the
     * connection is starting.
     */
    public void startConnection() {
        if (getClient().debugMode()) {
            getClient().print("Starting connection");
        }
        getThread().start();
    }

    /**
     * This method stops the client connection. If the debug mode is enabled, a message will be printed
     * to the console indicating that the connection is stopping.
     */
    public void stopConnection() {
        if (getClient().debugMode()) getClient().print("Stopping connection");
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
            getClient().warning("Could not close client");
            exception.printStackTrace();
        }
    }

    /**
     * This method pauses or unpauses the client connection based on the value of the `pause` parameter.
     * If the `pause` parameter is `true`, the connection will be paused. If `false`, the connection
     * will be unpaused. If the debug mode is enabled, a message indicating whether the connection is
     * being paused or unpaused will be printed to the console.
     *
     * @param pause a boolean value indicating whether to pause or unpause the connection
     */
    public void pauseConnection(final boolean pause) {
        this.paused = pause;
        if (getClient().debugMode()) {
            getClient().print((pause ? "Pausing" : "UnPausing") + " connection");
        }
    }

    /**
     * This method returns the `Client` object associated with this connection.
     *
     * @return the `Client` object associated with this connection
     */
    public Client getClient() {
        return client;
    }

    /**
     * This method returns the `Socket` object associated with this connection.
     *
     * @return the `Socket` object associated with this connection
     */
    public Socket getSocket() {
        return getClient().getSocket();
    }

    /**
     * This method returns the `Thread` object associated with this connection.
     *
     * @return the `Thread` object associated with this connection
     */
    public Thread getThread() {
        return thread;
    }
}
