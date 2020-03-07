/*
 * =============================================================================
 * Client.java
 * Copyright (c) 2010 Santosh Vaza.[vazasantosh@gmail.com]
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.

 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.

 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses/.
 * =============================================================================
 */
package com.chatter.client;

import com.chatter.ui.console.ChatterConsole;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.BindException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
 * This class is used for connecting client sockets to remote machine.
 * @author Santosh Vaza
 * @version 1.0
 */
public class Client {

    public final static long MAX_BUFFER_SIZE = 30 * 1024 * 1024;
    /**
     * specifies the default port at which the client will connect at remote host.
     */
    public static final int DEFAULT_PORT = 8082;
    /**
     * Specifies that Client is connected for sending simple text messages.
     */
    public static final int TEXT_MESSAGE = 101;
    /**
     * Specifies that client is used for sending binary files.
     */
    public static final int FILE_TRANSFER = 102;
    /**
     * InetAddress of remote host.
     */
    private InetAddress iDestination;
    /**
     * The input stream to the file that has to be sent.
     * This is only required only if mode is Client.TEXT_MESSAGE
     */
    private InputStream messageAsStream;
    /**
     * The message to be send
     * This is only required only if mode is Client.FILE_TRANSFER
     */
    private String messageAsText;
    /**
     * specifies the port at which the socket will connect at remote host.
     */
    private int port;
    /**
     * specifies that the log messages will be written to output stream or debug stream
     */
    protected boolean debug;
    /**
     * Specifies the mode of client i.e,
     * Client.TEXT_MESSAGE
     * Client.FILE_TRANSFER
     */
    private int mode;
    private SocketChannel socketChannel;
    /**
     * the chatter console object for writing the log messages.
     */
    private ChatterConsole chatterConsole;
    /**
     * The default buffer size, which is 1MB.
     */
    public static int BUFFER_SIZE = 1 * 1024 * 1024;
    /**
     * Flag from which user can determine that client has failed to connect or some error has occured
     * true              :              Some error has occured while performing operation.
     * false             :              The connection and operation was sucessfull.
     */
    private boolean fail;

    /**
     * Default constructor which sets debug to false and fail flag to false.
     */
    public Client() {
        debug = true;
        fail = false;
    }

    /**
     * @param iDestination                   InetAddress of remote host.
     * @param port                           The port at which the client will connect socket.
     * @param mode                           The mode of operation i.e FILE_TRANSFER or TEXT_CHAT
     * @param chatterConsole                 The chatter console object for writing log messages.
     */
    public Client(InetAddress iDestination, int port, int mode,
            ChatterConsole chatterConsole) {
        debug = true;
        fail = false;
        this.iDestination = iDestination;
        this.port = port;
        this.mode = mode;
        this.chatterConsole = chatterConsole;
    }

    /**
     * @param iDestination                   InetAddress of remote host.
     * @param mode                           The mode of operation i.e FILE_TRANSFER or TEXT_CHAT
     * @param chatterConsole                 The chatter console object for writing log messages.
     */
    public Client(InetAddress iDestination, int mode,
            ChatterConsole chatterConsole) {
        debug = true;
        fail = false;
        this.iDestination = iDestination;
        port = DEFAULT_PORT;
        this.mode = mode;
        this.chatterConsole = chatterConsole;
    }

    public ChatterConsole getChatterConsole() {
        return chatterConsole;
    }

    public void setChatterConsole(ChatterConsole chatterConsole) {
        this.chatterConsole = chatterConsole;
    }

