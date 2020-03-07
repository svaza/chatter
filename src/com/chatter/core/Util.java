 /*
 * =============================================================================
 * Util.java
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

import com.chatter.starter.Chatter;
import com.chatter.ui.console.ChatterConsole;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A utility class for chatter.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class Util {

    public static java.util.List<Users> users;
    public static java.util.List<Users> config;
    public static Users me;
    private static JDialog dialog;
    private static boolean retFlag = false;

    /**
     * 
     * @param ipaddress    IPAddress of the user to be search.
     * @param userconfig   List<Users> from which the user has to be searched
     * @return             List<Users> whose ipaddress matches with provided ipAddress
     */
    public static java.util.List<Users> searchByIPAddress(String ipaddress, java.util.List<Users> userconfig) {
        java.util.List<Users> vusers = new ArrayList<Users>();
        for (int i = 0; i < userconfig.size(); i++) {
            Users user = userconfig.get(i);
            if (user.getIPAddress().equals(ipaddress)) {
                vusers.add(user);
            }
        }

        return vusers;
    }

    /**
     * Converts the IPAddress in byte[] in network byte order
     * @param ip      IPAddress which has to be converted to byte[]
     * @return        byte[] of IPAddress which is converted to network byte order.
     */
    public static byte[] getBytes(String ip) {
        String _ip[] = ip.split("\\.");
        byte ipaddress[] = new byte[ip.split("\\.").length];
        for (int i = 0; i < _ip.length; i++) {
            ipaddress[i] = (byte) Integer.parseInt(_ip[i]);
        }

        return ipaddress;
    }

    public static double BytestoMB(double size) {
        return size / 1024D / 1024D;
    }

    public static double MBtoBytes(double size) {
        return size * 1024D * 1024D;
    }

    public static void showInformationDialog(Window parent, boolean modal, Object message, String title, boolean exitAfterShow) {
        final boolean exit = exitAfterShow;
        if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent, modal);
        } else if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent, modal);
        } else {
            dialog = new JDialog();
            dialog.setModal(modal);
        }
        JLabel img = new JLabel();
        Box bimg = Box.createVerticalBox();
        bimg.add(Box.createVerticalStrut(10));
        bimg.add(img);
        bimg.add(Box.createVerticalGlue());
        img.setIcon((Icon) Chatter.imageResource.get("appln.main-image"));
        JButton btn = new JButton("OK");
        btn.setCursor(new Cursor(12));
        btn.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Util.dialog.dispose();
                if (exit) {
                    System.exit(0);
                }
            }
        });
        javax.swing.border.Border btnBorder = BorderFactory.createEtchedBorder(1);
        btn.setBorder(btnBorder);
        btn.setFont(new Font("segoe ui", 0, 12));
        Box blab = Box.createHorizontalBox();
        blab.add(Box.createHorizontalStrut(10));
        blab.add(bimg);
        blab.add(Box.createHorizontalStrut(5));
        blab.add((Component) message);
        blab.add(Box.createHorizontalGlue());
        Box bbtn = Box.createHorizontalBox();
        bbtn.add(btn);
        Box all = Box.createVerticalBox();
        all.add(Box.createVerticalStrut(5));
        all.add(blab);
        all.add(Box.createVerticalStrut(10));
        all.add(bbtn);
        all.add(Box.createVerticalStrut(3));
        dialog.setTitle(title);
        dialog.add(all);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(0);
        dialog.setResizable(true);
        dialog.setVisible(true);
    }

    public static boolean showYesNoDialog(Window parent, boolean modal, Object message, String title) {
        if (parent instanceof Frame) {
            dialog = new JDialog((Frame) parent, modal);
        } else if (parent instanceof Dialog) {
            dialog = new JDialog((Dialog) parent, modal);
        }
        JLabel img = new JLabel();
        Box bimg = Box.createVerticalBox();
        bimg.add(Box.createVerticalStrut(10));
        bimg.add(img);
        bimg.add(Box.createVerticalGlue());
        img.setIcon((Icon) Chatter.imageResource.get("appln.main-image"));
        JButton btnYes = new JButton("YES");
        btnYes.setCursor(new Cursor(12));
        btnYes.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Util.dialog.dispose();
                Util.retFlag = true;
            }
        });
        javax.swing.border.Border btnBorder = BorderFactory.createEtchedBorder(1);
        btnYes.setBorder(btnBorder);
        btnYes.setFont(new Font("segoe ui", 0, 12));
        JButton btnNo = new JButton("NO");
        btnNo.setCursor(new Cursor(12));
        btnNo.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Util.dialog.dispose();
                Util.retFlag = false;
            }
        });
        btnNo.setBorder(btnBorder);
        btnNo.setFont(new Font("segoe ui", 0, 12));
        Box blab = Box.createHorizontalBox();
        blab.add(Box.createHorizontalStrut(10));
        blab.add(bimg);
        blab.add(Box.createHorizontalStrut(5));
        blab.add((Component) message);
        blab.add(Box.createHorizontalGlue());
        Box bbtn = Box.createHorizontalBox();
        bbtn.add(btnYes);
        bbtn.add(Box.createHorizontalStrut(5));
        bbtn.add(btnNo);
        Box all = Box.createVerticalBox();
        all.add(Box.createVerticalStrut(5));
        all.add(blab);
        all.add(Box.createVerticalStrut(10));
        all.add(bbtn);
        all.add(Box.createVerticalStrut(3));
        dialog.setTitle(title);
        dialog.add(all);
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setDefaultCloseOperation(0);
        dialog.setResizable(true);
        dialog.setVisible(true);
        return retFlag;
    }

    public static void showSaveDialogAndSaveToFile(StringBuffer message, ChatterConsole console, Component parent) {
        JFileChooser saveConsole = new JFileChooser();
        int userSelection = saveConsole.showSaveDialog(parent);
        if (userSelection == 0) {
            java.io.File file = saveConsole.getSelectedFile();
            try {
                FileWriter fr = new FileWriter(file);
                fr.write(message.toString());
                fr.close();
            } catch (Exception ee) {
                console.showConsole();
                console.println((new StringBuilder()).append("ERROR while saving console:").append(ee).toString());
            }
        }
    }

    public static String replace(String pattern, String replace, String string) {
        String retStr = "";
        for (int i = 0; i < string.length(); i++) {
            String s = (new StringBuilder()).append("").append(string.charAt(i)).toString();
            if (s.equals(pattern)) {
                retStr = (new StringBuilder()).append(retStr).append(replace).toString();
            } else {
                retStr = (new StringBuilder()).append(retStr).append(s).toString();
            }
        }

        return retStr;
    }
}
