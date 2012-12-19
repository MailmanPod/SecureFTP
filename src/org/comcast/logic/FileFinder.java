/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.logic;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.comcast.router.Message;
import org.comcast.router.RouterRetrieve;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.OpenAdressingHashTable;
import org.comcast.structures.SimpleList;

/**
 *
 * @author Quality of Service
 */
public class FileFinder {

    private RouterRetrieve results;

    public FileFinder(ServerConfig c) {
        results = new RouterRetrieve(c);
    }

    private OpenAdressingHashTable<Message, String> reloadLocal(String pathName) throws IOException {
        SimpleList<Message> localMessages = results.getLocalMessages(pathName);
        LocalIterator<Message> iter = localMessages.getIterador();

        OpenAdressingHashTable<Message, String> map = new OpenAdressingHashTable<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            map.putByKey(aux, aux.getLocalFile().getName());
            System.out.println(aux.getLocalFile().getName());
        }

        return map;
    }

    public Message getLocalExactName(String pathName, String localFileName) {
        Message r = null;
        try {
            OpenAdressingHashTable<Message, String> reloadLocal = reloadLocal(pathName);
            
            r = reloadLocal.get(localFileName);
            System.out.println("RERERFSDS;: " + r.getLocalFile().getName());
            return r;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public SimpleList<Message> getLocalNameAprox(String pathName, String localFileName){
        SimpleList<Message> aprox = new SimpleList<>();
        
        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            LocalIterator<Message> iter = localMessages.getIterador();
            
            while(iter.hasMoreElements()){
                Message aux = iter.returnElement();
                
                if(aux.getLocalFile().getName().startsWith(localFileName)){
                    aprox.addLast(aux);
                }
            }
            
            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }
    
    public SimpleList<Message> getLocalExtAprox(String pathName, String localFileName){
        SimpleList<Message> aprox = new SimpleList<>();
        String ph = "." + localFileName;
        
        try {
            SimpleList<Message> localMessages = results.getLocalMessages(pathName);
            LocalIterator<Message> iter = localMessages.getIterador();
            
            while(iter.hasMoreElements()){
                Message aux = iter.returnElement();
                
                if(aux.getLocalFile().getName().endsWith(ph)){
                    aprox.addLast(aux);
                }
            }
            
            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }
    
    private OpenAdressingHashTable<Message, String> reloadRemote(String pathName) throws IOException {
        SimpleList<Message> localMessages = results.getSimpleListCurrent(pathName);
        LocalIterator<Message> iter = localMessages.getIterador();

        OpenAdressingHashTable<Message, String> map = new OpenAdressingHashTable<>();

        while (iter.hasMoreElements()) {
            Message aux = iter.returnElement();
            map.putByKey(aux, aux.getFtpFile().getName());
        }

        return map;
    }
    
    public Message getRemoteExactName(String pathName, String remoteFileName) {
        Message r = null;
        try {
            OpenAdressingHashTable<Message, String> reloadRemote = reloadRemote(pathName);
            
            r = reloadRemote.get(remoteFileName);
            System.out.println("RERERFSDS;: " + r.getLocalFile().getName());
            return r;
        } catch (Exception ex) {
            return null;
        }
    }
    
    public SimpleList<Message> getRemoteNameAprox(String pathName, String remoteFileName){
        SimpleList<Message> aprox = new SimpleList<>();
        
        try {
            SimpleList<Message> remoteMessages = results.getSimpleListCurrent(pathName);
            LocalIterator<Message> iter = remoteMessages.getIterador();
            
            while(iter.hasMoreElements()){
                Message aux = iter.returnElement();
                
                if(aux.getLocalFile().getName().startsWith(remoteFileName)){
                    aprox.addLast(aux);
                }
            }
            
            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }
    
    public SimpleList<Message> getRemoteExtAprox(String pathName, String remoteFileName){
        SimpleList<Message> aprox = new SimpleList<>();
        String ph = "." + remoteFileName;
        
        try {
            SimpleList<Message> remoteMessages = results.getSimpleListCurrent(pathName);
            LocalIterator<Message> iter = remoteMessages.getIterador();
            
            while(iter.hasMoreElements()){
                Message aux = iter.returnElement();
                
                if(aux.getLocalFile().getName().endsWith(ph)){
                    aprox.addLast(aux);
                }
            }
            
            return aprox;
        } catch (Exception ex) {
            return aprox;
        }
    }
}
