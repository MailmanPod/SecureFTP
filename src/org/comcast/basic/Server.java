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
    private ServerConfig config;

    public Server(ServerConfig c) {
        messageToSend = new BinaryHeap<>();
        client = new FTPClient();
        config = c;
    }

    public Server(BinaryHeap<Message> group, ServerConfig c) {
        messageToSend = group;
        client = new FTPClient();
        config = c;
    }

    public void addMessage(Message send) {
        this.messageToSend.insert(send);
    }

    public Message takeMessage() throws UnderflowException {
        return this.messageToSend.deleteMin();
    }

    public Message findMaxPriority() throws UnderflowException {
        return this.messageToSend.findMin();
    }

    @Override
    public int compareTo(Server o) {
        if (o == null) {
            throw new NullObjectParameterException("El objeto servidor esta vacio");
        }

        return (this.getServerPriority() - o.getServerPriority());
    }

    @Override
    public void uploadMessage(Message message) throws SocketException, IOException {

        FileInputStream fis = null;

        client.connect(config.getIpAddress());
        client.login(config.getUserLogin(), config.getPassLogin());

        /*String local = ".\\FTPServer2\\UPLOADER.txt";
         String remote = "UPLOADER.txt";*/

        String local = message.getLocalPath();
        String remote = message.getRemotePath();

        client.setFileTransferMode(FTP.BINARY_FILE_TYPE);

        fis = new FileInputStream(local);
        client.storeFile(remote, fis);

        client.noop();

        fis.close();
        client.disconnect();
    }

    @Override
    public void uploadMessages() throws SocketException, IOException, UnderflowException {
        Message toSend = null;

        while (!this.messageToSend.isEmpty()) {
            toSend = this.messageToSend.deleteMin();
            uploadMessage(toSend);
        }
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
    public void downloadMessages() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
