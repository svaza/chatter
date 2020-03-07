 /*
 * =============================================================================
 * FileTransferServer.java
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
package com.chatter.server.filetransfer;

import com.chatter.server.Server;
import com.chatter.ui.console.ChatterConsole;
import java.net.BindException;
import java.nio.channels.SocketChannel;

/**
 * creates the File Transfer Server thread for receiving client notifications.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileTransferServer extends Server {

    /**
     * Constructor
     * @param port                           Port at which the server should run
     * @param chatterConsole                 ChatterConsole object for writing debug output
     * @param poolSize                       maximum clients that can be handled by server
     */
    public FileTransferServer(int port, ChatterConsole chatterConsole) {
        super(port, chatterConsole);
        super.setServerName("[File Transfer Server v1.0]");
    }

    /**
     * Constructor
     * @param chatterConsole                 ChatterConsole object for writing debug output
     * @param poolSize                       maximum clients that can be handled by server
     */
    public FileTransferServer(ChatterConsole chatterConsole, int poolSize) {
        super(chatterConsole);
        super.setServerName("[File Transfer Server v1.0]");
    }

    /**
     * starts the server.
     * @throws BindException
     */
    @Override
    public void startServer()
            throws BindException {
        super.writeOut((new StringBuilder()).append("starting ").append(getServerName()).append(" on port ").append(getPort()).toString());
        super.instantiateServer();
        start();
    }

    @Override
    public void run() {
        writeOut((new StringBuilder()).append(getServerName()).append(" started sucessfully listening on port ").append(getPort()).toString());
        do {
            if (!isStarted()) {
                break;
            }
            try {
                SocketChannel socketChannel = getServerSocketChannel().accept();
                FileTransferService s = new FileTransferService(getChatterConsole(), this, socketChannel);
                s.start();
            } catch (Exception e) {
                if (isDebug()) {
                    e.printStackTrace();
                }
            }
        } while (true);
    }

    public void releaseResources() {
        System.gc();
    }
}
