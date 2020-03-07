 /*
 * =============================================================================
 * FileScannerHandler.java
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

import java.io.File;

/**
 * The Controller for FileScannerUI
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class FileScannerHandler extends Thread {

    ChatterHandler chatter;
    File fileBulk[];
    FileScannerUI scanner;
    boolean done;
    boolean state;

    public FileScannerHandler(ChatterHandler chatter, File fileBulk[]) {
        done = false;
        state = true;
        this.chatter = chatter;
        this.fileBulk = fileBulk;
    }

    public void getFilesTillRoot(File folder, String previousParent) {
        if (state) {
            File files[] = folder.listFiles();
            if (files.length == 0) {
                chatter.handshakeMessage.append("\nno file\n").append(-999).append("\n").append(previousParent);
            }
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {
                    chatter.handshakeMessage.append("\n").append(files[i].getPath()).append("\n").append(files[i].length()).append("\n").append(previousParent);
                    chatter.path.append(files[i].getPath()).append("\n");
                    getScanner().setDisplayPath((new StringBuilder()).append("../../").append(files[i].getName()).toString());
                    continue;
                }
                if (files[i].isDirectory()) {
                    getFilesTillRoot(files[i], (new StringBuilder()).append(previousParent).append(System.getProperty("file.separator")).append(files[i].getName()).toString());
                }
            }
        }
    }

    public void startScan() {
        getScanner().start();
        for (int i = 0; i < fileBulk.length; i++) {
            if (!state) {
                continue;
            }
            if (fileBulk[i].isDirectory()) {
                getFilesTillRoot(fileBulk[i], fileBulk[i].getName());
            } else if (fileBulk[i].isFile()) {
                chatter.handshakeMessage.append("\n").append(fileBulk[i].getPath()).append("\n").append(fileBulk[i].length()).append("\n").append(fileBulk[i].getName());
                chatter.path.append(fileBulk[i].getPath()).append("\n");
                getScanner().setDisplayPath((new StringBuilder()).append("../../").append(fileBulk[i].getName()).toString());
            }
            if (i == fileBulk.length - 1) {
                state = false;
            }
        }

    }

    @Override
    public void run() {
        startScan();
        (new EndOperation(this)).start();
    }

    public FileScannerUI getScanner() {
        if (scanner == null) {
            scanner = new FileScannerUI(this);
        }
        return scanner;
    }
}
/**
 * A Class for performing the last task after scanning.
 */
class EndOperation extends Thread {

    FileScannerHandler handler;

    EndOperation(FileScannerHandler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        handler.chatter.getChatterUI().getFileTransferTab().getTxtChatArea().setText(handler.chatter.path.toString());
        handler.getScanner().getFileprogress().setIndeterminate(false);
        handler.getScanner().fileprogress.setValue(100);
        handler.getScanner().fileprogress.setString("scanning complete.");
        handler.chatter.getChatterUI().getFileTransferTab().getBtnBrowse().setEnabled(true);
        handler.chatter.getChatterUI().getFileTransferTab().getBtnClear().setEnabled(true);
        handler.chatter.getChatterUI().getFileTransferTab().getBtnSend().setEnabled(true);
        try {
            Thread.sleep(2000L);
        } catch (Exception e) {
            handler.chatter.chatterConsole.showConsole();
            handler.chatter.chatterConsole.println(e.getMessage());
        }
        handler.getScanner().getFileScannerDialog().dispose();
    }
}
