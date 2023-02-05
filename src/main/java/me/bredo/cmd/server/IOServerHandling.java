package me.bredo.cmd.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * An interface for handling input/output streams on the server side.
 *
 * @author bredo
 * @version 1.0
 */
public interface IOServerHandling {

    /**
     * Handles the input stream of a server client connection.
     *
     * @param dataInputStream        the input stream to handle
     * @param serverClientConnection the server client connection associated with the input stream
     * @throws IOException if an I/O error occurs during the handling of the input stream
     */
    void inputStream(final DataInputStream dataInputStream, final ServerClientConnection serverClientConnection) throws IOException;

    /**
     * Handles the output stream of a server client connection.
     *
     * @param dataOutputStream       the output stream to handle
     * @param serverClientConnection the server client connection associated with the output stream
     * @throws IOException if an I/O error occurs during the handling of the output stream
     */
    void outputStream(final DataOutputStream dataOutputStream, final ServerClientConnection serverClientConnection) throws IOException;
}
