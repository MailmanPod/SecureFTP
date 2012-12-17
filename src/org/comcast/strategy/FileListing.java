/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.strategy;

import org.comcast.router.Message;

/**
 *
 * @author Quality of Service
 */
public class FileListing {
    private ListingStrategy strategy;
    
    public FileListing(){
        
    }
    
    public void setListingStrategy(ListingStrategy newStrategy){
        this.strategy = newStrategy;
    }
    
    public Message[] getLocalMessage(String pathName){
        return strategy.listLocalMessages(pathName);
    }
    
    public Message[] getRemoteMessages(String pathName){
        return strategy.listRemoteFiles(pathName);
    }
    
    public String[] getRemoteDirectories(String pathName){
        return strategy.getRemoteDirectories(pathName);
    }
}
