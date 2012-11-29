/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.testing;

import java.io.File;
import java.io.IOException;
import org.comcast.basic.Client;
import org.comcast.basic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.schedulers.OutputScheduler;
import org.comcast.structures.BinaryHeap;
import org.quartz.SchedulerException;

/**
 *
 * @author Quality of Service
 */
public class Tester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws SchedulerException, IOException {
        // TODO code application logic here
        ServerConfig config = new ServerConfig("QoS-PC");
        config.setUserLogin("adminroot");
        config.setPassLogin("adminroot");

        Message archivo = new Message(new Client(), new File("RSAPrivate.key"),
                Message.HIGH_PRIORITY, "D:\\Proyectos en NetBeans 9\\neuromancerV1\\FTPServer2\\RSAPrivate.key", "RSAPrivate.key");
        
        Message archivo1 = new Message(new Client(), new File("RSAPublic.key"), 
                Message.HIGH_PRIORITY, "D:\\Proyectos en NetBeans 9\\neuromancerV1\\FTPServer2\\RSAPublic.key", "RSAPublic.key");
        
        Message archivo2 = new Message(new Client(), new File("UPLOADER.txt"), 
                Message.LOW_PRIORITY, "D:\\Proyectos en NetBeans 9\\neuromancerV1\\FTPServer2\\UPLOADER.txt", "UPLOADER.txt");
        
        Message archivo3 = new Message(new Client(), new File("retrieve.txt"), 
                Message.NORMAL_PRIORITY, "D:\\Proyectos en NetBeans 9\\neuromancerV1\\FTPServer2\\retrieve.txt", "retrieve.txt");

        BinaryHeap<Message> pila = new BinaryHeap<>();
        pila.insert(archivo3);
        pila.insert(archivo);
        pila.insert(archivo2);
        pila.insert(archivo1);
        

        OutputScheduler s = new OutputScheduler(config, pila);
        s.startJob();

        System.out.println("Pausa... pulse una tecla para finalizar la aplicación");
        System.in.read();
        
        s.stopJob();
    }
}
