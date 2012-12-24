/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.File;
import java.io.Serializable;
import org.apache.commons.net.ftp.FTPFile;
import org.comcast.exceptions.NullObjectParameterException;

/**
 *
 * @author Quality of Service
 */
public class Message implements Serializable, Comparable<Message> {

    public static final int HIGH_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;
    private InputChannel source;
    private int priority;
    private String localPath;
    private String remotePath;
    private String fileType;
    private FTPFile ftpFile;
    private File localFile;

    public Message(InputChannel source, int priority, String localPath, String remotePath) {
        this.source = source;
        this.priority = priority;
        this.localPath = localPath;
        this.remotePath = remotePath;
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

    /**
     * @return the ftpFile
     */
    public FTPFile getFtpFile() {
        return ftpFile;
    }

    /**
     * @param ftpFile the ftpFile to set
     */
    public void setFtpFile(FTPFile ftpFile) {
        this.ftpFile = ftpFile;
    }

    /**
     * @return the localFile
     */
    public File getLocalFile() {
        return localFile;
    }

    /**
     * @param localFile the localFile to set
     */
    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getPriorityString() {

        switch (this.getPriority()) {
            case HIGH_PRIORITY:
                return "High";
            case NORMAL_PRIORITY:
                return "Normal";
            case LOW_PRIORITY:
                return "Low";
            default:
                return "null";
        }
    }
}
