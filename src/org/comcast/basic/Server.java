/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.basic;

import org.comcast.exceptions.NullObjectParameterException;
import org.comcast.exceptions.UnderflowException;
import org.comcast.router.Message;
import org.comcast.router.OutputChannel;
import org.comcast.structures.BinaryHeap;

/**
 *
 * @author Quality of Service
 */
public class Server implements Comparable<Server>, OutputChannel {

    private BinaryHeap<Message> messageToSend;
    private int serverPriority;
    
    public Server(){
        messageToSend = new BinaryHeap<>();
    }
    
    public Server(Message[] group){
        messageToSend = new BinaryHeap<>(group);
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
    public void sendMessage(Message message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendMessages(BinaryHeap<Message> message) {
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
    
}
