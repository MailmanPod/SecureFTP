/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPFile;
import org.comcast.logic.Client;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.SimpleList;

/**
 *
 * @author Quality of Service
 */
public class RouterRetrieve {
    
    private Server server;
    private ServerConfig config;
    private SimpleList<Message> recursive;
    
    public RouterRetrieve(ServerConfig c){
        this.config = c;
        this.server = new Server(c);
    }
    
    public FTPFile[] getFiles(String dir) throws SocketException, IOException{
        return server.retrieveMesseges(dir);
    }
    
    public SimpleList<Message> getSimpleList(String dir) throws SocketException, IOException{
        SimpleList<Message> buffer = new SimpleList<>();
        FTPFile[] ftp = getFiles(dir);
        
        for(FTPFile file : ftp){
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "", dir + file.getName(), file);
            buffer.addLast(aux);
        }
        
        return buffer;
    }
    
    public SimpleList<Message> getAllFiles(String dir) throws SocketException, IOException{
        this.recursive = new SimpleList<>();
        FTPFile[] files = this.server.retrieveMesseges(dir);
        
        for(FTPFile ff: files){
            storeFiles(ff, dir);
        }
        
        return recursive;
    }
    
    private void storeFiles(FTPFile file, String dir) throws SocketException, IOException{
        
        if(file.isFile()){
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "", dir + file.getName(), file);
            this.recursive.addLast(aux);
        }
        
        if(file.isDirectory()){
            String dire = dir + file.getName() + "/";
            FTPFile[] p = getFiles(dire);
            
            for(FTPFile s : p){
                storeFiles(s, dire);
            }
        }
    }
}
