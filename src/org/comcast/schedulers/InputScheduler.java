/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.schedulers;

import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.logic.DateScheduler;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterInput;
import org.comcast.structures.BinaryHeap;
import org.comcast.xml.LoaderProvider;
import static org.quartz.DateBuilder.*;
import static org.quartz.JobBuilder.newJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 *
 * @author Quality of Service
 */
public class InputScheduler implements SchedulerInterface {

    private static Scheduler scheduler;
    private ResourceBundle inputScheduler_es_ES;
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
        
        try{
            Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
                case "Español":
                    this.inputScheduler_es_ES = ResourceBundle.getBundle("org/comcast/locale/InputScheduler_es_ES");
                    break;
                case "Ingles":
                    this.inputScheduler_es_ES = ResourceBundle.getBundle("org/comcast/locale/InputScheduler_en_US");
                    break;
                default:
                    this.inputScheduler_es_ES = ResourceBundle.getBundle("org/comcast/locale/InputScheduler_en_US");
                    break;
            }
            
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void setScheduler(Scheduler s) {
        this.scheduler = s;
    }

    public void setDateScheduler(DateScheduler ds) {
        this.date = ds;
    }

    @Override
    public final void startJob() throws SchedulerException {

        Date runTime = dateOf(date.getHour(), date.getMinute(), date.getSecond(), date.getDay(), date.getMonth(), date.getYear());

        String aux = advice.getMailText();
        String form = java.text.MessageFormat.format(this.inputScheduler_es_ES.getString("\\N" + "DATOS DE FECHA: {0}"), new Object[]{runTime});
        advice.setMailText(aux + form);

        JobDataMap map = new JobDataMap();
        map.put("comcast.config.serverconfig", this.configuration);
        map.put("comcast.config.server", this.serverSender);
        map.put("comcast.data.messages", this.downloadFiles);
        map.put("comcast.data.mail", this.advice);

        JobDetail job = newJob(RouterInput.class)
                .withIdentity("Download_Files", "download")
                .usingJobData(map)
                .build();

        Trigger trigger = newTrigger()
                .withIdentity("trigger1_download", "download")
                .startAt(runTime)
                .build();

        scheduler.scheduleJob(job, trigger);

        scheduler.start();
        try {
            long end = runTime.getTime();
            long start = System.currentTimeMillis();
            long res = end - start;

            Thread.sleep(res);

        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(null, 
                    java.text.MessageFormat.format(this.inputScheduler_es_ES.getString("EXCEPTION: \\N{0}"), new Object[]{ex.toString()}), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            
            scheduler.shutdown(true);
        }
    }

    @Override
    public final void stopJob() throws SchedulerException {
        scheduler.shutdown(true);
    }
}
