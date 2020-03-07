 /*
 * =============================================================================
 * ChatData.java
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

import java.io.Serializable;
import java.util.Date;

/**
 * Encapsulates the chat message information.
 * @version 1.0
 * @since 1.0
 * @author Santosh Vaza
 */
public class ChatData
        implements Serializable {

    private static final long serialVersionUID = 0xf4a24L;
    /**
     * the chat message.
     */
    private StringBuilder message;
    /**
     * whether the message was posted by user it self.
     */
    private boolean me;
    /**
     * time at which the message was posted.
     */
    private Date time;

    public ChatData() {
        message=new StringBuilder();
    }

    public String getMessage() {
        return message.toString();
    }

    public void setMessage(String message) {
        this.message=new StringBuilder(message);
    }

    public boolean isMe() {
        return me;
    }

    public void setMe(boolean me) {
        this.me = me;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
