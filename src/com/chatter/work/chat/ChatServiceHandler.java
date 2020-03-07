 /*
 * =============================================================================
 * ChatServiceHandler.java
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
package com.chatter.work.chat;

import com.chatter.core.Users;
import com.chatter.core.Util;
import com.chatter.protocol.ChatterProtocol;
import com.chatter.server.MessageRecieveListener;
import com.chatter.server.Server;
import com.chatter.ui.console.ChatterConsole;
import com.chatter.ui.chatter.*;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * Processes the chat messages.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatServiceHandler
        implements MessageRecieveListener {

    ChatterConsole chatterConsole;
    ChatterHandler handler;

    public ChatServiceHandler(ChatterConsole chatterConsole, ChatterHandler handler) {
        this.chatterConsole = chatterConsole;
        this.handler = handler;
    }

    @Override
    public void recieveMessage(Server server) {
        try {
            InputStream inputMessage = server.getRecievedClientSocketChannel().socket().getInputStream();
            List<Users> vuser = Util.searchByIPAddress(server.getRecievedClientSocketChannel().socket().getInetAddress().getHostAddress(), Util.users);
            StringBuilder message = new StringBuilder();
            byte bMessage[] = new byte[1024];
            for (int read = -1; (read = inputMessage.read(bMessage)) != -1;) {
                message.append(new String(bMessage, 0, read));
            }
            ChatterProtocol protocol = ChatterProtocol.getDecodeInstance(message.toString());
            if (protocol.getSubject().equals(ChatterProtocol.CHAT_MESSAGE)) {
                Users user = vuser.get(0);
                StringBuilder msg = new StringBuilder();
                msg.append(user.getUserName()).append(":-\n").append(protocol.getMessageBody()).append("\n--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------\n");

                ChatData cdata = new ChatData();
                cdata.setMe(false);
                cdata.setMessage(protocol.getMessageBody());
                cdata.setTime(new Date());
                ChatCache.addChatData(cdata, user);
                Users currentUser = handler.getCurrentUser();
                if (currentUser != null) {
                    if (user.getIPAddress().equals(currentUser.getIPAddress())) {
                        handler.getChatterUI().getChatTab().appendHistory(msg.toString());
                        handler.getChatterUI().getChatTab().moveChatHistoryScrollBarAtEnd();
                    }
                }
                handler.getChatBoard().createORShowChatTabForUser(user, true, true);
            } else {
            }
        } catch (Exception e) {
            chatterConsole.println(e);
        }
    }
}
