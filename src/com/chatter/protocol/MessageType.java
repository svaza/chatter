/*
 * =============================================================================
 * MessageType.java
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
package com.chatter.protocol;

/**
 * An interface for defining the types of subject used in chatter protocol
 * @author Santosh Vaza
 * @version 1.0
 */
public interface MessageType {

    /**
     * A subject for identifying that the message is a chat message.
     */
    public static final String CHAT_MESSAGE = "CHAT-MESSAGE";
    /**
     * A subject for identifying that the message is a
     * File transfer approval
     */
    public static final String FILE_TRANSFER_APPROVAL = "FILE-TRANSFER-APPROVAL";
    /**
     * A subject for identifying that the message is a
     * File Transfer Metadata request
     */
    public static final String REQUEST_FILE_METADATA = "REQUEST-FILE-METADATA";
    /**
     * A subject for identifying that the message is a
     * File transfer Request
     */
    public static final String FILE_TRANSFER_REQUEST = "FILE-TRANSFER-REQUEST";
}
