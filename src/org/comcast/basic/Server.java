/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.basic;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.comcast.exceptions.NullObjectParameterException;
import org.comcast.exceptions.UnderflowException;
import org.comcast.router.Message;
import org.comcast.router.OutputChannel;
import org.comcast.structures.BinaryHeap;
import org.comcast.structures.SimpleList;

/**
 *
 * @author Quality of Service
 */
public class Server implements Comparable<Server>, OutputChannel {

    private BinaryHeap<Message> messageToSend;
    private int serverPriority;
    private FTPClient client;
    
    public Server(){
        messageToSend = new BinaryHeap<>();
        client = new FTPClient();
    }
    
    public Server(Message[] group){
        messageToSend = new BinaryHeap<>(group);
        client = new FTPClient();
    }
    
    public void addMessage(Message send){
        this.messageToSend.insert(send);
    }
    
    public Message takeMessage() throws UnderflowException{
        return this.messageToSend.deleteMin();
    }
    
    public Message findMaxPriority() throws UnderflowException{
        return this.messageToSend.findMin();
    } 
    
    @Override
    public int compareTo(Server o) {
        if(o == null){
            throw new NullObjectParameterException("El objeto servidor esta vacio");
        }
        
        return (this.getServerPriority() - o.getServerPriority());
    }

    @Override
    public void uploadMessage(Message message) throws SocketException, IOException{
        
        FileInputStream fis = null;
        
        client.connect("localhost");
        client.login("adminroot", "adminroot");
        
        String local = ".\\FTPServer2\\UPLOADER.txt";
        String remote = "UPLOADER.txt";
        
        client.setFileTransferMode(FTP.BINARY_FILE_TYPE);
        
        fis = new FileInputStream(local);
        client.storeFile(remote, fis);
        
        client.noop();
        
        fis.close();
        client.disconnect();
    }

    @Override
    public void uploadMessages(BinaryHeap<Message> messages) throws SocketException, IOException{
        throw new UnsupportedOperationException("Not supported yet.");
    }

    /**
     * @return the serverPriority
     */
    public int getServerPriority() {
        return serverPriority;
    }

    /**
     * @param serverPriority the serverPriority to set
     */
    public void setServerPriority(int serverPriority) {
        this.serverPriority = serverPriority;
    }

    @Override
    public Message retrieveMessage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public SimpleList<Message> retrieveMesseges() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void downloadMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void downloadMessages(BinaryHeap<Message> messages) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
