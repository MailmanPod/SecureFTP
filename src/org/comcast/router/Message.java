/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.Serializable;
import org.apache.commons.net.ftp.FTPFile;
import org.comcast.exceptions.NullObjectParameterException;

/**
 *
 * @author Quality of Service
 */
public class Message implements Serializable, Comparable<Message> {

    public static final int HIGH_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 5;
    public static final int LOW_PRIORITY = 10;
    private InputChannel source;
    private int priority;
    private String localPath;
    private String remotePath;
    private String fileType;
    private FTPFile encapsulation;

    public Message(InputChannel source, int priority, String localPath, String remotePath, FTPFile en) {
        this.source = source;
        this.priority = priority;
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.encapsulation = en;
        this.fileType = "unknown";
    }

    /**
     * @return the source
     */
    public InputChannel getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(InputChannel source) {
        this.source = source;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    @Override
    public String toString() {
        return "Message{" + "source=" + source + ", priority=" + priority
                + ", localPath=" + localPath + ", remotePath=" + remotePath + '}';
    }

    @Override
    public int compareTo(Message o) {
        if (o == null) {
            throw new NullObjectParameterException("No hay mensaje para comparar <br> El mensaje esta vacio");
        }

        return this.getPriority() - o.getPriority();
    }

    /**
     * @return the localPath
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * @param localPath the localPath to set
     */
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /**
     * @return the remotePath
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * @param remotePath the remotePath to set
     */
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    /**
     * @return the encapsulation
     */
    public FTPFile getEncapsulation() {
        return encapsulation;
    }

    /**
     * @param encapsulation the encapsulation to set
     */
    public void setEncapsulation(FTPFile encapsulation) {
        this.encapsulation = encapsulation;
    }

    /**
     * @return the fileType
     */
    public String getFileType() {
        return this.fileType;
    }

    /**
     * @param fileType the fileType to set
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }
}
