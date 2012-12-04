/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.schedulers;

import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.comcast.builder.Mail;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterOutput;
import org.comcast.structures.BinaryHeap;
import static org.quartz.DateBuilder.*;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.impl.StdSchedulerFactory;

/**
 *
 * @author Quality of Service
 */
public class OutputScheduler extends Thread implements SchedulerInterface{

    private static Scheduler scheduler;
    private ServerConfig configuration;
    private BinaryHeap<Message> uploadFiles;
    private Mail advice;
    private Server serverSender;

    public OutputScheduler(ServerConfig config, BinaryHeap<Message> mess, Mail mail) {
        this.configuration = config;
        this.uploadFiles = mess;
        this.advice = mail;
        this.serverSender = new Server(mess, config);
    }

    public void setScheduler(Scheduler s){
        this.scheduler = s;
    }
    
    @Override
    public final void startJob() throws SchedulerException {
        System.out.println("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        //SchedulerFactory sf = new StdSchedulerFactory();
        //scheduler = sf.getScheduler();

        System.out.println("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
        Date runTime = evenMinuteDate(new Date());

        System.out.println("------- Scheduling Job  -------------------");
        JobDataMap map = new JobDataMap();
        map.put("comcast.config.serverconfig", this.configuration);
        map.put("comcast.config.server", this.serverSender);
        map.put("comcast.data.messages", this.uploadFiles);
        map.put("comcast.data.mail", this.advice);

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(RouterOutput.class)
                .withIdentity("Uploading_Files", "upload")
                .usingJobData(map)
                .build();

        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger_upload", "upload")
                .startAt(runTime)
                .build();

        // Tell quartz to schedule the job using our trigger
        scheduler.scheduleJob(job, trigger);
        System.out.println(job.getKey() + " will run at: " + runTime);

        // Start up the scheduler (nothing can actually run until the 
        // scheduler has been started)
        scheduler.start();

        System.out.println("------- Started Scheduler -----------------");

        // wait long enough so that the scheduler as an opportunity to 
        // run the job!
        System.out.println("------- Waiting 65 seconds... -------------");
        try {
            // wait 65 seconds to show job
            sleep(65L * 1000L);
            // executing...
        } catch (InterruptedException ex) {
            System.out.println("Exception: \n" + ex.toString());
            scheduler.shutdown(true);
        }
    }

    @Override
    public final void stopJob() throws SchedulerException {
        // shut down the scheduler
        System.out.println("------- Shutting Down ---------------------");
        scheduler.shutdown(true);
        System.out.println("------- Shutdown Complete -----------------");
    }
    
    @Override
    public void run(){
        try {
            startJob();
            
            stopJob();
        } catch (SchedulerException ex) {
            System.out.println("Exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
