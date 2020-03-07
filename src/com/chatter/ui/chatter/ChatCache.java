 /*
 * =============================================================================
 * ChatCache.java
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
 * Stores the chat details of all users
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatCache {

    private static List<ChatHistory> vChatHistory = new ArrayList<ChatHistory>();

    public ChatCache() {
    }

    /**
     * Adds the chat history to ChatHistory Collection
     * @param history
     */
    public static void addChatHistory(ChatHistory history) {
        vChatHistory.add(history);
    }

    /**
     * sets a new ChatHistory for the user, the user should be available in
     * the ChatHistory collection.
     * This method is overriding call and will replace the ChatHistory.
     * @param user          :         The user whose the ChatHistory is to be added
     * @param ch            :         ChatHistory to be added for user
     */
    public static void setChatHistoryForUser(Users user, ChatHistory ch) {
        for (int i = 0; i < vChatHistory.size(); i++) {
            ChatHistory _ch = vChatHistory.get(i);
            Users chu = _ch.getUser();
            if (chu.getIPAddress().equals(user.getIPAddress())) {
                vChatHistory.add(i, ch);
                break;
            }
        }
    }

    /**
     * adds the chat data in ChatHistory for the user
     * @param data           :          The chat data to be added
     * @param user           :          The user for which the ChatData is to be added
     */
    public static void addChatData(ChatData data, Users user) {
        for (int i = 0; i < vChatHistory.size(); i++) {
            ChatHistory ch = vChatHistory.get(i);
            Users chu = ch.getUser();
            if (chu.getIPAddress().equals(user.getIPAddress())) {
                ch.addChatData(data);
                break;
            }
        }
    }

    /**
     * Finds and returns the ChatHistory for the user
     * @param user           :          User whose chat history is to be searched
     * @return               :          ChatHistory for the user
     */
    public static ChatHistory findHistory(Users user) {
        for (int i = 0; i < vChatHistory.size(); i++) {
            ChatHistory ch = vChatHistory.get(i);
            Users chu = ch.getUser();
            if (chu.getIPAddress().equals(user.getIPAddress())) {
                return ch;
            }
        }

        return null;
    }

    /**
     * Clears the ChatHistory for the user
     * @param user            :              The user whose chat history is to be cleared.
     */
    public static void clearHistory(Users user) {
        int i = 0;
        do {
            if (i >= vChatHistory.size()) {
                break;
            }
            ChatHistory ch = vChatHistory.get(i);
            Users chu = ch.getUser();
            if (chu.getIPAddress().equals(user.getIPAddress())) {
                ch.setChatBulk(new ArrayList<ChatData>());
                vChatHistory.add(i, ch);
                break;
            }
            i++;
        } while (true);
    }
}
