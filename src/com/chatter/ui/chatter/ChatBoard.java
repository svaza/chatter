 /*
 * =============================================================================
 * ChatBoard.java
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
import com.chatter.protocol.ChatterProtocol;
import com.chatter.starter.Chatter;
import com.chatter.ui.console.ChatterConsole;
import com.thirdparty.chatter.ui.chatboard.ButtonTabComponent;
import java.awt.*;
import java.awt.event.*;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import javax.swing.*;

/**
 * This class is a popup board for chatting with multiple users.
 * @author Santosh Vaza
 */
public class ChatBoard
        implements IChat, ActionListener {

    protected List<Users> vUsers;
    protected Font fnt;
    protected JTabbedPane chatTabbs;
    protected JFrame chatFrame;
    protected ChatAreaTracker caTracker;
    protected ChatterHandler handler;
    protected JPopupMenu popChatBoard;
    protected ChatterConsole viewHistory;
    protected JMenuItem mnuLoadRefresh;
    protected JMenuItem mnuSave;
    protected JMenuItem mnuView;
    protected JMenuItem mnuClear;
    protected JMenuItem mnuClearHistory;
    protected JMenuItem mnuTClose;
    protected ImageIcon chatterIcon;
    protected JButton btnTabButton;

    /**
     *  Constructor
     * @param handler         :        ChatterHandler Object
     */
    ChatBoard(ChatterHandler handler) {
        initialize(handler);
    }

    private void initialize(ChatterHandler handler){
        fnt = new Font("segoe ui", Font.PLAIN, 12);
        chatterIcon = (ImageIcon) Chatter.imageResource.get("appln.main-image");
        getChatTabbs();
        vUsers = new ArrayList<Users>();
        caTracker = new ChatAreaTracker(this);
        this.handler = handler;
    }

    /**
     * 
     * @return  vUsers      :    Users handled by chat board.
     */
    public List<Users> getvUsers() {
        return vUsers;
    }

    /**
     * @return   chatTabbs   :  JTabbedPane  object in chatboard
     */
    public JTabbedPane getChatTabbs() {
        if (chatTabbs == null) {
            chatTabbs = new JTabbedPane();
            chatTabbs.setFont(fnt);
        }
        return chatTabbs;
    }

    /**
     * @return   chatFrame   :   ChatBoard Frame
     */
    public JFrame getChatFrame() {
        if (chatFrame == null) {
            chatFrame = new JFrame();
            Box bx = Box.createHorizontalBox();
            bx.add(getChatTabbs());
            chatFrame.add(bx);
            chatFrame.pack();
            chatFrame.setResizable(false);
            chatFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            chatFrame.setTitle("Chat Board");
            chatFrame.setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
            chatFrame.addWindowListener(new WindowAdapter() {

                public void windowClosing(WindowEvent e) {
                    chatFrame.setVisible(false);
                }
            });
        }
        return chatFrame;
    }

    /**
     * 
     * @param user                 :         User Object for which the tab is to be shown in chatBoard
     * @param show                 :         Show the ChatBoard after adding the user
     * @param takeHistory          :         Should take the history from handler.
     */
    public void createORShowChatTabForUser(Users user, boolean show, boolean takeHistory) {
        boolean flag = false;
        for (int i = 0; i < vUsers.size(); i++) {
            Users vu = vUsers.get(i);
            if (vu.getIPAddress().equals(user.getIPAddress())) {
                getChatTabbs().setSelectedIndex(i);
                ChatHistory ch = ChatCache.findHistory(user);
                ChatPanel chatPanel = (ChatPanel) getChatTabbs().getSelectedComponent();
                if (takeHistory) {
                    chatPanel.setChatHistory(ch.toString());
                    chatPanel.moveChatHistoryScrollBarAtEnd();
                }
                flag = true;
                break;
            }
        }

        if (!flag) {
            ChatPanel chatPanel = new ChatPanel();
            chatPanel.getTxtChatHistoryArea().setColumns(80);
            chatPanel.getTxtChatHistoryArea().setRows(30);
            chatPanel.getLabUserInfo().setVisible(false);
            chatPanel.getTxtChatArea().addKeyListener(caTracker);
            chatPanel.setPopChatTsk(getPopChatBoard(chatPanel));
            vUsers.add(user);

            ChatHistory ch = ChatCache.findHistory(user);
            if (takeHistory) {
                chatPanel.setChatHistory(ch.toString());
            }
            getChatTabbs().addTab(user.getUserName(),  chatPanel);
            getChatTabbs().setTabComponentAt(getChatTabbs().getTabCount() - 1,new ButtonTabComponent(this));
            getChatFrame().pack();
            getChatTabbs().setSelectedIndex(getChatTabbs().getTabCount() - 1);
        }
        getChatFrame().setVisible(show);
        getChatFrame().toFront();
    }

    public void selectTab(Users user) {
        int i = 0;
        do {
            if (i >= vUsers.size()) {
                break;
            }
            Users cv = (Users) vUsers.get(i);
            if (cv.getID() == user.getID()) {
                getChatTabbs().setSelectedIndex(i);
                break;
            }
            i++;
        } while (true);
    }

    public void closeCurrentTab() {
        int idx = getChatTabbs().getSelectedIndex();
        vUsers.remove(idx);
        getChatTabbs().remove(idx);
    }
    public void closeTab(int idx) {
        vUsers.remove(idx);
        getChatTabbs().remove(idx);
    }

    public void sendMessage() {
        Users cuser = vUsers.get(getChatTabbs().getSelectedIndex());
        try {
            ChatPanel panel = (ChatPanel) getChatTabbs().getSelectedComponent();
            String sOriginalMessage = panel.getTxtChatArea().getText();
            String message = (new StringBuilder()).append("Me:-\n").append(sOriginalMessage).append("\n").append("--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------").append("\n").toString();
            panel.getTxtChatHistoryArea().append(message);
            panel.getTxtChatHistoryArea().setCaretPosition(panel.getTxtChatHistoryArea().getText().length());
            if (handler.getCurrentUser() != null) {
                if (cuser.getIPAddress().equals(handler.getCurrentUser().getIPAddress())) {
                    handler.getChatterUI().getChatTab().appendHistory(message);
                    handler.getChatterUI().getChatTab().moveChatHistoryScrollBarAtEnd();
                }
            }
            panel.getTxtChatArea().setText("");
            String ip = cuser.getIPAddress();
            int chat_port = cuser.getServerPort();
            ChatData cdata = new ChatData();
            cdata.setTime(new Date());
            cdata.setMessage(sOriginalMessage);
            cdata.setMe(true);
            ChatCache.addChatData(cdata, cuser);
            ChatterProtocol protocol = ChatterProtocol.getEncodeInstance(sOriginalMessage, ChatterProtocol.CHAT_MESSAGE);
            protocol.addHeader("Date", new Date().toString());
            InetAddress iAddress = InetAddress.getByAddress(Util.getBytes(ip));
            Client client = new Client(iAddress, chat_port, 101, handler.chatterConsole);
            client.setMessageAsText(protocol.toString());
            client.connect();
            client.sendMessage(true);
        } catch (Exception e) {
            e.printStackTrace();
            handler.chatterConsole.println((new StringBuilder()).append("Error while sending chat message-->").append(e.getMessage()).toString());
            handler.chatterConsole.println("User Details are:-");
            handler.chatterConsole.println(Util.users.get(cuser.getID()));
        }
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();
        if (cmd.equals("VIEW")) {
            viewChatHistory();
        } else if (cmd.equals("LOAD_REFRESH")) {
            loadChatHistoryForCurrentUser();
        } else if (cmd.equals("SAVE")) {
            Util.showSaveDialogAndSaveToFile(new StringBuffer(handler.getChatterUI().getChatHistory()), handler.chatterConsole, getChatFrame());
        } else if (cmd.equals("CLEAR")) {
            clear();
        } else if (cmd.equals("CLEAR_HISTORY")) {
            clearHistory();
        }
    }

    private void clear() {
        ChatPanel cp = (ChatPanel) getChatTabbs().getSelectedComponent();
        cp.setChatHistory("");
    }

    private void clearHistory() {
        Users currentUser = (Users) vUsers.get(getChatTabbs().getSelectedIndex());
        boolean sel = Util.showYesNoDialog(getChatFrame(), true, getOptionLabel("This will delete your chat history. Do you want to continue?"), "Delete Chat History?");
        if (sel) {
            ChatCache.clearHistory(currentUser);
        }
    }

    private JLabel getOptionLabel(String message) {
        JLabel lab = new JLabel(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        return lab;
    }

    private void viewChatHistory() {
        if (viewHistory == null) {
            viewHistory = new ChatterConsole(ChatterConsole.DISPOSE_ON_CLOSE);
            viewHistory.getBtnSave().setVisible(false);
            viewHistory.getBtnClear().setVisible(false);
            viewHistory.getTxtConsole().setFont(fnt);
            int idx = getChatTabbs().getSelectedIndex();
            if (idx != -1) {
                Users currentUser = (Users) vUsers.get(idx);
                String title = (new StringBuilder()).append(currentUser.getUserName()).append("-").append(currentUser.getIPAddress()).toString();
                viewHistory.getFrmConsole().setTitle(title);
                viewHistory.getFrmConsole().setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
            }
            viewHistory.createAndShowGUI();
        }
        ChatPanel chatPanel = (ChatPanel) getChatTabbs().getSelectedComponent();
        viewHistory.clearConsole();
        viewHistory.print(chatPanel.getChatHistory());
        viewHistory.showConsole();
    }

    public JPopupMenu getPopChatBoard(ChatPanel panel) {
        if (popChatBoard == null) {
            popChatBoard = new JPopupMenu();
            popChatBoard.setBorderPainted(true);
            if (mnuClear == null) {
                mnuClear = panel.getMnuClear();
                mnuClear.addActionListener(this);
            }
            if (mnuClearHistory == null) {
                mnuClearHistory = panel.getMnuClearHistory();
                mnuClearHistory.addActionListener(this);
            }
            if (mnuLoadRefresh == null) {
                mnuLoadRefresh = panel.getMnuLoadRefresh();
                mnuLoadRefresh.addActionListener(this);
            }
            if (mnuSave == null) {
                mnuSave = panel.getMnuSave();
                mnuSave.addActionListener(this);
            }
            if (mnuView == null) {
                mnuView = panel.getMnuView();
                mnuView.addActionListener(this);
            }
            popChatBoard.add(mnuLoadRefresh);
            popChatBoard.add(mnuSave);
            popChatBoard.add(mnuView);
            popChatBoard.add(mnuClear);
            popChatBoard.add(mnuClearHistory);
        }
        return popChatBoard;
    }

    public void setPopChatBoard(JPopupMenu popChatBoard) {
        this.popChatBoard = popChatBoard;
    }


    private void loadChatHistoryForCurrentUser() {
        int idx = getChatTabbs().getSelectedIndex();
        if (idx != -1) {
            Users currentUser = (Users) vUsers.get(idx);
            ChatHistory ch = ChatCache.findHistory(currentUser);
            ChatPanel cp = (ChatPanel) getChatTabbs().getComponentAt(idx);
            cp.setChatHistory(ch.toString());
            cp.moveChatHistoryScrollBarAtEnd();
        }
    }
}
