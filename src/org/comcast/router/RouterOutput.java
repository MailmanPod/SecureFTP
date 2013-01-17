/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import java.util.ResourceBundle;
import javax.mail.MessagingException;
import javax.swing.JOptionPane;
import org.comcast.builder.Client;
import org.comcast.builder.Mail;
import org.comcast.exceptions.FTPConectionRefusedException;
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


/**
 *
 * @author Quality of Service
 */
public class RouterOutput implements Job {

    private static final String ROUTER_SERVICE_NAME = "router_output";

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
    private ResourceBundle routerOutput_es_ES = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_es_ES");

    private class Worker {

        private Server server;
        private StringBuilder showUploadFiles;
        private Mail mail;
        private ServerConfig config;

        public Worker(Server server, StringBuilder m, Mail k, ServerConfig c) {
            locale();
            this.server = server;
            this.showUploadFiles = m;
            this.config = c;
            this.mail = k;
        }
        
        private void locale(){
            try{
                
                Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
                case "Español":
                    routerOutput_es_ES  = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_es_ES");
                    break;
                case "Ingles":
                    routerOutput_es_ES  = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_en_US");
                    break;
                default:
                    routerOutput_es_ES  = ResourceBundle.getBundle("org/comcast/locale/RouterOutput_en_US");
                    break;
            }
                
            }catch(Exception ex){
                JOptionPane.showConfirmDialog(null, ex.getMessage(), "Error", JOptionPane.WARNING_MESSAGE);
            }
        }

        private void confirmMail() {
            try {
                String buffer = mail.getMailText();
                mail.setMailText(buffer + java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("org/comcast/locale/RouterOutput_es_ES").getString("\\N\\NARCHIVOS ENVIADOS AL SERVIDOR: {0}\\N\\N{1}"), new Object[] {config.getHostName(), showUploadFiles.toString()}));

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void confirmMail(String s) {
            try {
                mail.setMailText(java.text.MessageFormat.format(java.util.ResourceBundle.getBundle("org/comcast/locale/RouterOutput_es_ES").getString("\\N\\NERROR AL ENVIAR LOS ARCHIVOS AL SERVIDOR \\N\\N{0}"), new Object[] {s}));

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
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
