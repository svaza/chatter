 /*
 * =============================================================================
 * FileTransferService.java
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

import com.chatter.client.Client;
import com.chatter.protocol.ChatterProtocol;
import com.chatter.ui.console.ChatterConsole;
import com.chatter.core.FileMetadata;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * A thread for handling each file transfer request
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileTransferService extends Thread {

    private StringBuffer message;
    private String header;
    private int port;
    private String filePath;
    FileTransferServer fserver;
    ChatterConsole chatterConsole;
    private SocketChannel socketChannel;

    public FileTransferService(ChatterConsole chatterConsole, FileTransferServer fserver, SocketChannel socketChannel) {
        this.chatterConsole = chatterConsole;
        this.fserver = fserver;
        this.socketChannel = socketChannel;
    }

    public SocketChannel getSocketChannel() {
        return socketChannel;
    }

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getHeader() {
        return header;
    }

    public StringBuffer getMessage() {
        return message;
    }

    public int getPort() {
        return port;
    }

    public ChatterConsole getChatterConsole() {
        return chatterConsole;
    }

    private void service() throws Exception {

        StringBuilder sMessage = new StringBuilder();
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024 * 10);
        int read = -1;
        read = socketChannel.read(buffer);
        buffer.rewind();
        byte b[] = new byte[1024];
        buffer.get(b);
        sMessage.append(new String(b, 0, read));



        ChatterProtocol protocol = ChatterProtocol.getDecodeInstance(sMessage.toString());
        port = socketChannel.socket().getPort();
        fserver.getChatterConsole().println(protocol.getMessageBody());
        System.out.println(protocol);
        if (protocol.getSubject().equals(ChatterProtocol.REQUEST_FILE_METADATA)) {
            sendFileMetadata(protocol);
        } else if (protocol.getSubject().equals(ChatterProtocol.FILE_TRANSFER_REQUEST)) {
            sendFile(protocol);
        }
    }

    public void sendFileMetadata(ChatterProtocol protocol) {
        try {
            FileMetadata fm = new FileMetadata();
            File file = new File(protocol.getMessageBody());
            fm.setName(protocol.getMessageBody());
            if (file.canRead()) {
                fm.setStatusCode("OK");
                fm.setSize(file.length());
            } else {
                fm.setStatusCode("ERROR");
                fm.setSize(0);
            }
            Client client = new Client();
            client.setMode(Client.TEXT_MESSAGE);
            client.setMessageAsText(fm.toString());
            client.setChatterConsole(chatterConsole);
            client.setSocketChannel(socketChannel);
            client.sendMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (Exception e) {
                }
            }
        }
    }

    public void sendFile(ChatterProtocol protocol) {
        try {
            Client client = new Client();
            client.setMode(Client.FILE_TRANSFER);
            client.setSocketChannel(socketChannel);
            client.setMessageAsStream(new FileInputStream(protocol.getMessageBody()));
            client.setChatterConsole(chatterConsole);
            client.sendMessage(true);
        } catch (Exception e) {
            chatterConsole.println("Exception:" + e.getMessage());
            e.printStackTrace();
        } finally {
            if (socketChannel != null) {
                try {
                    socketChannel.close();
                } catch (Exception e) {
                }
            }
        }
    }

    @Override
    public void run() {
        try {
            service();
        } catch (Exception e) {
        }
        message = null;
        header = null;
        port = 0;
        filePath = null;
        chatterConsole = null;
        socketChannel = null;
        fserver.releaseResources();
    }
}
