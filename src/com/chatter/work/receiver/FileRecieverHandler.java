/*
 * =============================================================================
 * FileRecieverHandler.java
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
package com.chatter.work.receiver;

import com.chatter.client.Client;
import com.chatter.core.Services;
import com.chatter.core.Users;
import com.chatter.core.Util;
import com.chatter.protocol.ChatterProtocol;
import com.chatter.server.FileRecieverServer;
import com.chatter.starter.Chatter;
import com.chatter.ui.console.ChatterConsole;
import com.chatter.core.FileMetadata;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.util.*;
import javax.swing.*;

/**
 * This class performs complete file recieiving operation for a client.
 * @author Santosh Vaza
 */
public class FileRecieverHandler extends Thread implements ActionListener {

    boolean downloadFlag = false;
    boolean isException = false;
    /**
     * the message sent by the file requester it will be in following format
     * <full path>  \n
     * <size>       \n
     * <parent directory>  \n
     */
    StringBuffer message;
    /**
     * instance of FileRecieverServer.
     */
    FileRecieverServer fileReceverServer;
    /**
     * instance of FileRecieverUI object that notifies the user about number of files downloaded.
     */
    FileRecieverUI chatter;
    /**
     * specifies the storage area,which is selected by user for storing files.
     */
    String storageArea;
    /**
     * Users Instance that encapsulates all attributes of sender.
     */
    Users sender;
    ChatterConsole chatterConsole;
    /**
     * total number of files downloaded.
     */
    long totalFilesDownloaded;
    /**
     * file size of total files.
     */
    double totalDownloadedFilesSize;
    String totalDownloadFileSize;
    String transmissionErrorMessage;
    String transmissionErrorMessageHTML;
    ImageIcon chatterIcon;
    /**
     * FileReciever threads that will perform file recieiving operation.
     */
    long downloadCounter;
    long totalFiles;
    FileDownloader fd;

    public FileRecieverHandler(FileRecieverServer fileRecieverServer, StringBuffer message,
            String senderipaddress) {
        totalFilesDownloaded = 0;
        transmissionErrorMessage = "";
        transmissionErrorMessageHTML =
                "<html>An Error occurred at File Sender when requesting following files.<br><hr>";
        chatterIcon = (ImageIcon) Chatter.imageResource.get("appln.main-image");
        fileReceverServer = fileRecieverServer;
        this.message = message;
        List<Users> list = Util.searchByIPAddress(senderipaddress, Util.users);
        sender = list.get(0);
        chatterConsole = fileRecieverServer.getChatterConsole();
        fd = new FileDownloader();
    }

