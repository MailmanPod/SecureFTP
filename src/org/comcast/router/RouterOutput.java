package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import java.net.URISyntaxException;
import java.util.ResourceBundle;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.exceptions.UnderflowException;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.LocalIterator;
import org.comcast.xml.LoaderProvider;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.xml.sax.SAXException;

/**
 * Clase encargada de realizar la tarea de subir los archivos encriptados al
 * servidor ftp. <br> Define una implementacion del patron Router.<br> La tarea
 * se ejecutara segun el momento de tiempo definido por el usuario.<br>
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 3.5
 */
public class RouterOutput implements Job {

    private static final String ROUTER_SERVICE_NAME = "router_output";

    /**
     * Aqui se realiza el seteo de los parametros que se utilizaran en la
     * ejecucion de la tarea programada.<br> Ademas se setean los parametros
     * para la notificacion por mail de la tarea realizada.
     *
     * @param jec Es el contexto del trabajo. Aqui se pasan datos como por
     * ejemplo, La fecha de inicio de la tarea, junto a otros parametros de
     * ejecucion.
     * @throws JobExecutionException Si existe algun problema mientras se setean
     * los parametros de ejecucion de la tarea programada.
     */
    @Override
    public final void execute(JobExecutionContext jec) throws JobExecutionException {
        BinaryHeap<Message> toSend = null;
        ServerConfig serverConfig = null;
        Server server = null;
        Mail mail = null;

        JobDetail detail = jec.getJobDetail();
        JobDataMap dataMap = detail.getJobDataMap();

        serverConfig = (ServerConfig) dataMap.get("comcast.config.serverconfig");
        server = (Server) dataMap.get("comcast.config.server");
        toSend = (BinaryHeap<Message>) dataMap.get("comcast.data.messages");
        mail = (Mail) dataMap.get("comcast.data.mail");

        StringBuilder show = new StringBuilder();

        LocalIterator<Message> iter = toSend.getIterator();
        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            show.append("\n").append(aux.getLocalPath());
        }
        Worker routerWorkerThread = new Worker(server, show, mail, serverConfig);
        routerWorkerThread.run();
    }
    private ResourceBundle routerOutput_es_ES;

    /**
     * Clase privada encargada de realizar la tarea y la notificacion
     */
    private class Worker {

        private Server server;
        private StringBuilder showUploadFiles;
        private Mail mail;
        private ServerConfig config;

        /**
         * Constructor de la clase.
         *
         * @param server Quien realiza la tarea verdaderamente.
         * @param m Contenido del mail.
         * @param k Configuracion del Mail.
         * @param c Configuracion del servidor.
         */
        public Worker(Server server, StringBuilder m, Mail k, ServerConfig c) {
            locale();
            this.server = server;
            this.showUploadFiles = m;
            this.config = c;
            this.mail = k;
        }

        /**
         * Define el lenguaje de la aplicacion.
         */
        private void locale() {
            try {

                Client c = LoaderProvider.getInstance().getClientConfiguration();

                switch (c.getLocalization()) {
                    case "Español":
                        routerOutput_es_ES = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_es_ES");
                        break;
                    case "Ingles":
                        routerOutput_es_ES = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_en_US");
                        break;
                    default:
                        routerOutput_es_ES = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_en_US");
                        break;
                }

            } catch (ParserConfigurationException | SAXException | IOException | TransformerException | URISyntaxException | InformationRequiredException ex) {
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        /**
         * Confirmacion por mail de exito
         */
        private void confirmMail() {
            try {
                String buffer = mail.getMailText();
                mail.setMailText(buffer + java.text.MessageFormat.format(routerOutput_es_ES.getString("\\N\\NARCHIVOS ENVIADOS AL SERVIDOR: {0}\\N\\N{1}"), new Object[]{config.getHostName(), showUploadFiles.toString()}));

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Confirmacion por mail de error / falla
         *
         * @param s
         */
        private void confirmMail(String s) {
            try {
                mail.setMailText(java.text.MessageFormat.format(routerOutput_es_ES.getString("\\N\\NERROR AL ENVIAR LOS ARCHIVOS AL SERVIDOR \\N\\N{0}"), new Object[]{s}));

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        /**
         * Realiza el trabajo programado y confirma.
         */
        public void run() {
            try {
                server.uploadMessages();

                confirmMail();

            } catch (SocketException ex) {
                confirmMail(ex.getMessage());
            } catch (IOException | FTPConectionRefusedException | UnderflowException ex) {
                confirmMail(ex.getMessage());
            }
        }
    }
}
