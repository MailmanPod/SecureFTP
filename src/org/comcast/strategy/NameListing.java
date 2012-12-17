/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.strategy;

import java.io.File;
import java.io.IOException;
import java.net.SocketException;
import java.text.Collator;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.comcast.logic.ServerConfig;
import org.comcast.router.Message;
import org.comcast.router.RouterRetrieve;
import org.comcast.structures.SimpleList;

/**
 *
 * @author Quality of Service
 */
public class NameListing implements ListingStrategy{
    
    private int orderType;
    private RouterRetrieve results;

    public NameListing(ServerConfig c, int orderType) {
        this.orderType = orderType;
        results = new RouterRetrieve(c);
    }

    @Override
    public Message[] listLocalMessages(String pathName) {
        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            Message[] unsorted = localMessages.toArray(Message.class);
            
            Message[] sorted = localMessages.quickSort(unsorted, new NameComparator());
            
            return sorted;
            
        } catch (IOException ex) {
            Logger.getLogger(NameListing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public Message[] listRemoteFiles(String pathName) {
        try {
            SimpleList<Message> localMessages = results.getSimpleListCurrent(pathName);
            Message[] unsorted = localMessages.toArray(Message.class);
            
            Message[] sorted = localMessages.quickSort(unsorted, new NameComparator());
            
            return sorted;
            
        } catch (IOException ex) {
            Logger.getLogger(NameListing.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    @Override
    public String[] getRemoteDirectories(String pathName) {
        try{
            return results.getDirNamesCurrent(pathName);
        }catch(Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
    
    private class NameComparator implements Comparator<Message>{

        private Collator textComparator = Collator.getInstance();
        
        @Override
        public int compare(Message o1, Message o2) {
            
            return (orderType == ListingStrategy.ASC)? textComparator.compare(o1.getLocalFile().getName(), o2.getLocalFile().getName()): 
                    textComparator.compare(o2.getLocalFile().getName(), o1.getLocalFile().getName());
        }
        
    }
}
