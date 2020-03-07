 /*
 * =============================================================================
 * FileTransferPanel.java
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

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

/**
 * Encapsulates the FileTransfer panel.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileTransferPanel extends JPanel {

    private static final long serialVersionUID = 1L;
    private Font fnt;
    private JTextArea txtChatArea;
    private JScrollPane txtChatAreaPane;
    private JButton btnSend;
    private JButton btnClear;
    private JButton btnBrowse;

    FileTransferPanel() {
        initialize();
    }

    private void initialize() {
        fnt = new Font("segoe ui", 0, 12);
        Box bxbtn = Box.createHorizontalBox();
        bxbtn.add(getBtnBrowse());
        bxbtn.add(Box.createHorizontalStrut(10));
        bxbtn.add(getBtnSend());
        bxbtn.add(Box.createHorizontalStrut(10));
        bxbtn.add(getBtnClear());
        Box bxtxta = Box.createHorizontalBox();
        bxtxta.add(getChatAreaPane());
        Box all = Box.createVerticalBox();
        all.add(bxbtn);
        all.add(Box.createVerticalStrut(5));
        all.add(bxtxta);
        add(all);
    }

    public JButton getBtnBrowse() {
        if (btnBrowse == null) {
            btnBrowse = new JButton();
            btnBrowse.setText("BROWSE");
            btnBrowse.setFont(fnt);
            btnBrowse.setVisible(true);
            btnBrowse.setActionCommand("BROWSE");
            btnBrowse.setCursor(new Cursor(12));
            btnBrowse.setBorder(getBtnBorder());
        }
        return btnBrowse;
    }

    public JButton getBtnClear() {
        if (btnClear == null) {
            btnClear = new JButton();
            btnClear.setText("CLEAR");
            btnClear.setFont(fnt);
            btnClear.setActionCommand("CLEAR");
            btnClear.setCursor(new Cursor(12));
            btnClear.setBorder(getBtnBorder());
        }
        return btnClear;
    }

    public void setBtnClear(JButton btnClear) {
        this.btnClear = btnClear;
    }

    public JButton getBtnSend() {
        if (btnSend == null) {
            btnSend = new JButton();
            btnSend.setText("SEND");
            btnSend.setFont(fnt);
            btnSend.setActionCommand("SEND");
            btnSend.setCursor(new Cursor(12));
            btnSend.setBorder(getBtnBorder());
        }
        return btnSend;
    }

    private Border getBtnBorder() {
        return BorderFactory.createEtchedBorder(1);
    }

    public JTextArea getTxtChatArea() {
        if (txtChatArea == null) {
            txtChatArea = new JTextArea();
            txtChatArea.setRows(22);
            txtChatArea.setColumns(30);
            txtChatArea.setMaximumSize(txtChatArea.getPreferredSize());
            txtChatArea.setFont(fnt);
            txtChatArea.setEditable(false);
        }
        return txtChatArea;
    }

    public JScrollPane getChatAreaPane() {
        if (txtChatArea == null) {
            Border txtChatAreaBorder = BorderFactory.createLineBorder(Color.BLACK);
            getTxtChatArea().setBorder(txtChatAreaBorder);
            txtChatAreaPane = new JScrollPane(getTxtChatArea());
            txtChatAreaPane.setHorizontalScrollBarPolicy(32);
            txtChatAreaPane.setVerticalScrollBarPolicy(22);
        }
        return txtChatAreaPane;
    }
}
