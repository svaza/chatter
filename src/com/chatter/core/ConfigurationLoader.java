 /*
 * =============================================================================
 * ConfigurationLoader.java
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

import com.chatter.ui.console.ChatterConsole;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.*;

/**
 * This class parses configuration file.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ConfigurationLoader {

    private List<Users> users;
    final private static String t = "";
    StringBuffer userData;
    Users user;
    ChatterConsole chatterConsole;
    private int counter;

    /**
     * A private constructor that initializes SAXParser for parsing XML file.
     * @param path                  The path of XML file to be parsed
     * @param chatterConsole        ChatterConsole object for writing debug output
     * @throws IOException
     * @throws FileNotFoundException
     */
    private ConfigurationLoader(String path, ChatterConsole chatterConsole)
            throws IOException, FileNotFoundException {
        users = new ArrayList<Users>();
        userData = new StringBuffer();
        user = new Users();
        counter = 0;
        this.chatterConsole = chatterConsole;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            parser.parse(new File(path), new ConfigurationParser());
        } catch (SAXException sx) {
            chatterConsole.showConsole();
            chatterConsole.println(sx);
        } catch (FileNotFoundException e) {
            throw e;
        } catch (IOException ioe) {
            throw ioe;
        } catch (Exception sx) {
            chatterConsole.println(sx);
        }
    }

    /**
     * returns ConfigurationLoader instance based on parameters given
     * 
     * @param path               The path of XML file to be parsed
     * @param chatterConsole     Chatter Console object for writing debug output.
     * @return                   ConfigurationLoader instance
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static ConfigurationLoader newInstance(String path, ChatterConsole chatterConsole)
            throws IOException, FileNotFoundException {
        synchronized (t) {
            return new ConfigurationLoader(path, chatterConsole);
        }
    }

    /**
     * returns List<Users>
     * @return         List<Users>
     */
    @SuppressWarnings("ReturnOfCollectionOrArrayField")
    public List<Users> getUsers() {
        return users;
    }

    /**
     * @param users           the users to set
     */
    public void setUsers(List<Users> users) {
        this.users = users;
    }

    /**
     * An inner class for parsing Configuration file.
     */
    private class ConfigurationParser extends HandlerBase {

        @Override
        public void startElement(String name, AttributeList list) {
            if (name.equals("user")) {
                user = new Users();
            }
            userData = new StringBuffer();
        }

        @Override
        public void characters(char ch[], int start, int length) {
            userData.append(ch, start, length);
        }

        @Override
        public void endElement(String name) {
            if (name.equals("ip-address")) {
                user.setIPAddress(userData.toString());
            } else if (name.equals("user-name")) {
                user.setUserName(userData.toString());
            } else if (name.equals("server-port")) {
                user.setServerPort((new Integer(userData.toString())).intValue());
            } else if (name.equals("handshake-server-port")) {
                user.setHandShakeServerPort((new Integer(userData.toString())).intValue());
            } else if (name.equals("filetransfer-server-port")) {
                user.setFileTransferServerPort((new Integer(userData.toString())).intValue());
            } else if (name.equals("instance-port")) {
                user.setInstancePort((new Integer(userData.toString())).intValue());
            } else if (name.equals("buffer-size")) {
                user.setBufferSize((new Integer(userData.toString())).intValue());
            } else if (name.equals("filerecieve-server-port")) {
                user.setFileRecieverServerPort((new Integer(userData.toString())).intValue());
            } else if (name.equals("pool-size")) {
                user.setPoolSize((new Integer(userData.toString())).intValue());
            } else if (name.equals("downloadqueue-size")) {
                user.setDownloadQueueSize((new Integer(userData.toString())).intValue());
            } else if (name.equals("user")) {
                user.setID(counter);
                counter++;
                getUsers().add(user);
            }
        }
    }
}
