/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.SimpleList;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Quality of Service
 */
public class RouterOutput implements OutputChannel, Job{

    @Override
    public void uploadMessage(Message message) throws SocketException, IOException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void uploadMessages(BinaryHeap<Message> message) throws SocketException, IOException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void downloadMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void downloadMessages(BinaryHeap<Message> messages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Message retrieveMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SimpleList<Message> retrieveMesseges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