    /**
     * fail     :      specifies whether some error occured while performing task.
     * @return    fail (true) if some error occured
     */
    public boolean isFail() {
        return fail;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getMessageAsText() {
        return messageAsText;
    }

    public void setMessageAsText(String messageAsText) {
        this.messageAsText = messageAsText;
    }

    public InetAddress getIDestination() {
        return iDestination;
    }

    public void setIDestination(InetAddress iDestination) {
        this.iDestination = iDestination;
    }

    public InputStream getMessageAsStream() {
        return messageAsStream;
    }

    public void setMessageAsStream(InputStream messageAsStream) {
        this.messageAsStream = messageAsStream;
    }

    /**
     * Sends message to client
     * @param shutAfterCompleting            : should shutdown all sockets after sending message.
     */
    public void sendMessage(boolean shutAfterCompleting) {
        run(shutAfterCompleting);
    }

    /**
     * performs shut down operation of client.
     */
    public void shutDownClient() {
        try {
            if (messageAsStream != null) {
                messageAsStream.close();
            }
            if (socketChannel != null) {
                socketChannel.close();
            }
        } catch (Exception e) {
            fail = true;
            writeOut((new StringBuilder()).append("CLIENT:").append(e).toString());
        }
    }

    /**
     * writes the message to console or output stream if debug is set to true.
     * @param message        message to be written to log or output stream
     */
    public void writeOut(String message) {
        if (debug) {
            if (chatterConsole != null) {
                chatterConsole.println(message);
            } else {
                System.out.println(message);
            }
        }
    }

    /**
     * connects the client to remote host.
     */
    public void connect() throws Exception {
        try {

            socketChannel =
                    SocketChannel.open(new InetSocketAddress(getIDestination(),
                    getPort()));
            socketChannel.socket().setReuseAddress(true);

        } catch (BindException e) {
            writeOut((new StringBuilder()).append("CLIENT reconnecting...:").append(e).toString());
            try {
                Thread.sleep(300);
                connect();
            } catch (Exception ie) {
                fail = true;
                writeOut((new StringBuilder()).append("CLIENT:").append(ie).toString());
                if (debug) {
                    e.printStackTrace();
                }
                throw ie;
            }
        } catch (Exception e) {
            fail = true;
            writeOut((new StringBuilder()).append("CLIENT:").append(e).toString());
            if (debug) {
                e.printStackTrace();
            }
            throw e;
        }
    }

    /**
     * Performs File Transfer operation.
     * @throws IOException
     */
    public void performFileTransferOperation() throws IOException {
        FileChannel fc = ((FileInputStream) messageAsStream).getChannel();
        long bytesTransferred = 0;
        long totalBytes = fc.size();
        while (bytesTransferred < totalBytes) {
            long bufferSize = Client.BUFFER_SIZE;
            if (totalBytes - bytesTransferred < bufferSize) {
                bufferSize = (int) (totalBytes - bytesTransferred);
                if (bufferSize <= 0) {
                    bufferSize = (int) totalBytes;
                }
            }
            long bytesRead =
                    fc.transferTo(bytesTransferred, bufferSize, socketChannel);
            if (bytesRead > 0) {
                bytesTransferred += bytesRead;
            }
        }
        writeOut("CLIENT:file sent sucessfully");
    }

    /**
     * Performs text message transfer operation.
     * @throws IOException
     */
    public void performTextMessageTransferOperation() throws IOException {
        OutputStream out = socketChannel.socket().getOutputStream();
        out.write(getMessageAsText().getBytes());
        out.close();
        writeOut("CLIENT:Message sent sucessfully");
    }

    @SuppressWarnings("CallToThreadDumpStack")
    private void run(boolean shutAfterCompleting) {
        try {
            //perform file transfer operation
            if (mode == FILE_TRANSFER) {
                performFileTransferOperation();
            } //perform text chat operation
            else if (mode == TEXT_MESSAGE) {
                performTextMessageTransferOperation();
            } else {
                throw new IllegalArgumentException("Invalid mode of chat");
            }
        } catch (BindException e) {
            writeOut("failed to bind server");
        } catch (Exception e) {
            writeOut("EXCEPTION:");
            writeOut(e.getMessage());
        } finally {
            if (shutAfterCompleting) {
                shutDownClient();
            }
        }
    }
}
