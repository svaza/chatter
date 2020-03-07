 /*
 * =============================================================================
 * ChatHistory.java
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

import com.chatter.core.Users;
import java.util.ArrayList;
import java.util.List;

/**
 * Collection of chat messages (ChatData) for a particular user
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatHistory {

    private Users user;
    private List<ChatData> chatBulk;
    public static final String LINE_SEPARATOR = "--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------";

    public ChatHistory() {
        chatBulk = new ArrayList<ChatData>();
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<ChatData> getChatBulk() {
        return chatBulk;
    }

    public void setChatBulk(List<ChatData> chatBulk) {
        this.chatBulk = chatBulk;
    }

    public void addChatData(ChatData cdata) {
        getChatBulk().add(cdata);
    }

    @Override
    public String toString() {
        StringBuilder buff = new StringBuilder();
        for (int i = 0; i < chatBulk.size(); i++) {
            ChatData cdata = chatBulk.get(i);
            if (cdata.isMe()) {
                buff.append("Me:-");
            } else {
                buff.append(getUser().getUserName()).append(":-");
            }
            buff.append("\n");
            buff.append(cdata.getMessage());
            buff.append("\n");
            buff.append(LINE_SEPARATOR);
            buff.append("\n");
        }

        return buff.toString();
    }
}
