/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import javax.mail.MessagingException;
import org.comcast.builder.Mail;
import org.comcast.exceptions.UnderflowException;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.LocalIterator;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Quality of Service
 */
public class RouterInput implements Job {

    private static final String ROUTER_SERVICE_NAME = "router_input";

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

        new RouterWorkerThread(server, show, mail, serverConfig);
    }

    private class RouterWorkerThread implements Runnable {

        private Server server;
        private StringBuilder showUploadFiles;
        private Mail mail;
        private ServerConfig config;
        private Thread runner;

        public RouterWorkerThread(Server server, StringBuilder m, Mail k, ServerConfig c) {
            this.server = server;
            this.showUploadFiles = m;
            this.config = c;
            this.mail = k;

            runner = new Thread(this);
            runner.start();
        }

        private void confirmMail() {
            try {
                String buffer = mail.getMailText();
                mail.setMailText(buffer + "\nArchivos descargados del servidor: " + config.getHostName() + "\n" + showUploadFiles.toString());

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                //JOptionPane
                System.exit(1);
            }
        }

        private void confirmMail(String s) {
            try {
                mail.setMailText("Error al descargar los archivos del servidor \n\n" + s);

                mail.initSession();
                mail.createMail();
                mail.sendMail();

            } catch (MessagingException ex) {
                //JOptionPane
                System.exit(1);
            }
        }

        @Override
        public void run() {
            try {
                server.downloadMessages();

                confirmMail();

            } catch (SocketException ex) {
                confirmMail(ex.getMessage());
            } catch (IOException | UnderflowException ex) {
                confirmMail(ex.getMessage());
            }
        }
    }
}
