/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.schedulers;

import org.quartz.SchedulerException;

/**
 *
 * @author Quality of Service
 */
public interface SchedulerInterface {

    public void startJob() throws SchedulerException;

    public void stopJob() throws SchedulerException;
}
