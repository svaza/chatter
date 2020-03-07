 /*
 * =============================================================================
 * ChatterTray.java
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

import java.awt.*;

/**
 * Thread for running chatter tray
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatterTray extends Thread {

    public SystemTray chatterSystemTay;
    public TrayIcon chatterTray;
    public PopupMenu chatterPopupMenu;
    public MenuItem exitItem;
    public MenuItem openChatterItem;
    public MenuItem aboutChatterItem;
    public MenuItem chatterConsoleItem;
    private static final String EXIT = "Exit";
    private static final String OPEN_CHATTER = (new StringBuilder()).append("Open ").append(Chatter.getApplicationProperty("appln.name")).toString();
    private static final String ABOUT_CHATTER = "About..";
    private static final String SHOW_CONSOLE = "Show Console";

    @Override
    public void run() {
        initializeChatterTray();
    }

    public void initializeChatterTray() {
        if (SystemTray.isSupported()) {
            chatterSystemTay = SystemTray.getSystemTray();
            chatterTray = new TrayIcon(Toolkit.getDefaultToolkit().getImage(ChatterTray.class.getResource(Chatter.getApplicationProperty("appln.tray-image"))));
            exitItem = new MenuItem(EXIT);
            exitItem.setActionCommand("EXIT");
            openChatterItem = new MenuItem(OPEN_CHATTER);
            openChatterItem.setActionCommand("OPEN_CHATTER");
            aboutChatterItem = new MenuItem(ABOUT_CHATTER);
            aboutChatterItem.setActionCommand("ABOUT_CHATTER");
            chatterConsoleItem = new MenuItem(SHOW_CONSOLE);
            chatterConsoleItem.setActionCommand("SHOW_CONSOLE");
            chatterPopupMenu = new PopupMenu();
            chatterPopupMenu.add(openChatterItem);
            chatterPopupMenu.add(aboutChatterItem);
            chatterPopupMenu.add(chatterConsoleItem);
            chatterPopupMenu.addSeparator();
            chatterPopupMenu.add(exitItem);
            chatterPopupMenu.setFont(new Font("segoe ui", 0, 12));
            chatterTray.setPopupMenu(chatterPopupMenu);
            try {
                chatterSystemTay.add(chatterTray);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
