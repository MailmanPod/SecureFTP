/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.builder;

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
    private String publicStorage;
    private String privateStorage;

    public Client() {
    }

    @Override
    public int compareTo(Client o) {
        if (o == null) {
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
        return "Client{" + "clientName=" + clientName + ", clientLastName=" + clientLastName + ", organization=" + organization
                + ", setupRun=" + setupRun + ", localization=" + localization + ", downloadPath=" + downloadPath
                + ", publicStorage=" + publicStorage + ", privateStorage=" + privateStorage + '}';
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

    /**
     * @return the publicStorage
     */
    public String getPublicStorage() {
        return publicStorage;
    }

    /**
     * @param publicStorage the publicStorage to set
     */
    public void setPublicStorage(String publicStorage) {
        this.publicStorage = publicStorage;
    }

    /**
     * @return the privateStorage
     */
    public String getPrivateStorage() {
        return privateStorage;
    }

    /**
     * @param privateStorage the privateStorage to set
     */
    public void setPrivateStorage(String privateStorage) {
        this.privateStorage = privateStorage;
    }
}
