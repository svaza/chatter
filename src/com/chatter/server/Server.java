 /*
 * =============================================================================
 * Server.java
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
package com.chatter.server;

import com.chatter.ui.console.ChatterConsole;
import java.io.IOException;
import java.net.*;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * creates the server thread for receiving client notifications.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class Server extends Thread {

    /**
     * indicates whether server is running in debug mode
     */
    private boolean debug;
    /**
     * default port for running server.
     */
    private static final int DEFAULT_PORT = 8082;
    /**
     * port at which server runs.
     */
    private int port;
    /**
     * name of server
     */
    private String serverName;
    /**
     * MessageRecieveListener Object which will be notified when server recieves notification from user.
     */
    private MessageRecieveListener messageRecieveListener;
    /**
     * ServerSocketChannel Object.It is used for recieving large data from clients.
     */
    protected ServerSocketChannel serverSocketChannel;
    /**
     * SocketChannel recieved from ServerSocketChannel.accept() method.
     */
    protected SocketChannel recievedClientSocketChannel;
    /**
     * ChatterConsole object for writing debug output
     */
    private ChatterConsole chatterConsole;
    /**
     * if true then, Indicates whether some error occured in server.
     */
    private boolean failToRun;
    /**
     * if true then, Indicates that server is started.
     */
    private boolean started;
    private ServerSocket chatServerSocket;

    /**
     * creates  a server thread on DEFAULT_PORT with debug to true and default server name to "server"    * @param chatterConsole     ChatterConsole object for writing debug output.
     * @param chatterConsole                 ChatterConsole object for writing debug output
     */
    public Server(ChatterConsole chatterConsole) {
        debug = true;
        serverName = "Server";
        failToRun = false;
        started = true;
        port = DEFAULT_PORT;
        this.chatterConsole = chatterConsole;
    }

    public Server(int port, ChatterConsole chatterConsole) {
        debug = true;
        serverName = "Server";
        failToRun = false;
        started = true;
        this.port = port;
        this.chatterConsole = chatterConsole;
    }

    public Server(int port, ThreadGroup group, ChatterConsole chatterConsole) {
        super(group, "serverName");
        debug = true;
        serverName = "Server";
        failToRun = false;
        started = true;
        this.port = port;
        this.chatterConsole = chatterConsole;
    }

    public ChatterConsole getChatterConsole() {
        return chatterConsole;
    }

    public boolean isFailToRun() {
        return failToRun;
    }

    public SocketChannel getRecievedClientSocketChannel() {
        return recievedClientSocketChannel;
    }

    public ServerSocketChannel getServerSocketChannel() {
        return serverSocketChannel;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public MessageRecieveListener getMessageRecieveListener() {
        return messageRecieveListener;
    }

    public void addMessageRecieveListener(MessageRecieveListener messageRecieveListener) {
        this.messageRecieveListener = messageRecieveListener;
    }

    public void stopService()
            throws IOException {
        started = false;
        if (serverSocketChannel != null) {
            serverSocketChannel.close();
        }
    }

    public boolean isStarted() {
        return started;
    }

    public synchronized void run() {
        writeOut((new StringBuilder()).append(serverName).append(" started sucessfully listening on port ").append(port).toString());
        do {
            if (!started) {
                break;
            }
            try {
                recievedClientSocketChannel = serverSocketChannel.accept();
                messageRecieveListener.recieveMessage(this);
                recievedClientSocketChannel.close();
                writeOut((new StringBuilder()).append(serverName).append(":CLOSE").toString());
            } catch (Exception e) {
                writeOut(getServerName() + " Failed to start");
                failToRun = true;
                if (isDebug()) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    protected void instantiateServer()
            throws BindException {
        try {
            serverSocketChannel = ServerSocketChannel.open();
            chatServerSocket = serverSocketChannel.socket();
            chatServerSocket.bind(new InetSocketAddress(getPort()));
            super.setName((new StringBuilder()).append(serverName).append(" listening on port ").append(port).toString());
        } catch (BindException e) {
            throw e;
        } catch (Exception e) {
            writeOut((new StringBuilder()).append(serverName).append(":").append(e).toString());
            if (isDebug()) {
                e.printStackTrace();
            }
        }
    }

    public void writeOut(String message) {
        if (isDebug()) {
            if (chatterConsole != null) {
                chatterConsole.println(message);
            } else {
                System.out.println(message);
            }
        }
    }

    public void startServer()
            throws BindException {
        try {
            writeOut((new StringBuilder()).append("starting ").append(serverName).append(" on port ").append(port).toString());
            instantiateServer();
            start();
        } catch (BindException be) {
            failToRun = true;
            throw be;
        } catch (Exception e) {
            writeOut((new StringBuilder()).append(serverName).append(":").append(e).toString());
            if (isDebug()) {
                e.printStackTrace();
            }
        }
    }

    /**
     * @return the debug
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * @param debug the debug to set
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
    }
}
