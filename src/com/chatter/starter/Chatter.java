 /*
 * =============================================================================
 * Chatter.java
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
package com.chatter.starter;

import com.chatter.server.filetransfer.FileTransferServer;
import com.chatter.client.Client;
import com.chatter.core.*;
import com.chatter.server.*;
import com.chatter.ui.about.AboutChatter;
import com.chatter.ui.console.ChatterConsole;
import com.chatter.ui.chatter.*;
import com.chatter.work.chat.ChatServiceHandler;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.BindException;
import java.net.InetAddress;
import java.util.*;
import java.util.List;
import javax.swing.*;
/**
 * Main class
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class Chatter
        implements ActionListener {

    AboutChatter aboutChatter;
    static String configFilePath = (new StringBuilder()).append(System.getProperty("user.dir")).append(System.getProperty("file.separator")).append("config").append(System.getProperty("file.separator")).append("config.xml").toString();
    static String usersFilePath = (new StringBuilder()).append(System.getProperty("user.dir")).append(System.getProperty("file.separator")).append("config").append(System.getProperty("file.separator")).append("users.xml").toString();
    public Server chatService;
    public Server instanceService;
    public FileTransferServer ftserver;
    public FileRecieverServer frserver;
    ChatterHandler hchatter;
    ChatterTray startChatterTray;
    ChatterConsole chatterConsole;
    boolean isAppRunning;
    boolean userXML;
    boolean configXML;
    public static Properties applnProperties = null;
    public static final Hashtable imageResource = new Hashtable();
    static String settingsFilePath = (new StringBuilder()).append(System.getProperty("user.dir")).append(System.getProperty("file.separator")).append("settings.ini").toString();
    boolean settingsFlag;
    boolean jflag;

    /**
     * Default Constructor
     */
    public Chatter() {
        isAppRunning = true;
        userXML = true;
        configXML = true;
        settingsFlag = true;
        jflag = true;
        loadProperties();
        loadUsersAndConfig();
        if (!jflag) {
            return;
        } else {
            aboutChatter=new AboutChatter();
            startChatterTray = new ChatterTray();
            chatterConsole = new ChatterConsole();
            chatterConsole.createAndShowGUI();
            hchatter = new ChatterHandler(chatterConsole);
            return;
        }
    }

    /**
     * returns application wide property
     * @param property               :    the name of property
     * @return                       :    property value
     */
    public static String getApplicationProperty(String property) {
        return applnProperties.getProperty(property);
    }

    /**
     * loads the application wide properties
     */
    private void loadProperties() {
        try {
            applnProperties = new Properties();
            applnProperties.load(Chatter.class.getResource("/resources/chatter.properties").openStream());
        } catch (Exception e) {
            e.printStackTrace();
            jflag = false;
        }
        if (jflag) {
            settingsFilePath = applnProperties.getProperty("appln.settings");
            settingsFilePath.trim();
            if (settingsFilePath.charAt(0) == '$') {
                settingsFilePath = Util.replace("$", System.getProperty("user.dir"), settingsFilePath);
            }
            String property = applnProperties.getProperty("appln.main-image");
            imageResource.put("appln.main-image", new ImageIcon(Chatter.class.getResource(property)));
            property = applnProperties.getProperty("appln.information-image");
            imageResource.put("appln.information-image", new ImageIcon(Chatter.class.getResource(property)));
            property = applnProperties.getProperty("appln.connected-image");
            imageResource.put("appln.connected-image", new ImageIcon(Chatter.class.getResource(property)));
            property = applnProperties.getProperty("appln.tray-image");
            imageResource.put("appln.tray-image", new ImageIcon(Chatter.class.getResource(property)));
            property = applnProperties.getProperty("appln.disconnected-image");
            imageResource.put("appln.disconnected-image", new ImageIcon(Chatter.class.getResource(property)));
            try {
                Properties cp = new Properties();
                FileInputStream fin = new FileInputStream(settingsFilePath);
                cp.load(fin);
                fin.close();
                configFilePath = cp.getProperty("config");
                configFilePath.trim();
                if (configFilePath.charAt(0) == '$') {
                    configFilePath = Util.replace("$", System.getProperty("user.dir"), configFilePath);
                }
                usersFilePath = cp.getProperty("users");
                usersFilePath.trim();
                if (usersFilePath.charAt(0) == '$') {
                    usersFilePath = Util.replace("$", System.getProperty("user.dir"), usersFilePath);
                }
            } catch (Exception e) {
                settingsFlag = false;
            }
        }
    }

    /**
     * Load users and configuration
     */
    private void loadUsersAndConfig() {
        try {
            ConfigurationLoader cusers = ConfigurationLoader.newInstance(usersFilePath, chatterConsole);
            Util.users = cusers.getUsers();
        } catch (FileNotFoundException e) {
            userXML = false;
        } catch (Exception e) {
            userXML = false;
        }
        try {
            ConfigurationLoader config = ConfigurationLoader.newInstance(configFilePath, chatterConsole);
            Util.config = config.getUsers();
        } catch (FileNotFoundException e) {
            configXML = false;
        } catch (Exception e) {
            configXML = false;
        }
    }

    public void actionPerformed(ActionEvent e) {
        String actionCmd = e.getActionCommand();
        if (actionCmd.equals("EXIT")) {
            startChatterTray.chatterSystemTay.remove(startChatterTray.chatterTray);
            System.exit(0);
        } else if (actionCmd.equals("OPEN_CHATTER")) {
            hchatter.getChatterUI().chatFrame.setVisible(true);
        } else if (actionCmd.equals("ABOUT_CHATTER")) {
            aboutChatter.show();
        } else if (actionCmd.equals("SHOW_CONSOLE")) {
            chatterConsole.showConsole();
        }
    }

    private void startServices() {
        if (configXML && userXML) {
            try {
                List<Users> lstUser = Util.searchByIPAddress(InetAddress.getLocalHost().getHostAddress(), Util.config);
                if (lstUser.isEmpty()) {
                    Util.showInformationDialog(null, true, getOptionLabel("<html>Your configuration is not available in config file.<br/>Please update it.<br/>System will now exit."), applnProperties.getProperty("appln.name"), true);
                } else {
                    Users me = Util.config.get(0);
                    Util.me = me;
                    int bufferSize = (int) Util.MBtoBytes(me.getBufferSize());
                    if (bufferSize > 0 && bufferSize <= Client.MAX_BUFFER_SIZE) {
                        Client.BUFFER_SIZE = bufferSize;
                    }
                    chatterConsole.println("............................................................................................");
                    chatterConsole.println("User Configuration");
                    chatterConsole.println(me);
                    chatterConsole.println("............................................................................................");
                    try {
                        instanceService = new Server(me.getInstancePort(), chatterConsole);
                        instanceService.setServerName("[Instance Manager Server v1.0]");
                        ChatServiceHandler handler = new ChatServiceHandler(chatterConsole, hchatter);
                        instanceService.addMessageRecieveListener(handler);
                        instanceService.startServer();
                        isAppRunning = instanceService.isFailToRun();
                    } catch (BindException be) {
                        isAppRunning = instanceService.isFailToRun();
                    }
                    if (!isAppRunning) {
                        chatService = new Server(me.getServerPort(), chatterConsole);
                        chatService.setServerName("[Chatter Server v1.0]");
                        ChatServiceHandler chatServer = new ChatServiceHandler(chatterConsole, hchatter);
                        chatService.addMessageRecieveListener(chatServer);
                        chatService.startServer();
                        ftserver = new FileTransferServer(me.getFileTransferServerPort(), chatterConsole);
                        ftserver.setServerName("[File Transfer Server v1.0]");
                        ftserver.startServer();
                        frserver = new FileRecieverServer(me.getFileRecieverServerPort(), chatterConsole);
                        frserver.setServerName("[File Reciever Server v1.0]");
                        frserver.startServer();
                    }
                }
            } catch (Exception e) {
                chatterConsole.showConsole();
                chatterConsole.println(e);
            }
        }
    }

    public void initializeUsersChatHistory() {
        List<Users> vusers = Util.users;
        for (int i = 0; i < vusers.size(); i++) {
            ChatHistory ch = new ChatHistory();
            Users user = vusers.get(i);
            user.setID(user.getID());
            ch.setUser(user);
            ChatCache.addChatHistory(ch);
        }

    }

    public static JLabel getOptionLabel(String message) {
        JLabel lab = new JLabel(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        return lab;
    }

    public static JScrollPane getOptionMessage(String message) {
        JLabel lab = new JLabel();
        lab.setText(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        lab.setHorizontalTextPosition(4);
        JScrollPane pan = new JScrollPane(lab);
        pan.setPreferredSize(new Dimension(700, 150));
        pan.setMaximumSize(pan.getPreferredSize());
        pan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return pan;
    }
    public static JScrollPane getOptionMessage(String message,int width,int height) {
        JLabel lab = new JLabel();
        lab.setText(message);
        lab.setFont(new Font("segoe ui", 0, 12));
        lab.setForeground(Color.red);
        lab.setHorizontalTextPosition(4);
        JScrollPane pan = new JScrollPane(lab);
        pan.setPreferredSize(new Dimension(width, height));
        pan.setMaximumSize(pan.getPreferredSize());
        pan.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        pan.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        return pan;
    }
    public static void main(String args[]) {
        Thread mainThread = Thread.currentThread();
        Chatter startChatter = null;
        startChatter = new Chatter();

        if (!startChatter.configXML || !startChatter.userXML || !startChatter.jflag || !startChatter.settingsFlag) {
            String message = "<html>";
            if (!startChatter.jflag) {
                message = (new StringBuilder()).append(message).append("could not find joctopus.properties file inside application's JAR file<br/>").toString();
                message = (new StringBuilder()).append(message).append("make sure file exists and then try to run application<br/>").toString();
                message = (new StringBuilder()).append(message).append("OR it might be present but malformed, so please correct the file<br/>").toString();
                message = (new StringBuilder()).append(message).append("System will now exit !!!!.<br/>").toString();
                Util.showInformationDialog(null, true, getOptionLabel(message), applnProperties.getProperty("appln.name"), true);
            }
            if (!startChatter.settingsFlag) {
                message = (new StringBuilder()).append(message).append("could not find settings.ini file at following path:<br/>").toString();
                message = (new StringBuilder()).append(message).append(settingsFilePath).append("<br/>").toString();
                message = (new StringBuilder()).append(message).append("make sure file exists and then try to run application<br/>OR it might be present but malformed, so please correct the file<br/>").toString();
                message = (new StringBuilder()).append(message).append("System will now exit !!!!.<br/>").toString();
                Util.showInformationDialog(null, true, getOptionLabel(message), (new StringBuilder()).append(applnProperties.getProperty("appln.name")).append(" ").append("1.2").toString(), true);
            }
            if (!startChatter.configXML) {
                message = (new StringBuilder()).append(message).append("cannot find config.xml at following path:").append(configFilePath).append("<br/>").toString();
            }
            if (!startChatter.userXML) {
                message = (new StringBuilder()).append(message).append("cannot find users.xml at following path:").append(usersFilePath).append("<br/>").toString();
            }
            message = (new StringBuilder()).append(message).append("<hr>make sure that the file exists, and then try to run the application.<br>OR it might be present but malformed, so please correct the file<br/>System will now exit !!!!.<br/>").toString();
            message = (new StringBuilder()).append(message).append("(hint: Refer settings.ini at following path:)<br/>").append(settingsFilePath).toString();
            Util.showInformationDialog(null, false, getOptionMessage(message), (new StringBuilder()).append(applnProperties.getProperty("appln.name")).append(" ").append("1.2").toString(), true);
        } else {
            startChatter.startServices();
            if (!startChatter.isAppRunning) {
                try {
                    startChatter.hchatter.getChatterUI().createAndShowGUI();
                    startChatter.initializeUsersChatHistory();
                    startChatter.startChatterTray.initializeChatterTray();
                    startChatter.startChatterTray.chatterTray.setToolTip((new StringBuilder()).append(applnProperties.getProperty("appln.name")).append(" ").append("1.2").toString());
                    startChatter.startChatterTray.openChatterItem.addActionListener(startChatter);
                    startChatter.startChatterTray.aboutChatterItem.addActionListener(startChatter);
                    startChatter.startChatterTray.exitItem.addActionListener(startChatter);
                    startChatter.startChatterTray.chatterConsoleItem.addActionListener(startChatter);
                    mainThread.join();
                } catch (Exception e) {
                    if (startChatter != null) {
                        startChatter.chatterConsole.showConsole();
                        startChatter.chatterConsole.println(e);
                    }
                    e.printStackTrace();
                }
            } else {
                String message = "";
                message = (new StringBuilder()).append(message).append("<html>An instance of this application is already running.<br>Make sure its not already running by finding chatter tray icon on system tray, and then try to run again.<hr>If not then find value for &#060instance-port&#062 in config.xml, and check wheather another application is not already using this port<br>OR<br>change value for &#060instance-port&#062.<hr>System will now exit. !!!!").toString();
                Util.showInformationDialog(null, false, getOptionMessage(message), (new StringBuilder()).append(applnProperties.getProperty("appln.name")).append(" ").append("1.2").toString(), true);
            }
        }
    }
}