    private void initializeUI() {
        List<Users> users = Util.users;
        String userName = "";
        int j = 0;
        do {
            if (j >= users.size()) {
                break;
            }
            Users user = users.get(j);
            if (user.getIPAddress().equals(sender.getIPAddress())) {
                userName = user.getUserName();
                break;
            }
            j++;
        } while (true);
        chatter = new FileRecieverUI(message.toString());
        chatter.title =
                (new StringBuilder()).append("Message from ").append(userName).toString();
        chatter.btnAccept.addActionListener(this);
        chatter.btnCancel.addActionListener(this);
        chatter.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ACCEPT")) {
            JFileChooser chooseFile = new JFileChooser();
            chooseFile.setApproveButtonText("Select");
            chooseFile.setDialogTitle("Select storage folder");
            chooseFile.setFileSelectionMode(1);
            chooseFile.setMultiSelectionEnabled(false);
            chooseFile.setFont(new Font("segoe ui", 0, 12));
            int userSelection = chooseFile.showOpenDialog(chatter.frm);
            if (userSelection == JFileChooser.APPROVE_OPTION) {
                storageArea = chooseFile.getSelectedFile().getPath();
                for (int i = 0; i < chatter.vVirtualPath.size(); i++) {
                    String folder =
                            (new StringBuilder()).append(storageArea).append(System.getProperty("file.separator")).append(chatter.vVirtualPath.get(i)).toString();
                    (new File(folder)).mkdirs();
                }

                double totalFileSize = 0.0D;
                for (int i = 0; i < chatter.vfileSize.size(); i++) {
                    double l =
                            (new Double(chatter.vfileSize.get(i).toString())).doubleValue();
                    totalFileSize += l;
                }

                totalFileSize = Util.BytestoMB(totalFileSize);
                totalDownloadFileSize =
                        (new Formatter()).format("%.2f", new Object[]{Double.valueOf(totalFileSize)}).toString();
                chatter.setBtnAcceptVisibility(false);
                int temp =
                        (chatter.vfileSize.size() + (new Double(totalDownloadFileSize)).intValue())
                        / 2;
                chatter.fileDownloadProgress.setMaximum(temp);
                chatter.initializeTableRendererForDownload();
                chatter.setBtnCancelCommand("CANCEL_FILE_TRANSFER");
                totalFiles = chatter.vpaths.size();
                goNext();
            }
        } else if (e.getActionCommand().equals("CANCEL")) {
            chatter.frm.dispose();
            storageArea = "";
        } else if (e.getActionCommand().equals("CANCEL_FILE_TRANSFER")) {
            confirmStopFileTransfer("<html>Stop file transfer ?<br>Note:download will stop after current file is downloaded.");
        } else if (e.getActionCommand().equals("ON_COMPLETE")) {
            chatter.frm.dispose();
            releaseResources();
        }
    }

    public void goNext() {
        try {
            if (totalFilesDownloaded < totalFiles && !downloadFlag) {
                fd = null;
                fd = new FileDownloader();
                fd.start();
            } else {
                downloadFlag = true;
                chatter.btnCancel.setText("Download Complete");
                chatter.btnCancel.setActionCommand("ON_COMPLETE");
                if (isException) {
                    chatterConsole.println("\n");
                    chatterConsole.println("\n");
                    chatterConsole.println("\n");
                    chatterConsole.println("\n");
                    chatterConsole.println("==================================================================================================================================================================");
                    chatterConsole.println(transmissionErrorMessage);
                    chatterConsole.println("==================================================================================================================================================================");
                    Util.showInformationDialog(chatter.frm, true,
                            Chatter.getOptionMessage(transmissionErrorMessageHTML),
                            (new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" ").append(Services.VERSION).toString(),
                            false);
                }
            }
        } catch (Exception e) {
            chatterConsole.println(e);
        }
    }

    public void releaseResources() {
        synchronized (this) {
            message = null;
            chatter = null;
            storageArea = null;
            sender = null;
            chatterConsole = null;
            totalFilesDownloaded = 0;
            totalDownloadedFilesSize = 0;
            totalDownloadFileSize = null;
            transmissionErrorMessage = null;
            transmissionErrorMessageHTML = null;
            chatterIcon = null;
            downloadCounter = 0;
            totalFiles = 0;
            fd = null;
            fileReceverServer.releaseResources();
        }
    }

    public void confirmStopFileTransfer(String message) {
        System.out.println("Stop Download ?");
        boolean sel =
                Util.showYesNoDialog(chatter.frm, true, getOptionLabel(message),
                "Stop file transfer?");
        if (sel) {
            if (downloadFlag) {
                Util.showInformationDialog(chatter.frm, true,
                        getOptionLabel("Download already completed.     "),
                        (new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" ").append("1.2").toString(),
                        false);
            } else {
                System.out.println("Stop Download ?:YES");
                downloadFlag = true;
                chatter.getLabDownloadCancelled().setVisible(true);
                chatter.fileDownloadProgress.setVisible(false);
                chatter.btnCancel.setVisible(true);
                chatter.btnAccept.setVisible(false);
            }
        }
    }

    private JLabel getOptionLabel(String message) {
        JLabel lab = new JLabel(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        return lab;
    }

    public void run() {
        initializeUI();
    }

    class FileDownloader extends Thread {

        public void run() {
            requestFileMetaData();
        }

        private void updateProgressString() {
            String destination =
                    (new Formatter()).format("%.2f", Double.valueOf(Util.BytestoMB(totalDownloadedFilesSize))).toString();
            int temp =
                    ((new Double(destination)).intValue() + (int) totalFilesDownloaded)
                    / 2;

            StringBuilder sPrgMessage = new StringBuilder();
            sPrgMessage.append("Downloaded (");
            sPrgMessage.append(destination);
            sPrgMessage.append(" / ");
            sPrgMessage.append(totalDownloadFileSize);
            sPrgMessage.append(" MB) & (");
            sPrgMessage.append(totalFilesDownloaded);
            sPrgMessage.append(" / ");
            sPrgMessage.append(totalFiles);
            sPrgMessage.append(" Files).");

            chatter.setProgressBarString(sPrgMessage.toString());
            chatter.setProgressBarValue(temp);
        }

        /**
         * PHASE1
         * Tells server to send file metadata.
         * File Request Format:
         *
         * <request-file-metadata>\n
         * <file-path> \n
         *
         */
        public void requestFileMetaData() {
            try {
                InetAddress iAddress =
                        InetAddress.getByAddress(Util.getBytes(sender.getIPAddress()));
                StringBuilder sMessage = new StringBuilder();
                sMessage.append(chatter.getFilePathAt(new Long(totalFilesDownloaded).intValue()));
                sMessage.append("\n");
                ChatterProtocol protocol = ChatterProtocol.getEncodeInstance(sMessage.toString(), ChatterProtocol.REQUEST_FILE_METADATA);
                protocol.addHeader("Date", new Date().toString());

                SocketChannel sc =
                        SocketChannel.open(new InetSocketAddress(iAddress,
                        sender.getFileTransferServerPort()));
                ByteBuffer buffer =
                        ByteBuffer.wrap(protocol.toString().getBytes());
                sc.write(buffer);
                requestFile(sc);
            } catch (Exception s2) {
                chatterConsole.println(s2);
                s2.printStackTrace();
                goNext();
            }
        }

        /**
         * PHASE2 :
         * Recieves and parses file metadata.
         * Then based on response it will request file from server.
         * File Request Format:
         *
         * <file-request> \n
         * <file-path> \n
         */
        public void requestFile(SocketChannel sc) {
            try {
                ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
                int read = sc.read(buffer);
                buffer.rewind();
                byte b[] = new byte[1024];
                buffer.get(b);
                sc.close();

                StringBuilder sMessage = new StringBuilder(new String(b, 0, read));

                FileMetadata fm = getFileMetaData(sMessage.toString());
                if (fm.getStatusCode().equals("OK")) {

                    sMessage = new StringBuilder();
                    sMessage.append(chatter.getFilePathAt(new Long(totalFilesDownloaded).intValue()));
                    sMessage.append("\n");

                    ChatterProtocol protocol = ChatterProtocol.getEncodeInstance(sMessage.toString(), ChatterProtocol.FILE_TRANSFER_REQUEST);
                    protocol.addHeader("Date", new Date().toString());

                    InetAddress iAddress =
                            InetAddress.getByAddress(Util.getBytes(sender.getIPAddress()));
                    sc = SocketChannel.open(new InetSocketAddress(iAddress, sender.getFileTransferServerPort()));
                    buffer = ByteBuffer.wrap(protocol.toString().getBytes());
                    sc.write(buffer);
                    recieveStream(sc, fm.getSize());
                } else {
                    System.out.println("Exception:" + fm);
                    totalDownloadedFilesSize += fm.getSize();
                    System.out.println(totalFilesDownloaded + ":"
                            + totalFiles);
                    totalFilesDownloaded++;
                    isException = true;
                    transmissionErrorMessageHTML += fm.getName() + "<br/>";
                    transmissionErrorMessage += fm.getName() + "\n";
                    updateProgressString();
                    goNext();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public FileMetadata getFileMetaData(String metaString) {
            FileMetadata fm = new FileMetadata();
            String metaStr[] = metaString.split("\\n");
            String statusCode =
                    metaStr[0].substring(metaStr[0].indexOf(":") + 1,
                    metaStr[0].length());
            String fileName =
                    metaStr[1].substring(metaStr[1].indexOf(":") + 1, metaStr[1].length());
            String fileSize =
                    metaStr[2].substring(metaStr[2].indexOf(":") + 1, metaStr[2].length());
            fm.setName(fileName);
            fm.setSize(new Long(fileSize));
            fm.setStatusCode(statusCode);
            return fm;
        }

        public void recieveStream(SocketChannel sc, long fileSize) {
            StringBuilder sFullPath = new StringBuilder();
            sFullPath.append(storageArea);
            sFullPath.append(System.getProperty("file.separator"));
            sFullPath.append(chatter.getParentFolderAt((new Long(totalFilesDownloaded).intValue())));
            sFullPath.append(System.getProperty("file.separator"));
            sFullPath.append(new File(chatter.getFilePathAt(new Long(totalFilesDownloaded).intValue()).toString()).getName());
            File fRoot = new File(sFullPath.toString());
            try {
                fRoot.getParentFile().mkdirs();
                FileOutputStream fos = new FileOutputStream(fRoot.getPath());
                FileChannel fc = fos.getChannel();
                long bytesTransferred = 0;
                while (bytesTransferred < fileSize) {
                    long bufferSize = Client.BUFFER_SIZE;
                    if ((fileSize - bytesTransferred) < bufferSize) {
                        bufferSize = (int) (fileSize - bytesTransferred);
                        if (bufferSize <= 0) {
                            bufferSize = (int) fileSize;
                        }
                    }
                    long bytesRead =
                            fc.transferFrom(sc, bytesTransferred, bufferSize);
                    totalDownloadedFilesSize += bytesRead;
                    updateProgressString();
                    if (bytesRead > 0) {
                        bytesTransferred += bytesRead;
                    }
                }
                totalFilesDownloaded++;
                updateProgressString();
                fc.close();
                fos.close();
                sc.close();
            } catch (Exception e) {
                chatterConsole.println(e);
                e.printStackTrace();
            } finally {
                goNext();
            }
        }
    }
}
