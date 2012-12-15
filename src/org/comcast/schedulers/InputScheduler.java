/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.schedulers;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.comcast.builder.Mail;
import org.comcast.logic.DateScheduler;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterInput;
import org.comcast.router.RouterOutput;
import org.comcast.structures.BinaryHeap;
import org.quartz.CronExpression;
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
import org.quartz.impl.triggers.CronTriggerImpl;

/**
 *
 * @author Quality of Service
 */
public class InputScheduler extends Thread implements SchedulerInterface {

    private static Scheduler scheduler;
    private ServerConfig configuration;
    private BinaryHeap<Message> downloadFiles;
    private Mail advice;
    private Server serverSender;
    private DateScheduler date;

    public InputScheduler(ServerConfig config, BinaryHeap<Message> mess, Mail mail) {
        this.configuration = config;
        this.downloadFiles = mess;
        this.advice = mail;
        this.serverSender = new Server(mess, config);
    }

    public void setScheduler(Scheduler s) {
        this.scheduler = s;
    }

    public void setDateScheduler(DateScheduler ds) {
        this.date = ds;
    }

    @Override
    public final void startJob() throws SchedulerException {
        System.out.println("------- Initializing ----------------------");

        // First we must get a reference to a scheduler

        //scheduler = sf.getScheduler();

        System.out.println("------- Initialization Complete -----------");

        // computer a time that is on the next round minute
        //Date runTime = evenMinuteDate(new Date());
//        Date runTime = dateOf(20, 54, 00, 9, DECEMBER, 2012);
        Date runTime = dateOf(date.getHour(), date.getMinute(), date.getSecond(), date.getDay(), date.getMonth(), date.getYear());

        String aux = advice.getMailText();
        String form = "\n" + "Datos de fecha: " + runTime;
        advice.setMailText(aux + form);

        System.out.println("------- Scheduling Job  -------------------");
        JobDataMap map = new JobDataMap();
        map.put("comcast.config.serverconfig", this.configuration);
        map.put("comcast.config.server", this.serverSender);
        map.put("comcast.data.messages", this.downloadFiles);
        map.put("comcast.data.mail", this.advice);

        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(RouterInput.class)
                .withIdentity("Download_Files", "download")
                .usingJobData(map)
                .build();

        // Trigger the job to run on the next round minute
        Trigger trigger = newTrigger()
                .withIdentity("trigger1_download", "download")
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

        try {
            // wait 65 seconds to show job
            long end = runTime.getTime();
            long start = System.currentTimeMillis();
            long res = end - start;
            System.out.println("------- Waiting " + res + " seconds... -------------");
            sleep(res);
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
    public void run() {
        try {
            startJob();

            stopJob();
        } catch (SchedulerException ex) {
            System.out.println("Exception: " + ex.toString());
            ex.printStackTrace();
        }
    }
}
