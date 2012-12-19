/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.strategy;

import java.io.IOException;
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
public class SizeListing implements ListingStrategy{

    private int orderType;
    private RouterRetrieve results;
    
    public SizeListing(ServerConfig c, int orderType) {
        this.orderType = orderType;
        results = new RouterRetrieve(c);
    }
    
    @Override
    public Message[] listLocalMessages(String pathName) {
        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            Message[] unsorted = localMessages.toArray(Message.class);
            
            Message[] sorted = localMessages.quickSort(unsorted, new SizeComparator());
            
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
            
            Message[] sorted = localMessages.quickSort(unsorted, new SizeComparator());
            
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
    
    private class SizeComparator implements Comparator<Message>{
        
        @Override
        public int compare(Message o1, Message o2) {
            
            if(o1.getLocalFile() != null && o2.getLocalFile() != null){
                int result = 0;
                if(orderType == ASC){
                    result = (int) (o1.getLocalFile().length() - o2.getLocalFile().length());
                }else{
                    result = (int) (o2.getLocalFile().length() - o1.getLocalFile().length());
                }
                
                return result;
            }
            
            if(o1.getFtpFile() != null && o2.getFtpFile() != null){
                int result = 0;
                if(orderType == ASC){
                    result = (int) (o1.getFtpFile().getSize() - o2.getFtpFile().getSize());
                }else{
                    result = (int) (o2.getFtpFile().getSize() - o1.getFtpFile().getSize());
                }
                
                return result;
            }
            
            return 0;
        }
    }
}
