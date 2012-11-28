/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.File;
import java.io.Serializable;
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
    private File message;
    private int priority;

    public Message(InputChannel source, File message, int priority) {
        this.source = source;
        this.message = message;
        this.priority = priority;
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
     * @return the message
     */
    public File getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(File message) {
        this.message = message;
    }

    /**
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "Message{" + "source=" + source + ", message=" + message + ", priority=" + priority + '}';
    }

    @Override
    public int compareTo(Message o) {
        if(o == null){
            throw new NullObjectParameterException("No hay mensaje para comparar <br> El mensaje esta vacio");
        }
        
        return this.getPriority() - o.getPriority();
    }
    
}
