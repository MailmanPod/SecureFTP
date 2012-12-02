/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPFile;
import org.comcast.exceptions.UnderflowException;

/**
 *
 * @author Quality of Service
 */
public interface OutputChannel extends Serializable {

    public static final int MAX_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 5;
    public static final int LOW_PRIORITY = 10;

    public void uploadMessages() throws SocketException, IOException, UnderflowException;

    public void downloadMessages() throws SocketException, IOException, UnderflowException;

    public FTPFile[] retrieveMesseges(String dir) throws SocketException, IOException;
}
