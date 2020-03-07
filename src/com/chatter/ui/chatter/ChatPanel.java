 /*
 * =============================================================================
 * ChatPanel.java
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

import com.chatter.starter.Chatter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * The UI class encapsulating the chat area.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatPanel extends JPanel {

    private Font fnt;
    private static final long serialVersionUID = 1L;
    private JLabel labUserInfo;
    private JTextArea txtChatHistoryArea;
    private JTextArea txtChatArea;
    private JScrollPane scrollChatPane;
    private JScrollPane scrollLabChatHistory;
    private JMenuItem mnuLoadRefresh;
    private JMenuItem mnuSave;
    private JMenuItem mnuView;
    private JMenuItem mnuPopout;
    private JMenuItem mnuClear;
    private JMenuItem mnuClearHistory;
    private JPopupMenu popChatTsk;

    public ChatPanel() {
        initialize();
    }

    private void initialize() {
        fnt = new Font("segoe ui", 0, 12);
        Box bxinfo = Box.createHorizontalBox();
        bxinfo.add(getLabUserInfo());
        Box bxchathistory = Box.createHorizontalBox();
        bxchathistory.add(getScrollChatHistory());
        TitledBorder title = BorderFactory.createTitledBorder("[ Chat History ]");
        title.setTitleColor(Color.black);
        title.setTitleFont(new Font("segoe ui", 0, 12));
        bxchathistory.setBorder(title);
        Box bxchatarea = Box.createHorizontalBox();
        bxchatarea.add(getScrollChatPane());
        Box all = Box.createVerticalBox();
        all.add(Box.createVerticalStrut(10));
        all.add(bxinfo);
        all.add(Box.createVerticalStrut(2));
        all.add(bxchathistory);
        all.add(Box.createVerticalStrut(2));
        all.add(bxchatarea);
        add(all);
    }

    public JMenuItem getMnuClear() {
        if (mnuClear == null) {
            mnuClear = new JMenuItem("Clear");
            mnuClear.setActionCommand("CLEAR");
            mnuClear.setFont(fnt);
        }
        return mnuClear;
    }

    public void setMnuClear(JMenuItem mnuClear) {
        this.mnuClear = mnuClear;
    }

    public JMenuItem getMnuClearHistory() {
        if (mnuClearHistory == null) {
            mnuClearHistory = new JMenuItem("Clear History");
            mnuClearHistory.setActionCommand("CLEAR_HISTORY");
            mnuClearHistory.setFont(fnt);
        }
        return mnuClearHistory;
    }

    public void setMnuClearHistory(JMenuItem mnuClearHistory) {
        this.mnuClearHistory = mnuClearHistory;
    }

    public JMenuItem getMnuPopout() {
        if (mnuPopout == null) {
            mnuPopout = new JMenuItem("Popout");
            mnuPopout.setActionCommand("POPOUT");
            mnuPopout.setFont(fnt);
        }
        return mnuPopout;
    }

    public JPopupMenu getPopChatTsk() {
        if (popChatTsk == null) {
            popChatTsk = new JPopupMenu();
            popChatTsk.setBorderPainted(true);
            popChatTsk.add(getMnuLoadRefresh());
            popChatTsk.add(getMnuPopout());
            popChatTsk.add(getMnuSave());
            popChatTsk.add(getMnuView());
            popChatTsk.add(getMnuClear());
            popChatTsk.add(getMnuClearHistory());
        }
        return popChatTsk;
    }

    public JMenuItem getMnuLoadRefresh() {
        if (mnuLoadRefresh == null) {
            mnuLoadRefresh = new JMenuItem("Load/Refresh");
            mnuLoadRefresh.setActionCommand("LOAD_REFRESH");
            mnuLoadRefresh.setFont(fnt);
        }
        return mnuLoadRefresh;
    }

    public JMenuItem getMnuSave() {
        if (mnuSave == null) {
            mnuSave = new JMenuItem("Save");
            mnuSave.setActionCommand("SAVE");
            mnuSave.setFont(fnt);
        }
        return mnuSave;
    }

    public JMenuItem getMnuView() {
        if (mnuView == null) {
            mnuView = new JMenuItem("View");
            mnuView.setActionCommand("VIEW");
            mnuView.setFont(fnt);
        }
        return mnuView;
    }

    public JTextArea getTxtChatHistoryArea() {
        if (txtChatHistoryArea == null) {
            txtChatHistoryArea = new JTextArea();
            javax.swing.border.Border txtChatAreaBorder = BorderFactory.createLineBorder(Color.BLACK);
            txtChatHistoryArea.setBorder(txtChatAreaBorder);
            txtChatHistoryArea.setEditable(false);
            txtChatHistoryArea.setText("");
            txtChatHistoryArea.setColumns(30);
            txtChatHistoryArea.setRows(15);
            txtChatHistoryArea.setFont(fnt);
            txtChatHistoryArea.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    ifThenShowPopup(e);
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    ifThenShowPopup(e);
                }

                private void ifThenShowPopup(MouseEvent e) {
                    if (e.isPopupTrigger()) {
                        getPopChatTsk().show((Component) e.getSource(), e.getX(), e.getY());
                    }
                }
            });
        }
        return txtChatHistoryArea;
    }

    public JScrollPane getScrollChatHistory() {
        if (scrollLabChatHistory == null) {
            scrollLabChatHistory = new JScrollPane(getTxtChatHistoryArea());
            scrollLabChatHistory.setHorizontalScrollBarPolicy(32);
            scrollLabChatHistory.setVerticalScrollBarPolicy(22);
        }
        return scrollLabChatHistory;
    }

    public JLabel getLabUserInfo() {
        if (labUserInfo == null) {
            labUserInfo = new JLabel("Disconnected");
            labUserInfo.setIcon((Icon) Chatter.imageResource.get("appln.disconnected-image"));
            labUserInfo.setHorizontalTextPosition(4);
            labUserInfo.setFont(fnt);
            labUserInfo.setForeground(Color.red);
        }
        return labUserInfo;
    }

    public JScrollPane getScrollChatPane() {
        if (scrollChatPane == null) {
            javax.swing.border.Border txtChatAreaBorder = BorderFactory.createLineBorder(Color.BLACK);
            getTxtChatArea().setBorder(txtChatAreaBorder);
            scrollChatPane = new JScrollPane(getTxtChatArea());
            scrollChatPane.setHorizontalScrollBarPolicy(31);
            scrollChatPane.setVerticalScrollBarPolicy(22);
        }
        return scrollChatPane;
    }

    public JTextArea getTxtChatArea() {
        if (txtChatArea == null) {
            txtChatArea = new JTextArea();
            txtChatArea.setRows(4);
            txtChatArea.setColumns(30);
            txtChatArea.setFont(fnt);
        }
        return txtChatArea;
    }

    public void setFnt(Font fnt) {
        this.fnt = fnt;
    }

    public void setLabUserInfo(JLabel labUserInfo) {
        this.labUserInfo = labUserInfo;
    }

    public void setMnuLoadRefresh(JMenuItem mnuLoadRefresh) {
        this.mnuLoadRefresh = mnuLoadRefresh;
    }

    public void setMnuPopout(JMenuItem mnuPopout) {
        this.mnuPopout = mnuPopout;
    }

    public void setMnuSave(JMenuItem mnuSave) {
        this.mnuSave = mnuSave;
    }

    public void setMnuView(JMenuItem mnuView) {
        this.mnuView = mnuView;
    }

    public void setPopChatTsk(JPopupMenu popChatTsk) {
        this.popChatTsk = popChatTsk;
    }

    public void setScrollChatPane(JScrollPane scrollChatPane) {
        this.scrollChatPane = scrollChatPane;
    }

    public void setScrollLabChatHistory(JScrollPane scrollLabChatHistory) {
        this.scrollLabChatHistory = scrollLabChatHistory;
    }

    public void setTxtChatArea(JTextArea txtChatArea) {
        this.txtChatArea = txtChatArea;
    }

    public void setTxtChatHistoryArea(JTextArea txtChatHistoryArea) {
        this.txtChatHistoryArea = txtChatHistoryArea;
    }

    public void setChatHistory(String str) {
        getTxtChatHistoryArea().setText(str);
    }

    public void moveChatHistoryScrollBarAtPosition(int length) {
        getTxtChatHistoryArea().setCaretPosition(length);
    }

    public void moveChatHistoryScrollBarAtEnd() {
        int len = getTxtChatHistoryArea().getText().length();
        getTxtChatHistoryArea().setCaretPosition(len);
    }

    public void appendHistory(String str) {
        getTxtChatHistoryArea().append(str);
    }

    public String getChatMessageToSend() {
        return getTxtChatArea().getText();
    }

    public String getChatHistory() {
        return getTxtChatHistoryArea().getText();
    }
}
