/*
 * =============================================================================
 * ChatterProtocol.java
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

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * A class for encapsulating and de-encapsulating the message with a specific format
 * @author Santosh Vaza
 * @version 1.0
 */
public class ChatterProtocol {

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
    /**
     * the subject of the message,
     * should be one of the following constants.
     * CHAT_MESSAGE
     * FILE_TRANSFER_APPROVAL
     * REQUEST_FILE_METADATA
     * FILE_TRANSFER_REQUEST
     */
    private String subject;
    /**
     * a hashtable for storing headers
     */
    private Hashtable headers;
    /**
     * holds the body of message
     */
    private StringBuilder messageBody;
    /**
     * the original encapsulated message.
     */
    private String message;

    /**
     * Constructor for decoding protocol message
     * @param message                :           Message to be decoded
     */
    private ChatterProtocol(String message) throws ChatterException {
        this.subject = "";
        this.messageBody = new StringBuilder();
        this.message = message;
        headers = new Hashtable();
        //split the message with new line character \n
        String temp[] = message.split("\\n");
        if (temp.length == 0) {
            throw new ChatterException("Invalid message");
        }
        if (!(isSubjectValid(temp[0]))) {
            throw new ChatterException("Invalid subject");
        }
        //set the subject globally
        this.subject = temp[0];
        //get the headers..
        int idx = 0;
        for (int i = 1; i < temp.length; i++) {
            String sTemp = temp[i];
            if (sTemp == null || sTemp.equals("")) {
                idx = i;
                break;
            } else {
                String key = sTemp.substring(0, sTemp.indexOf(":"));
                String value = sTemp.substring(sTemp.indexOf(":") + 1);
                headers.put(key, value);
            }
        }
        for (int i = (idx + 1); i < temp.length; i++) {
            messageBody.append(temp[i]);
            if (i < temp.length - 1) {
                messageBody.append("\n");
            }
        }
    }

    /**
     * Constructor for encoding protocol message
     * @param messageBody            :              The main body of message (OR the message it self to be encoded)
     * @param subject                :              The subject of protocol , it should be the valid types supported by ChatterProtocol
     */
    private ChatterProtocol(String messageBody, String subject) throws ChatterException {
        if (!(isSubjectValid(subject))) {
            throw new ChatterException("Invalid subject");
        }
        this.subject = subject;
        this.messageBody = new StringBuilder(messageBody);
        headers = new Hashtable();
    }

    private boolean isSubjectValid(String subject) {
        return (subject.equals(MessageType.CHAT_MESSAGE) || subject.equals(MessageType.FILE_TRANSFER_APPROVAL) || subject.equals(MessageType.FILE_TRANSFER_REQUEST) || subject.equals(MessageType.REQUEST_FILE_METADATA));
    }

    /**
     * for creating new protocol message
     * @param messageBody
     * @param subject
     * @return
     */
    public static ChatterProtocol getEncodeInstance(String messageBody, String subject) throws ChatterException {
        return new ChatterProtocol(messageBody, subject);
    }

    /**
     * For decoding protocol message
     * @param message
     * @return
     */
    public static ChatterProtocol getDecodeInstance(String message) throws ChatterException {
        return new ChatterProtocol(message);
    }

    /**
     * returns the hashtable of headers, each header is
     * a key value pair separated by colon.
     * @return
     */
    public Hashtable getHeaders() {
        return headers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessageBody() {
        return messageBody.toString();
    }

    public void setMessageBody(String messageBody) {
        this.messageBody =new StringBuilder(messageBody);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * adds the new header to message
     * @param header             the key or header prefix.
     * @param value              the value for header
     */
    public void addHeader(String header, String value) {
        headers.put(header, value);
    }

    @Override
    public String toString() {
        StringBuilder sBuild = new StringBuilder();
        //append subject
        sBuild.append(subject);
        sBuild.append("\n");

        //append headers
        Enumeration eKeys = headers.keys();
        while (eKeys.hasMoreElements()) {
            String key = eKeys.nextElement().toString();
            sBuild.append(key);
            sBuild.append(":");
            sBuild.append(headers.get(key));
            sBuild.append("\n");
        }
        sBuild.append("\n");
        sBuild.append(messageBody.toString());
        return sBuild.toString();
    }
}
