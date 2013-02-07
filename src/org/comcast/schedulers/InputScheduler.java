package org.comcast.schedulers;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.ResourceBundle;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.exceptions.InformationRequiredException;
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
import org.xml.sax.SAXException;

/**
 * Clase que realiza la planificacion de la tarea: descargar archivos desde el
 * servidor ftp.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 2.0
 */
public class InputScheduler implements SchedulerInterface {

    private static Scheduler scheduler;
    private ResourceBundle inputScheduler_es_ES;
    private ServerConfig configuration;
    private BinaryHeap<Message> downloadFiles;
    private Mail advice;
    private Server serverSender;
    private DateScheduler date;

    /**
     * Constructor de la clase.
     *
     * @param config Configuracion del servidor.
     * @param mess Cola de prioridad con los archivos a descargar.
     * @param mail Configuracion del mail.
     */
    public InputScheduler(ServerConfig config, BinaryHeap<Message> mess, Mail mail) {
        this.configuration = config;
        this.downloadFiles = mess;
        this.advice = mail;
        this.serverSender = new Server(mess, config);

        try {
            Client c = LoaderProvider.getInstance().getClientConfiguration();

            switch (c.getLocalization()) {
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

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException | URISyntaxException | InformationRequiredException ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Se modifica el objeto que realiza la planificacion de la tarea.
     *
     * @param s Nuevo planificador.
     */
    public void setScheduler(Scheduler s) {
        this.scheduler = s;
    }

    /**
     * Se modifica la fecha de efecucion de la tarea planificada.
     *
     * @param ds Nueva Fecha.
     */
    public void setDateScheduler(DateScheduler ds) {
        this.date = ds;
    }

    /**
     * Se encarga de configurar y empezar la tarea planificada.
     *
     * @throws SchedulerException Si hay error en la planificacion de la tarea.
     */
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

    /**
     * Se encarga de detener la tarea que se encuentra en ejecucion.
     *
     * @throws SchedulerException Si hay un error en la detencion de la tarea
     */
    @Override
    public final void stopJob() throws SchedulerException {
        scheduler.shutdown(true);
    }
}
