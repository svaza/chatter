 /*
 * =============================================================================
 * ChatterConsole.java
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
package com.chatter.ui.console;

import com.chatter.core.Util;
import com.chatter.starter.Chatter;
import com.chatter.ui.chatter.ChatterUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

/**
 * A console window for chatter.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatterConsole
        implements ActionListener, Runnable {

    private JTextArea txtConsole;
    private JButton btnSave;
    private JButton btnClear;
    private Font fontConsole;
    private Font fontBtn;
    private JScrollPane paneConsole;
    private JFrame frmConsole;
    private Thread thread;
    private int mode;
    public static int HIDE_ON_CLOSE = 1;
    public static int DISPOSE_ON_CLOSE = 2;
    public static int EXIT_ON_CLOSE = 3;
    public static int DO_NOTHING_ON_CLOSE = 0;

    public ChatterConsole() {
        fontConsole = new Font("monospaced", 0, 11);
        fontBtn = new Font("segoe ui", 0, 12);
        thread = new Thread(this);
        mode = HIDE_ON_CLOSE;
        initializeComponents();
    }

    public ChatterConsole(int mode) {
        fontConsole = new Font("monospaced", 0, 11);
        fontBtn = new Font("segoe ui", 0, 12);
        thread = new Thread(this);
        this.mode = mode;
        initializeComponents();
    }

    public JButton getBtnClear() {
        if (btnClear == null) {
            btnClear = new JButton();
            btnClear.setText("CLEAR");
            btnClear.setFont(getFontBtn());
            btnClear.setCursor(new Cursor(12));
            btnClear.addActionListener(this);
            btnClear.setActionCommand("CLEAR");
            btnClear.setBorder(BorderFactory.createEtchedBorder(1));
        }
        return btnClear;
    }

    public void setBtnClear(JButton btnClear) {
        this.btnClear = btnClear;
    }

    public JButton getBtnSave() {
        if (btnSave == null) {
            btnSave = new JButton();
            btnSave.setText("SAVE");
            btnSave.setFont(getFontBtn());
            btnSave.setCursor(new Cursor(12));
            btnSave.addActionListener(this);
            btnSave.setActionCommand("SAVE");
            btnSave.setBorder(BorderFactory.createEtchedBorder(1));
        }
        return btnSave;
    }

    public void setBtnSave(JButton btnSave) {
        this.btnSave = btnSave;
    }

    public Font getFontBtn() {
        return fontBtn;
    }

    public void setFontBtn(Font fontBtn) {
        this.fontBtn = fontBtn;
    }

    public Font getFontConsole() {
        return fontConsole;
    }

    public void setFontConsole(Font fontConsole) {
        this.fontConsole = fontConsole;
    }

    public JFrame getFrmConsole() {
        if (frmConsole == null) {
            frmConsole = new JFrame();
            frmConsole.setTitle((new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" Console ").append("1.2").toString());
            frmConsole.setLocationRelativeTo(null);
            frmConsole.setDefaultCloseOperation(mode);
            frmConsole.setResizable(true);
            frmConsole.setVisible(false);
            frmConsole.setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
        }
        return frmConsole;
    }

    public void setFrmConsole(JFrame frmConsole) {
        this.frmConsole = frmConsole;
    }

    public JScrollPane getPaneConsole() {
        if (paneConsole == null) {
            paneConsole = new JScrollPane(getTxtConsole());
            paneConsole.setHorizontalScrollBarPolicy(32);
            paneConsole.setVerticalScrollBarPolicy(22);
        }
        return paneConsole;
    }

    public void setPaneConsole(JScrollPane paneConsole) {
        this.paneConsole = paneConsole;
    }

    public JTextArea getTxtConsole() {
        if (txtConsole == null) {
            txtConsole = new JTextArea();
            txtConsole.setColumns(100);
            txtConsole.setRows(10);
            txtConsole.setFont(getFontConsole());
            txtConsole.setBorder(BorderFactory.createLineBorder(Color.black));
            txtConsole.setText("");
            txtConsole.setEditable(false);
        }
        return txtConsole;
    }

    public void setTxtConsole(JTextArea txtConsole) {
        this.txtConsole = txtConsole;
    }

    public synchronized void println(Object obj) {
        getTxtConsole().append((new StringBuilder()).append(obj.toString()).append("\n").toString());
        scrollToEnd();
    }

    public synchronized void print(Object obj) {
        getTxtConsole().append(obj.toString());
        scrollToEnd();
    }

    public void clearConsole() {
        getTxtConsole().setText("");
    }

    public void showConsole() {
        getFrmConsole().setVisible(true);
        getFrmConsole().setSize(700, 300);
        getFrmConsole().toFront();
    }

    public void hideConsole() {
        getFrmConsole().setVisible(false);
    }

    public void scrollToEnd() {
        getTxtConsole().setCaretPosition(getTxtConsole().getText().length());
    }

    public void scrollToPosition(int position) {
        getTxtConsole().setCaretPosition(position);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SAVE")) {
            Util.showSaveDialogAndSaveToFile(new StringBuffer(txtConsole.getText()), this, frmConsole);
        } else if (e.getActionCommand().equals("CLEAR")) {
            clearConsole();
        }
    }

    public void run() {
    }

    private void initializeComponents() {
        Box bxtxt = Box.createHorizontalBox();
        bxtxt.add(getPaneConsole());
        Box bxbtn = Box.createHorizontalBox();
        bxbtn.add(getBtnSave());
        bxbtn.add(Box.createRigidArea(new Dimension(5, 5)));
        bxbtn.add(getBtnClear());
        Box bxconsole = Box.createVerticalBox();
        bxconsole.add(bxtxt);
        bxconsole.add(bxbtn);
        bxconsole.add(Box.createVerticalStrut(5));
        getFrmConsole().add(bxconsole);
        getFrmConsole().pack();
    }

    public void createAndShowGUI() {
        thread.start();
    }
}
