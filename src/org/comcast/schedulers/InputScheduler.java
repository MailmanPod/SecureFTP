/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.schedulers;

import java.util.Date;
import org.comcast.builder.Mail;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterInput;
import org.comcast.router.RouterOutput;
import org.comcast.structures.BinaryHeap;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.DateBuilder.*;
import static org.quartz.JobBuilder.newJob;

/**
 *
 * @author Quality of Service
 */
public class InputScheduler implements SchedulerInterface {

    private static Scheduler scheduler;
    private ServerConfig configuration;
    private BinaryHeap<Message> downloadFiles;
    private Mail advice;
    private Server serverSender;

    public InputScheduler(ServerConfig config, BinaryHeap<Message> mess, Mail mail) {
        this.configuration = config;
        this.downloadFiles = mess;
        this.advice = mail;
        this.serverSender = new Server(mess, config);
    }

    @Override
    public final void startJob() throws SchedulerException {
        System.out.println("------- Initializing ----------------------");

        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        scheduler = sf.getScheduler();

        System.out.println("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
        Date runTime = evenMinuteDate(new Date());

        System.out.println("------- Scheduling Job  -------------------");
        JobDataMap map = new JobDataMap();
        map.put("comcast.config.serverconfig", this.configuration);
        map.put("comcast.config.server", this.serverSender);
        map.put("comcast.data.messages", this.downloadFiles);
        map.put("comcast.data.mail", this.advice);

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(RouterInput.class)
                .withIdentity("Download_Files", "group1")
                .usingJobData(map)
                .build();

        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
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
            Thread.sleep(65L * 1000L);
            // executing...
        } catch (Exception e) {
        }
    }

    @Override
    public final void stopJob() throws SchedulerException {
        // shut down the scheduler
        System.out.println("------- Shutting Down ---------------------");
        scheduler.shutdown(true);
        System.out.println("------- Shutdown Complete -----------------");
    }
}
