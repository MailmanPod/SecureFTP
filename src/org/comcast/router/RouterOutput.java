/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.comcast.basic.Server;
import org.comcast.basic.ServerConfig;
import org.comcast.exceptions.UnderflowException;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.OpenAdressingHashTable;
import org.comcast.structures.SimpleList;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Quality of Service
 */
public class RouterOutput implements Job{
    private static final String ROUTER_SERVICE_NAME = "router_output";

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        BinaryHeap<Message> toSend = null;
        ServerConfig serverConfig = null;
        Server server = null;
        
        JobDetail detail = jec.getJobDetail();
        JobDataMap dataMap = detail.getJobDataMap();
        
        serverConfig = (ServerConfig) dataMap.get("comcast.config.serverconfig");
        server = (Server) dataMap.get("comcast.config.server");
        toSend = (BinaryHeap<Message>) dataMap.get("comcast.data.messages");
        
        new RouterWorkerThread(server);
    }
    
    private class RouterWorkerThread implements Runnable{

        private Server server;
        private Thread runner;

        public RouterWorkerThread(Server server) {
            this.server = server;
            runner = new Thread(this);
            runner.start();
        }
        
        @Override
        public void run() {
            try {
                server.uploadMessages();
            } catch (SocketException ex) {
                System.out.println("Exception Name: " + ex.getClass().getCanonicalName());
                ex.printStackTrace();
            } catch (    IOException | UnderflowException ex) {
                System.out.println("Exception Name: " + ex.getClass().getCanonicalName());
                ex.printStackTrace();
            }
        }
    }
}
