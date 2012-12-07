/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.logic;

import org.comcast.exceptions.NullObjectParameterException;
import org.comcast.router.InputChannel;

/**
 *
 * @author Quality of Service
 */
public class Client implements Comparable<Client>, InputChannel {

    private String clientName;
    private String clientLastName;
    private String organization;
    private boolean setupRun;
    private String localization;
    private String downloadPath;

    public Client() {
    }
    
    public Client(String clientName, String clientLastName, String organization, String localization, String downloadPath, boolean setupRun) {
        this.clientName = clientName;
        this.clientLastName = clientLastName;
        this.organization = organization;
        this.localization = localization;
        this.downloadPath = downloadPath;
        this.setupRun = setupRun;
    }

    @Override
    public int compareTo(Client o) {
        if(o == null){
            throw new NullObjectParameterException("El objeto Cliente no tiene ningun valor asociado");
        }
        
        return this.getClientLastName().compareTo(o.getClientLastName());
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the clientLastName
     */
    public String getClientLastName() {
        return clientLastName;
    }

    /**
     * @param clientLastName the clientLastName to set
     */
    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    /**
     * @return the organization
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * @param organization the organization to set
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * @return the downloadPath
     */
    public String getDownloadPath() {
        return downloadPath;
    }

    /**
     * @param downloadPath the downloadPath to set
     */
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    @Override
    public String toString() {
        return "Client{" + "clientName=" + clientName + ", clientLastName=" + clientLastName + 
                ", organization=" + organization + ", setupRun=" + setupRun + ", localization=" + localization + ", downloadPath=" + downloadPath + '}';
    }

    /**
     * @return the setupRun
     */
    public boolean isSetupRun() {
        return setupRun;
    }

    /**
     * @param setupRun the setupRun to set
     */
    public void setSetupRun(boolean setupRun) {
        this.setupRun = setupRun;
    }

    /**
     * @return the localization
     */
    public String getLocalization() {
        return localization;
    }

    /**
     * @param localization the localization to set
     */
    public void setLocalization(String localization) {
        this.localization = localization;
    }
}
