/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.basic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Quality of Service
 */
public class ServerConfig {
    private String ipAddress;
    private String hostName;
    
    private String userLogin;
    private String passLogin;

    public ServerConfig(String hostName) {
        this.hostName = hostName;
        this.ipAddress = getIPAddress();
    }

    private String getIPAddress(){
        try {
            InetAddress localhost = InetAddress.getByName(hostName);
            return localhost.getHostAddress();
        } catch (UnknownHostException ex) {
            return "localhost";
        }
    }
    
    @Override
    public String toString() {
        return "ServerConfig{" + "ipAddress=" + getIpAddress() + ", hostName=" + getHostName() + ", userLogin=" + getUserLogin() + '}';
    }

    /**
     * @return the ipAddress
     */
    public String getIpAddress() {
        return ipAddress;
    }

    /**
     * @param ipAddress the ipAddress to set
     */
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    /**
     * @return the hostName
     */
    public String getHostName() {
        return hostName;
    }

    /**
     * @param hostName the hostName to set
     */
    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    /**
     * @return the userLogin
     */
    public String getUserLogin() {
        return userLogin;
    }

    /**
     * @param userLogin the userLogin to set
     */
    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
    }

    /**
     * @return the passLogin
     */
    public String getPassLogin() {
        return passLogin;
    }

    /**
     * @param passLogin the passLogin to set
     */
    public void setPassLogin(String passLogin) {
        this.passLogin = passLogin;
    }
    
    
}
