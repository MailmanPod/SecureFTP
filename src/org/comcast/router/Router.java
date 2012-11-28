/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import org.comcast.structures.BinaryHeap;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Quality of Service
 */
public class Router implements OutputChannel, Job{

    @Override
    public void sendMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessages(BinaryHeap<Message> message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
