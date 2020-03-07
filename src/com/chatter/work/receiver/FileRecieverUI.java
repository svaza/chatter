 /*
 * =============================================================================
 * FileRecieverUI.java
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

import com.chatter.starter.Chatter;
import com.chatter.ui.chatter.ChatterUI;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

/**
 * Encapsulates the UI for FileReceiverWindow.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileRecieverUI extends Thread
        implements ActionListener {

    public Object data[][];
    public List<List> vdata;
    public List<String> vpaths;
    public List<Integer> vfileSize;
    public JTable downloadTable;
    public JButton btnAccept;
    public JButton btnCancel;
    public FileRecieverTableModel downloadTableModel;
    public String downloadToken;
    public Font font;
    public JScrollPane downloadTablePane;
    public JFrame frm;
    public String title;
    public boolean statusFlag;
    public List<String> vVirtualPath;
    public List<String> vVirtualMappedPath;
    public JProgressBar fileDownloadProgress;
    public JLabel labDownloadCancelled;
    public String columns[] = {
        "File Name", "Size (MB)", "Action"
    };

    public FileRecieverUI(String downloadToken) {
        vdata = new ArrayList<List>();
        vpaths = new ArrayList<String>();
        vfileSize = new ArrayList<Integer>();
        font = new Font("segoe ui", 0, 12);
        frm = new JFrame();
        statusFlag = true;
        vVirtualPath = new ArrayList<String>();
        vVirtualMappedPath = new ArrayList<String>();
        this.downloadToken = downloadToken;
        configureButtons();
        configureDownloadTable();
        configureProgressFile();
    }

    private Object[][] parseVectorToObject(List vToParse) {
        if (vToParse.isEmpty()) {
            return new Object[0][0];
        }
        List vTok = (List) vToParse.get(0);
        Object _data[][] = new Object[vToParse.size()][vTok.size()];
        for (int i = 0; i < vToParse.size(); i++) {
            List vT = (List) vToParse.get(i);
            for (int j = 0; j < vTok.size(); j++) {
                _data[i][j] = vT.get(j);
            }

        }

        return _data;
    }

    public void configureProgressFile() {
        fileDownloadProgress = new JProgressBar();
        fileDownloadProgress.setMinimum(0);
        fileDownloadProgress.setBorderPainted(true);
        fileDownloadProgress.setStringPainted(true);
        fileDownloadProgress.setFont(new Font("segoe ui", 1, 12));
    }

    public JLabel getLabDownloadCancelled() {
        if (labDownloadCancelled == null) {
            labDownloadCancelled = new JLabel("download cancelled by user.");
            labDownloadCancelled.setForeground(Color.red);
            labDownloadCancelled.setIcon((Icon) Chatter.imageResource.get("appln.information-image"));
            labDownloadCancelled.setHorizontalTextPosition(4);
            labDownloadCancelled.setVisible(false);
            labDownloadCancelled.setFont(new Font("segoe ui", 0, 10));
        }
        return labDownloadCancelled;
    }

    public void configureDownloadTable() {
        downloadTable = new JTable();
        initializeDownloadData(downloadToken.split("\n"));
        data = parseVectorToObject(vdata);
        downloadTableModel = new FileRecieverTableModel(columns, data);
        downloadTable.setModel(downloadTableModel);
        downloadTable.getTableHeader().setReorderingAllowed(false);
        downloadTable.getColumnModel().getColumn(2).setCellRenderer(new DownloadTableRenderer());
        downloadTable.getColumnModel().getColumn(2).setCellEditor(new DownloadTableEditor());
        downloadTable.getColumnModel().getColumn(0).setPreferredWidth(300);
        downloadTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        downloadTable.getColumnModel().getColumn(2).setPreferredWidth(51);
        downloadTable.getColumnModel().getColumn(2).setResizable(false);
        downloadTable.getColumnModel().getColumn(0).setResizable(false);
        downloadTable.getColumnModel().getColumn(1).setResizable(false);
        downloadTable.setFont(font);
        downloadTable.setSelectionMode(0);
        downloadTable.getTableHeader().setFont(font);
        downloadTablePane = new JScrollPane(downloadTable);
        downloadTablePane.setVerticalScrollBarPolicy(22);
        downloadTablePane.setBorder(BorderFactory.createLineBorder(Color.black));
    }

    public void configureButtons() {
        btnAccept = new JButton();
        btnAccept.setText("Accept");
        btnAccept.setFont(font);
        btnAccept.setActionCommand("ACCEPT");
        btnAccept.setCursor(new Cursor(12));
        btnAccept.addActionListener(this);
        btnCancel = new JButton();
        btnCancel.setText("Cancel");
        btnCancel.setFont(font);
        btnCancel.setActionCommand("CANCEL");
        btnCancel.setCursor(new Cursor(12));
        btnCancel.addActionListener(this);
        javax.swing.border.Border btnBorder = BorderFactory.createEtchedBorder(1);
        btnAccept.setBorder(btnBorder);
        btnCancel.setBorder(btnBorder);
    }

    void initializeDownloadData(String dataT[]) {
        JButton btn = new JButton("Delete");
        btn.addActionListener(this);
        btn.setActionCommand("DELETE_DOWNLOAD");
        btn.setFont(new Font("segoe ui", 0, 12));
        btn.setCursor(new Cursor(12));
        btn.addActionListener(this);
        javax.swing.border.Border btnBorder = BorderFactory.createEtchedBorder(1);
        btn.setBorder(btnBorder);
        for (int i = 1; i < dataT.length; i += 3) {
            vVirtualPath.add(dataT[i + 2]);
            String Size = dataT[i + 1];
            if (!Size.equals("-999")) {
                vpaths.add(dataT[i]);
                vVirtualMappedPath.add(dataT[i + 2]);
                String fileName = (new File(dataT[i])).getName();
                vfileSize.add(new Integer(Size));
                double totalSize = (new Double((new Long(Size)).longValue())).doubleValue() / 1024D / 1024D;
                Size = (new Formatter()).format("%.2f", new Object[]{
                            Double.valueOf(totalSize)
                        }).toString();
                totalSize = (new Double(Size)).doubleValue();
                List vTok = new ArrayList();
                vTok.add(fileName);
                vTok.add(Double.valueOf(totalSize));
                vTok.add(btn);
                vdata.add(vTok);
            }
        }
    }

    public void initializeTableRendererForDownload() {
        Object _data[][] = parseVectorToObject(vdata);
        String cols[] = {
            "File Name", "Size (MB)"
        };
        downloadTableModel = new FileRecieverTableModel(cols, _data);
        downloadTable.setModel(downloadTableModel);
        downloadTable.getTableHeader().setReorderingAllowed(false);
        downloadTable.getColumnModel().getColumn(0).setPreferredWidth(475);
        downloadTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        downloadTable.getColumnModel().getColumn(0).setResizable(false);
        downloadTable.getColumnModel().getColumn(1).setResizable(false);

    }

    @Override
    public void run() {
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        Box bxTable = Box.createHorizontalBox();
        bxTable.add(downloadTablePane);
        Box bxbtn = Box.createHorizontalBox();
        bxbtn.add(btnAccept);
        bxbtn.add(Box.createHorizontalStrut(5));
        bxbtn.add(btnCancel);
        Box progress = Box.createHorizontalBox();
        progress.add(fileDownloadProgress);
        Box dc = Box.createHorizontalBox();
        dc.add(Box.createHorizontalStrut(10));
        dc.add(getLabDownloadCancelled());
        dc.add(Box.createHorizontalGlue());
        Box all = Box.createVerticalBox();
        all.add(bxTable);
        all.add(Box.createVerticalStrut(5));
        all.add(progress);
        all.add(Box.createVerticalStrut(5));
        all.add(bxbtn);
        all.add(Box.createVerticalStrut(5));
        all.add(dc);
        all.add(Box.createVerticalStrut(2));
        JPanel pan = new JPanel();
        pan.add(all);
        frm.setTitle(title);
        frm.add(pan);
        frm.pack();
        frm.setResizable(false);
        frm.setLocationRelativeTo(null);
        frm.setDefaultCloseOperation(0);
        frm.setIconImage(Toolkit.getDefaultToolkit().getImage(ChatterUI.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
        frm.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
        if (actionCmd.equals("CANCEL")) {
            frm.dispose();
        }
        if (actionCmd.equals("DELETE_DOWNLOAD")) {
            int row = downloadTable.getSelectedRow();
            if (row != -1) {
                vpaths.remove(row);
                vdata.remove(row);
                vfileSize.remove(row);
                vVirtualMappedPath.remove(row);
                vVirtualPath.remove(row);
            }
            data = parseVectorToObject(vdata);
            downloadTableModel = new FileRecieverTableModel(columns, data);
            downloadTable.setModel(downloadTableModel);
            downloadTable.getTableHeader().setReorderingAllowed(false);
            downloadTable.getColumnModel().getColumn(2).setCellRenderer(new DownloadTableRenderer());
            downloadTable.getColumnModel().getColumn(2).setCellEditor(new DownloadTableEditor());
            downloadTable.getColumnModel().getColumn(0).setPreferredWidth(300);
            downloadTable.getColumnModel().getColumn(1).setPreferredWidth(150);
            downloadTable.getColumnModel().getColumn(2).setPreferredWidth(51);
            downloadTable.getColumnModel().getColumn(2).setResizable(false);
            downloadTable.getColumnModel().getColumn(0).setResizable(false);
            downloadTable.getColumnModel().getColumn(1).setResizable(false);
            if (vfileSize.isEmpty()) {
                btnAccept.setVisible(false);
            }
        }
    }

    public void setBtnAcceptVisibility(boolean visibility) {
        btnAccept.setVisible(visibility);
    }

    public void setBtnCancelVisibility(boolean visibility) {
        btnCancel.setVisible(visibility);
    }

    public void setBtnCancelCommand(String cmd) {
        btnCancel.setActionCommand(cmd);
    }

    public void setProgressBarString(String str) {
        fileDownloadProgress.setString(str);
    }

    public void setProgressBarValue(int value) {
        fileDownloadProgress.setValue(value);
    }
    public String getFilePathAt(int index){
        return (String) vpaths.get(index);
    }
    public String getParentFolderAt(int index){
        return vVirtualMappedPath.get(index);
    }
    public Integer getFileSizeAt(int index){
        return vfileSize.get(index);
    }


}
