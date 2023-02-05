package me.bredo.cmd.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * The interface IOClientHandling provides methods to handle input and output stream of client connection.
 *
 * @author Bredo
 */
public interface IOClientHandling {

    /**
     * Handles the input stream of the client connection.
     *
     * @param dataInputStream  The data input stream to be handled.
     * @param clientConnection The client connection that the input stream belongs to.
     * @throws IOException If an input or output exception occurs.
     */
    void inputStream(final DataInputStream dataInputStream, final ClientConnection clientConnection) throws IOException;

    /**
     * Handles the output stream of the client connection.
     *
     * @param dataOutputStream The data output stream to be handled.
     * @param clientConnection The client connection that the output stream belongs to.
     * @throws IOException If an input or output exception occurs.
     */
    void outputStream(final DataOutputStream dataOutputStream, final ClientConnection clientConnection) throws IOException;
}
