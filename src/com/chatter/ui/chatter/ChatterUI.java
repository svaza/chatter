 /*
 * =============================================================================
 * ChatterUI.java
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

import com.chatter.core.Users;
import com.chatter.core.Util;
import com.chatter.starter.Chatter;
import com.chatter.ui.console.ChatterConsole;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * The main UI for Chatter.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatterUI extends Thread {

    JTable chatterTable;
    JButton btnSend;
    JButton btnBrowse;
    public JTextArea txtChatArea;
    ChatterTableModel chatterTableModel;
    java.util.List<Users> users;
    JScrollPane chatterScrollPane;
    JScrollPane txtChatAreaPane;
    JFileChooser chooseFile;
    public final JFrame chatFrame = new JFrame();
    public StringBuffer handshakeMessage;
    ChatterConsole chatterConsole;
    private JTabbedPane tabWorkArea;
    private FileTransferPanel fileTransferTab;
    private ChatPanel chatTab;

    public ChatterUI() {
        handshakeMessage = new StringBuffer();
    }

    public FileTransferPanel getFileTransferTab() {
        if (fileTransferTab == null) {
            fileTransferTab = new FileTransferPanel();
        }
        return fileTransferTab;
    }

    public ChatPanel getChatTab() {
        if (chatTab == null) {
            chatTab = new ChatPanel();
        }
        return chatTab;
    }

    public JTabbedPane getTabWorkArea() {
        if (tabWorkArea == null) {
            tabWorkArea = new JTabbedPane();
            tabWorkArea.setFont(new Font("segoe ui", 0, 12));
            tabWorkArea.add(getFileTransferTab(), "File Transfer");
            tabWorkArea.add(getChatTab(), "Chat");
        }
        return tabWorkArea;
    }

    public JScrollPane getScrollChatterTable() {
        if (chatterScrollPane == null) {
            chatterScrollPane = new JScrollPane(getChatterTable());
            chatterScrollPane.setVerticalScrollBarPolicy(20);
            chatterScrollPane.setHorizontalScrollBarPolicy(30);
            chatterScrollPane.setMaximumSize(chatterScrollPane.getPreferredSize());
        }
        return chatterScrollPane;
    }

    public JTable getChatterTable() {
        if (chatterTable == null) {
            chatterTable = new JTable();
            users = Util.users;
            Object data[][] = new Object[users.size()][2];
            String columns[] = {
                "User Name", "IP Address"
            };
            for (int i = 0; i < users.size(); i++) {
                Users user = users.get(i);
                data[i][0] = user.getUserName();
                data[i][1] = user.getIPAddress();
            }

            chatterTableModel = new ChatterTableModel(columns, data);
            chatterTable.setModel(chatterTableModel);
            Font chatterTableFont = new Font("segoe ui", 0, 12);
            chatterTable.setFont(chatterTableFont);
            chatterTable.getTableHeader().setFont(chatterTableFont);
            chatterTable.setPreferredScrollableViewportSize(new Dimension(340, 150));
        }
        return chatterTable;
    }

    public void createAndShowGUI() {
        start();
    }

    @Override
    public void run() {
        Box bxtable = Box.createHorizontalBox();
        bxtable.add(Box.createHorizontalGlue());
        bxtable.add(getScrollChatterTable());
        bxtable.add(Box.createHorizontalGlue());
        Box bxtab = Box.createHorizontalBox();
        bxtab.add(getTabWorkArea());
        Box bxusers = Box.createVerticalBox();
        bxusers.add(bxtable);
        bxusers.add(Box.createVerticalStrut(5));
        bxusers.add(bxtab);
        bxusers.add(Box.createVerticalStrut(2));
        javax.swing.border.Border borderusers = BorderFactory.createEtchedBorder(1);
        bxusers.setBorder(borderusers);
        Box chat = Box.createVerticalBox();
        chat.add(bxusers);
        JPanel chatPanel = new JPanel();
        chatPanel.add(chat);
        chatFrame.add(chatPanel);
        chatFrame.pack();
        chatFrame.setTitle((new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" ").append("1.2").toString());
        chatFrame.setLocationRelativeTo(null);
        chatFrame.setResizable(false);
        chatFrame.setVisible(false);
        chatFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
        chatFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                chatFrame.setVisible(false);
            }
        });
    }

    public String getChatHistory() {
        return getChatTab().getChatHistory();
    }

    public String getChatMessageToSend() {
        return getChatTab().getChatMessageToSend();
    }
}
