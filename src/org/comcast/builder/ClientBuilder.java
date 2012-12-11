/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.builder;

import java.io.File;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.logic.Validator;

/**
 *
 * @author Quality of Service
 */
public class ClientBuilder {

    public static final int CLIENT_NAME = 1;
    public static final int CLIENT_LAST_NAME = 2;
    public static final int ORGANIZATION = 4;
    public static final int LOCALE = 8;
    public static final int DOWNLOAD_PATH = 16;
    public static final int PUBLIC_STORAGE = 32;
    public static final int PRIVATE_STORAGE = 64;
    private Client client;
    private int requiredElements;

    public ClientBuilder() {
        this.client = new Client();
    }

    public void buildClientName(String clientName) {

        if (clientName != null && !Validator.isTextEmpty(clientName)) {
            client.setClientName(clientName);
        }
    }

    public void buildClientLastName(String clientLastName) {

        if (clientLastName != null && !Validator.isTextEmpty(clientLastName)) {
            client.setClientLastName(clientLastName);
        }
    }

    public void buildOrganization(String organization) {

        if (organization != null && !Validator.isTextEmpty(organization)) {
            client.setOrganization(organization);
        }
    }

    public void buildLocale(String locale) {

        client.setLocalization(locale);
    }

    public void buildDownloadPath(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setDownloadPath(path);
        }
    }

    public void buildPublicStorage(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setPublicStorage(path);
        }
    }

    public void buildPrivateStorage(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setPrivateStorage(path);
        }
    }

    public void buildSetupRun(boolean setup) {

        client.setSetupRun(setup);
    }

    public Client getCLient() throws InformationRequiredException {
        this.requiredElements = 0;

        if (client.getClientName() == null) {
            this.requiredElements += CLIENT_NAME;
        }

        if (client.getClientLastName() == null) {
            this.requiredElements += CLIENT_LAST_NAME;
        }

        if (client.getOrganization() == null) {
            this.requiredElements += ORGANIZATION;
        }

        if (client.getLocalization() == null) {
            this.requiredElements += LOCALE;
        }

        if (client.getDownloadPath() == null) {
            this.requiredElements += DOWNLOAD_PATH;
        }

        if (client.getPublicStorage() == null) {
            this.requiredElements += PUBLIC_STORAGE;
        }

        if (client.getPrivateStorage() == null) {
            this.requiredElements += PRIVATE_STORAGE;
        }

        if (this.requiredElements > 0) {
            throw new InformationRequiredException("Faltan datos requeridos para construir el Cliente");
        }

        return this.client;
    }
}
