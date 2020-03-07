/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.chatter.core;

import java.io.Serializable;

/**
 *
 * @author santoshitc
 */
public class FileMetadata implements Serializable{

    protected String name;
    protected long size;
    protected String statusCode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String toString(){
        StringBuilder str=new StringBuilder();
        str.append("STATUS:"+this.getStatusCode()+"\n");
        str.append("NAME:"+this.getName()+"\n");
        str.append("SIZE:"+this.getSize()+"\n");
        return str.toString();
    }


}
