 /*
 * =============================================================================
 * Users.java
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
package com.chatter.core;

import java.io.Serializable;

/**
 * Encapsulates each user configuration.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class Users
        implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * IPAddress of user
     */
    private String iPAddress;
    /**
     * The name of user for identifying the user.
     */
    private String userName;
    /**
     * Chat server port of user.
     */
    private int serverPort;
    /**
     * Handshake server port of user
     */
    private int handShakeServerPort;
    /**
     * File Transfer Server port of user.
     */
    private int fileTransferServerPort;
    /**
     * Port for controlling the instance of application.
     * When the application runs it binds at this port and does not allows
     * other instance of application to bind.
     */
    private int instancePort;
    /**
     * The buffer size of data chunk.
     */
    private int bufferSize;
    /**
     * The port at which the application will recieve files.
     */
    private int fileRecieverServerPort;
    /**
     * The size of pool, means the maximum number of users the application can
     * serve.
     */
    private int poolSize;
    /**
     * The status of user.
     */
    private String status;
    /**
     * The unique ID assigned to user
     */
    private int ID;
    /**
     * The size of download queue, means the number of files the application will
     * download asychronously.
     */
    private int downloadQueueSize;

    /**
     * default constructor for initializing the default user variables.
     */
    public Users() {
        userName = "Anonymous";
        poolSize = 100;
        serverPort = 20000;
        handShakeServerPort = 20001;
        fileTransferServerPort = 20002;
        fileRecieverServerPort = 20003;
        downloadQueueSize = 1;
        instancePort=20004;
        bufferSize=1;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getFileTransferServerPort() {
        return fileTransferServerPort;
    }

    public int getPoolSize() {
        return poolSize;
    }

    public void setPoolSize(int poolSize) {
        this.poolSize = poolSize;
    }

    public int getFileRecieverServerPort() {
        return fileRecieverServerPort;
    }

    public void setFileRecieverServerPort(int fileRecieverServerPort) {
        this.fileRecieverServerPort = fileRecieverServerPort;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    public int getInstancePort() {
        return instancePort;
    }

    public void setInstancePort(int instancePort) {
        this.instancePort = instancePort;
    }

    public void setFileTransferServerPort(int fileTransferServerPort) {
        this.fileTransferServerPort = fileTransferServerPort;
    }

    public int getHandShakeServerPort() {
        return handShakeServerPort;
    }

    public void setHandShakeServerPort(int handShakeServerPort) {
        this.handShakeServerPort = handShakeServerPort;
    }

    public String getIPAddress() {
        return iPAddress;
    }

    public void setIPAddress(String iPAddress) {
        this.iPAddress = iPAddress;
    }

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort(int serverPort) {
        this.serverPort = serverPort;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public int getDownloadQueueSize() {
        return downloadQueueSize;
    }

    public void setDownloadQueueSize(int downloadQueueSize) {
        this.downloadQueueSize = downloadQueueSize;
    }

    public String toString() {
        return (new StringBuilder()).append("==========================================\nUser Name:").append(getUserName()).append("\n").append("Ip-Address:").append(getIPAddress()).append("\n").append("Buffer Size:").append(getBufferSize()).append("\n").append("File Reciever Server Port:").append(getFileRecieverServerPort()).append("\n").append("File Transfer Server Port:").append(getFileTransferServerPort()).append("\n").append("Handshake Server Port:").append(getHandShakeServerPort()).append("\n").append("Instance Port:").append(getInstancePort()).append("\n").append("Pool Size:").append(getPoolSize()).append("\n").append("Server Port:").append(getServerPort()).append("\n").append("Download Queue Size:").append(getDownloadQueueSize()).append("\n").append("==========================================").toString();
    }
}
