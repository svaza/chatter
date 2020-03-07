 /*
 * =============================================================================
 * ChatterHandler.java
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
package com.chatter.ui.chatter;

import com.chatter.client.Client;
import com.chatter.core.Users;
import com.chatter.core.Util;
import com.chatter.protocol.ChatterException;
import com.chatter.protocol.ChatterProtocol;
import com.chatter.starter.Chatter;
import com.chatter.ui.console.ChatterConsole;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * The controller for ChatterUI
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatterHandler implements ActionListener, IChat {

    private JFileChooser chooseFile;
    private File filesToSend[];
    public StringBuilder handshakeMessage;
    public ChatterConsole chatterConsole;
    public StringBuilder path;
    private ChatterUI ui;
    private ChatAreaTracker caTracker;
    private Users currentUser;
    private ChatBoard chatBoard;
    private ChatterConsole viewHistory;

    /**
     * Constructor
     * @param console          :           The ChatterConsole object for writing the debug output
     */
    public ChatterHandler(ChatterConsole console) {
        initialize(console);
    }

    /**
     * perform the initialization operation
     * @param console          :           The ChatterConsole object for writing the debug output
     */
    private void initialize(ChatterConsole console) {
        handshakeMessage = null;
        path = null;
        chatterConsole = console;
        handshakeMessage = new StringBuilder();
        path = new StringBuilder();
        caTracker = new ChatAreaTracker(this);
        getChatterUI().getFileTransferTab().getBtnSend().addActionListener(this);
        getChatterUI().getFileTransferTab().getBtnBrowse().addActionListener(this);
        getChatterUI().getFileTransferTab().getBtnClear().addActionListener(this);
        getChatterUI().getChatTab().getMnuPopout().addActionListener(this);
        getChatterUI().getChatTab().getMnuLoadRefresh().addActionListener(this);
        getChatterUI().getChatTab().getMnuSave().addActionListener(this);
        getChatterUI().getChatTab().getMnuView().addActionListener(this);
        getChatterUI().getChatTab().getMnuClear().addActionListener(this);
        getChatterUI().getChatTab().getMnuClearHistory().addActionListener(this);
        ui.getChatTab().getTxtChatArea().addKeyListener(caTracker);
        chatBoard = new ChatBoard(this);
    }

    public ChatBoard getChatBoard() {
        return chatBoard;
    }

    public void setChatBoard(ChatBoard chatBoard) {
        this.chatBoard = chatBoard;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    /**
     * Sets the current user for chatting in main UI.
     * @param currentUser              :               The user to be set
     */
    public void setCurrentUser(Users currentUser) {
        this.currentUser = currentUser;
    }

    public ChatterUI getChatterUI() {
        if (ui == null) {
            ui = new ChatterUI();
        }
        return ui;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("SEND")) {
            sendFile();
        } else if (cmd.equals("CLEAR")) {
            clearFileTransferData();
        } else if (cmd.equals("BROWSE")) {
            showBrowser();
        } else if (cmd.equals("VIEW")) {
            viewChatHistory();
        } else if (cmd.equals("LOAD_REFRESH")) {
            loadCurrentSelectedUser();
            loadChatHistoryForCurrentUser();
        } else if (cmd.equals("SAVE")) {
            boolean loaded = loadCurrentSelectedUser();
            if (loaded) {
                Util.showSaveDialogAndSaveToFile(new StringBuffer(getChatterUI().getChatHistory()), chatterConsole, getChatterUI().chatFrame);
            }
        } else if (cmd.equals("POPOUT")) {
            boolean loaded = loadCurrentSelectedUser();
            if (loaded) {
                chatBoard.createORShowChatTabForUser(currentUser, true, true);
                chatBoard.getChatFrame().setVisible(true);
                chatBoard.getChatFrame().setExtendedState(0);
                chatBoard.getChatFrame().toFront();
            }
        } else if (cmd.equals("CLEAR_HISTORY")) {
            clearHistory();
        } else if (cmd.equals("CLEAR")) {
            getChatterUI().getChatTab().setChatHistory("");
        }
    }

    private void clearFileTransferData() {
        handshakeMessage = null;
        handshakeMessage = new StringBuilder();
        path = new StringBuilder();
        getChatterUI().getFileTransferTab().getTxtChatArea().setText("");
    }

    private void clearHistory() {
        boolean loaded = loadCurrentSelectedUser();
        if (loaded) {
            boolean sel = Util.showYesNoDialog(getChatterUI().chatFrame, true, getOptionLabel("This will delete your chat history. Do you want to continue?"), "Delete Chat History?");
            if (sel) {
                ChatCache.clearHistory(currentUser);
            }
        }
    }

    private JLabel getOptionLabel(String message) {
        JLabel lab = new JLabel(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        return lab;
    }

    private void viewChatHistory() {
        boolean loaded = loadCurrentSelectedUser();
        if (loaded) {
            if (viewHistory == null) {
                viewHistory = new ChatterConsole(ChatterConsole.DISPOSE_ON_CLOSE);
                viewHistory.getBtnSave().setVisible(false);
                viewHistory.getBtnClear().setVisible(false);
                viewHistory.getTxtConsole().setFont(new Font("segoe ui", Font.PLAIN, 12));
                String title = (new StringBuilder()).append(currentUser.getUserName()).append("-").append(currentUser.getIPAddress()).toString();
                viewHistory.getFrmConsole().setTitle(title);
                viewHistory.getFrmConsole().setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource("/images/ChatterTray.gif")));
                viewHistory.createAndShowGUI();
            }
            viewHistory.clearConsole();
            viewHistory.print(getChatterUI().getChatHistory());
            viewHistory.showConsole();
        }
    }

    private boolean loadCurrentSelectedUser() {
        int idx = getChatterUI().getChatterTable().getSelectedRow();
        if (idx != -1) {
            currentUser = Util.users.get(idx);
            String userInfo = (new StringBuilder()).append("<html><u>").append(currentUser.getUserName()).append(":<font color=\"blue\">").append(currentUser.getIPAddress()).append("</font></u>").toString();
            getChatterUI().getChatTab().getLabUserInfo().setText(userInfo);
            getChatterUI().getChatTab().getLabUserInfo().setIcon((Icon) Chatter.imageResource.get("appln.connected-image"));
            getChatterUI().getChatTab().getLabUserInfo().setCursor(new Cursor(12));
            return true;
        } else {
            Util.showInformationDialog(getChatterUI().chatFrame, true, getOptionLabel("Please select user.             "), "Error", false);
            return false;
        }
    }

    private void loadChatHistoryForCurrentUser() {
        int idx = getChatterUI().getChatterTable().getSelectedRow();
        if (idx != -1) {
            ChatHistory ch = ChatCache.findHistory(currentUser);
            getChatterUI().getChatTab().setChatHistory(ch.toString());
            getChatterUI().getChatTab().moveChatHistoryScrollBarAtEnd();
        }
    }

    private void showBrowser() {
        chooseFile = new JFileChooser();
        chooseFile.setDialogType(0);
        chooseFile.setFileSelectionMode(2);
        chooseFile.setMultiSelectionEnabled(true);
        chooseFile.setApproveButtonText("Scan and Add");
        int userSelection = chooseFile.showOpenDialog(getChatterUI().chatFrame);
        if (userSelection == 0) {
            filesToSend = chooseFile.getSelectedFiles();
            getChatterUI().getFileTransferTab().getBtnBrowse().setEnabled(false);
            getChatterUI().getFileTransferTab().getBtnClear().setEnabled(false);
            getChatterUI().getFileTransferTab().getBtnSend().setEnabled(false);
            FileScannerHandler filehandler = new FileScannerHandler(this, filesToSend);
            filehandler.start();
        }
    }

    private boolean sendFile() {
        boolean state = false;
        StringBuilder exceptionMessage = new StringBuilder("<html>Cannot send files to following users : <br/>");
        int selectedRows[] = getChatterUI().getChatterTable().getSelectedRows();
        if (selectedRows.length > 0) {
            //check whether user has selected any user
            if (getChatterUI().getFileTransferTab().getTxtChatArea().getText().length() > 0) {
                ChatterProtocol protocol = null;
                try {
                    protocol = ChatterProtocol.getEncodeInstance(handshakeMessage.toString(), ChatterProtocol.FILE_TRANSFER_APPROVAL);
                } catch (ChatterException ex) {
                }
                //check whether user has selected any files to transfer
                for (int i = 0; i < selectedRows.length; i++) {
                    Users user = Util.users.get(selectedRows[i]);
                    try {
                        InetAddress iAddress = InetAddress.getByAddress(Util.getBytes(user.getIPAddress()));
                        Client client = new Client(iAddress, user.getFileRecieverServerPort(), Client.TEXT_MESSAGE, chatterConsole);
                        client.setMessageAsText(protocol.toString());
                        client.connect();
                        client.sendMessage(true);
                    } catch (Exception e) {
                        state = true;
                        exceptionMessage.append(user.getUserName());
                        exceptionMessage.append("&lt;");
                        exceptionMessage.append(user.getIPAddress());
                        exceptionMessage.append("&gt;");
                        exceptionMessage.append("<br/>");
                        chatterConsole.println("Exception while sending file to:\n " + user);
                    }
                }
            } else {
                Util.showInformationDialog(getChatterUI().chatFrame, true, getOptionLabel("Please select files to transfer"), "Error", false);
            }
        } else {
            Util.showInformationDialog(getChatterUI().chatFrame, true, getOptionLabel("Please select user             "), "Error", false);
        }
        //check whether if any exception has occurred
        if (state) {
            exceptionMessage.append("<br/>");
            exceptionMessage.append("check whether user is over LAN or check IP address of user.");
            Util.showInformationDialog(getChatterUI().chatFrame, true, Chatter.getOptionMessage(exceptionMessage.toString(),350,150), "Error", false);
        }
        return state;
    }

    public void sendMessage() {
        int idx = getChatterUI().getChatterTable().getSelectedRow();
        if (idx != -1) {
            StringBuilder exceptionMessage = new StringBuilder("<html>Cannot send chat message to following user : <br/>");
            try {
                loadCurrentSelectedUser();
                String sOriginalMessage = getChatterUI().getChatTab().getChatMessageToSend();
                String message = (new StringBuilder()).append("Me:-\n").append(sOriginalMessage).append("\n").append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").append("\n").toString();
                getChatterUI().getChatTab().appendHistory(message);
                getChatterUI().getChatTab().moveChatHistoryScrollBarAtEnd();
                getChatterUI().getChatTab().getTxtChatArea().setText("");
                List<Users> boardusers = getChatBoard().getvUsers();
                for (int i = 0; i < boardusers.size(); i++) {
                    Users buser = (Users) boardusers.get(i);
                    if (buser.getID() == currentUser.getID()) {
                        ChatPanel panel = (ChatPanel) getChatBoard().getChatTabbs().getComponentAt(i);
                        panel.getTxtChatHistoryArea().append(message);
                    }
                }
                String ip = currentUser.getIPAddress();
                int chat_port = currentUser.getServerPort();
                ChatData cdata = new ChatData();
                cdata.setTime(new Date());
                cdata.setMessage(sOriginalMessage);
                cdata.setMe(true);
                ChatCache.addChatData(cdata, currentUser);
                InetAddress iAddress = InetAddress.getByAddress(Util.getBytes(ip));
                ChatterProtocol protocol = ChatterProtocol.getEncodeInstance(sOriginalMessage, ChatterProtocol.CHAT_MESSAGE);
                protocol.addHeader("Date", new Date().toString());
                Client client = new Client(iAddress, chat_port, Client.TEXT_MESSAGE, chatterConsole);
                client.setMessageAsText(protocol.toString());
                client.connect();
                client.sendMessage(true);
            } catch (Exception e) {
                exceptionMessage.append(currentUser.getUserName());
                exceptionMessage.append("&lt;");
                exceptionMessage.append(currentUser.getIPAddress());
                exceptionMessage.append("&gt;");
                exceptionMessage.append("<br/>");
                exceptionMessage.append("<br/>");
                exceptionMessage.append("check whether user is over LAN or check IP address of user.");
                Util.showInformationDialog(getChatterUI().chatFrame, true, Chatter.getOptionMessage(exceptionMessage.toString(),350,150), "Error", false);
                chatterConsole.println((new StringBuilder()).append("Error while sending chat message-->").append(e.getMessage()).toString());
                chatterConsole.println("User Details are:-");
                chatterConsole.println((Users) Util.users.get(idx));
            }
        } else {
            Util.showInformationDialog(getChatterUI().chatFrame, true, getOptionLabel("Please connect to user             "), "Error", false);
        }
        //check whether if any exception has occurred

    }
}
