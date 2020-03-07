 /*
 * =============================================================================
 * FileScannerUI.java
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
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

/**
 * Encapsulates the UI for FileScanner
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileScannerUI extends Thread {

    JProgressBar fileprogress;
    JLabel labpaths;
    JLabel labnofiles;
    Font fnt;
    private long filecounter;
    private static final String TITLE = (new StringBuilder()).append(Chatter.getApplicationProperty("appln.name")).append(" ").append("1.2").toString();
    JDialog fileScannerDialog;
    FileScannerHandler handler;
    JButton btnDone;

    public FileScannerUI(FileScannerHandler handler) {
        fileprogress = null;
        labpaths = null;
        labnofiles = null;
        fnt = null;
        filecounter = 0;
        btnDone = null;
        this.handler = handler;
    }

    public Font getFnt() {
        if (fnt == null) {
            fnt = new Font("segoe ui", 0, 12);
        }
        return fnt;
    }

    public JProgressBar getFileprogress() {
        if (fileprogress == null) {
            fileprogress = new JProgressBar();
            fileprogress.setMinimum(0);
            fileprogress.setMaximum(100);
            fileprogress.setBorderPainted(true);
            fileprogress.setStringPainted(true);
            fileprogress.setString("scanning....");
            fileprogress.setIndeterminate(true);
            fileprogress.setFont(getFnt());
        }
        return fileprogress;
    }

    public JLabel getLabnofiles() {
        if (labnofiles == null) {
            labnofiles = new JLabel();
            labnofiles.setFont(getFnt());
            labnofiles.setText("0");
        }
        return labnofiles;
    }

    public JLabel getLabpaths() {
        if (labpaths == null) {
            labpaths = new JLabel();
            labpaths.setText("please wait");
            labpaths.setFont(getFnt());
        }
        return labpaths;
    }

    public void setDisplayPath(String path) {
        filecounter++;
        getLabnofiles().setText((new Long(filecounter)).toString());
        getLabpaths().setText(path);
    }

    private Box getContents() {
        Box bxpaths = Box.createHorizontalBox();
        bxpaths.add(getLabpaths());
        bxpaths.add(Box.createHorizontalStrut(20));
        Box bxnofiles = Box.createHorizontalBox();
        JLabel lab = new JLabel();
        lab.setText("Files Scanned:");
        lab.setFont(getFnt());
        bxnofiles.add(lab);
        bxnofiles.add(Box.createHorizontalGlue());
        bxnofiles.add(getLabnofiles());
        Box bxprogress = Box.createHorizontalBox();
        bxprogress.add(Box.createHorizontalStrut(20));
        bxprogress.add(getFileprogress());
        bxprogress.add(Box.createHorizontalStrut(20));
        Box gui = Box.createVerticalBox();
        gui.add(bxpaths);
        gui.add(bxnofiles);
        gui.add(bxprogress);
        gui.add(Box.createVerticalStrut(10));
        return gui;
    }

    public JDialog getFileScannerDialog() {
        if (fileScannerDialog == null) {
            fileScannerDialog = new JDialog(handler.chatter.getChatterUI().chatFrame, false);
            fileScannerDialog.add(getContents());
            fileScannerDialog.pack();
            fileScannerDialog.setLocationRelativeTo(handler.chatter.getChatterUI().chatFrame);
            fileScannerDialog.setTitle(TITLE);
            fileScannerDialog.setDefaultCloseOperation(0);
            fileScannerDialog.addWindowListener(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent e) {
                    handler.state = false;
                }
            });
        }
        return fileScannerDialog;
    }

    public void createAndShowGUI() {
        getFileScannerDialog().setVisible(true);
    }

    @Override
    public void run() {
        createAndShowGUI();
    }
}
