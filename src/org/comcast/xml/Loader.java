/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Mail;
import org.comcast.builder.MailBuilder;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.logic.Client;
import org.comcast.logic.ServerConfig;
import org.xml.sax.SAXException;

/**
 *
 * @author Quality of Service
 */
public class Loader {

    private XMLConfiguration general;

    public Loader() {
        general = new XMLConfiguration();
    }

    public ServerConfig getServerConfiguration() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.SERVER_CONFIG_SCHEMA, XMLConfiguration.SERVER_CONFIG);
        ServerConfig config = general.getServerConfig();
        general.closeConection(XMLConfiguration.SERVER_CONFIG);

        return config;
    }

    public Client getClientConfiguration() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.CLIENT_CONFIG_SCHEMA, XMLConfiguration.CLIENT_CONFIG);
        Client config = general.getClientConfig();
        general.closeConection(XMLConfiguration.CLIENT_CONFIG);

        return config;
    }

    public Mail getMail() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException, InformationRequiredException {

        general.createConection(XMLConfiguration.MAIL_PROPERTIES_SCHEMA, XMLConfiguration.MAIL_PROPERTIES);
        Properties properties = general.getMailProperties();
        general.closeConection(XMLConfiguration.MAIL_PROPERTIES);

        general.createConection(XMLConfiguration.MAIL_CONTENT_SCHEMA, XMLConfiguration.MAIL_CONTENT);
        Properties content = general.getMailContent();
        general.closeConection(XMLConfiguration.MAIL_CONTENT);

        MailBuilder builder = new MailBuilder();
        builder.buildProperties(properties);
        builder.buildFrom(content.getProperty("comcast.from"));
        builder.buildRecipient(content.getProperty("comcast.recipient"));
        builder.buildSubject(content.getProperty("comcast.subject"));
        builder.buildSendProtocol(content.getProperty("comcast.protocol"));
        builder.buildMailText(content.getProperty("comcast.text"));
        builder.buildMailUserName(content.getProperty("comcast.user"));
        builder.buildMailUserPassword(content.getProperty("comcast.password"));

        return builder.getMail();
    }

    public void setServerConfiguration(ServerConfig config) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.SERVER_CONFIG_SCHEMA, XMLConfiguration.SERVER_CONFIG);
        general.setServerConfig(config);
        general.closeConection(XMLConfiguration.SERVER_CONFIG);
    }

    public void setClientConfiguration(Client config) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.CLIENT_CONFIG_SCHEMA, XMLConfiguration.CLIENT_CONFIG);
        general.setClientConfig(config);
        general.closeConection(XMLConfiguration.CLIENT_CONFIG);
    }

    public void setMailProperties(String mailUser) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.MAIL_PROPERTIES_SCHEMA, XMLConfiguration.MAIL_PROPERTIES);
        general.setMailProperties(mailUser);
        general.closeConection(XMLConfiguration.MAIL_PROPERTIES);
    }

    public void setMailContent(Properties content) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.MAIL_CONTENT_SCHEMA, XMLConfiguration.MAIL_CONTENT);
        general.setMailContent(content);
        general.closeConection(XMLConfiguration.MAIL_CONTENT);
    }
}
