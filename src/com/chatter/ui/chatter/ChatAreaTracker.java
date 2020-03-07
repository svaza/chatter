 /*
 * =============================================================================
 * ChatAreaTracker.java
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Tracks the key event ALT+S combination on ChatArea
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatAreaTracker extends KeyAdapter {

    private int alt;
    private IChat iChat;

    public ChatAreaTracker(IChat iChat) {
        alt = -1;
        this.iChat = iChat;
    }

    public IChat getiChat() {
        return iChat;
    }

    public void setiChat(IChat iChat) {
        this.iChat = iChat;
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_S && alt == KeyEvent.VK_ALT) {
            iChat.sendMessage();
        }
        if (e.getKeyCode() == KeyEvent.VK_ALT) {
            alt = e.getKeyCode();
        }
    }

    public void keyReleased(KeyEvent e) {
        alt = -1;
    }
}
