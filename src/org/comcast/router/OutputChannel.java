/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.Serializable;
import org.comcast.structures.BinaryHeap;

/**
 *
 * @author Quality of Service
 */
public interface OutputChannel extends Serializable{
    
    public static final int MAX_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 5;
    public static final int LOW_PRIORITY = 10;
    
    public void sendMessage(Message message);
    public void sendMessages(BinaryHeap<Message> message);
}
