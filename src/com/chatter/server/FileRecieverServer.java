 /*
 * =============================================================================
 * FileRecieverServer.java
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

import com.chatter.core.Services;
import com.chatter.protocol.ChatterProtocol;
import com.chatter.ui.console.ChatterConsole;
import com.chatter.work.receiver.FileRecieverHandler;
import java.io.InputStream;
import java.net.*;

/**
 * creates the File Reciever Thread thread for receiving client notifications.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileRecieverServer extends Server {

    /**
     * Constructor
     * @param port                                     Port at which the FileRecieverServer runs
     * @param chatterConsole                           
     * @param poolSize
     */
    public FileRecieverServer(int port, ChatterConsole chatterConsole) {
        super(port, chatterConsole);
        super.setServerName(Services.FILE_RECIEVER_SERVER);
    }

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
                recievedClientSocketChannel = serverSocketChannel.accept();
                InputStream inputMessage = recievedClientSocketChannel.socket().getInputStream();
                StringBuilder message = new StringBuilder();
                byte bMessage[] = new byte[1024];
                for (int read = -1; (read = inputMessage.read(bMessage)) != -1;) {
                    message.append(new String(bMessage, 0, read));
                }
                recievedClientSocketChannel.close();
                ChatterProtocol protocol = ChatterProtocol.getDecodeInstance(message.toString());
                if (protocol.getSubject().equals(ChatterProtocol.FILE_TRANSFER_APPROVAL)) {
                    FileRecieverHandler h = new FileRecieverHandler(this, new StringBuffer(protocol.getMessageBody()), recievedClientSocketChannel.socket().getInetAddress().getHostAddress());
                    h.start();
                }

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
