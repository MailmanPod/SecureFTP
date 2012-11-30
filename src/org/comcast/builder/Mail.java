/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.builder;

import java.util.Properties;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author Quality of Service
 */
public class Mail {

    private Properties properties;
    private String from;
    private String recipient;
    private String subject;
    private String mailText;
    private String sendProtocol;
    private String userName;
    private String userPassword;
    
    private Session session;
    private MimeMessage message;

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * @param properties the properties to set
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * @return the from
     */
    public String getFrom() {
        return from;
    }

    /**
     * @param from the from to set
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * @return the recipient
     */
    public String getRecipient() {
        return recipient;
    }

    /**
     * @param recipient the recipient to set
     */
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the mailText
     */
    public String getMailText() {
        return mailText;
    }

    /**
     * @param mailText the mailText to set
     */
    public void setMailText(String mailText) {
        this.mailText = mailText;
    }

    /**
     * @return the sendProtocol
     */
    public String getSendProtocol() {
        return sendProtocol;
    }

    /**
     * @param sendProtocol the sendProtocol to set
     */
    public void setSendProtocol(String sendProtocol) {
        this.sendProtocol = sendProtocol;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the userPassword
     */
    public String getUserPassword() {
        return userPassword;
    }

    /**
     * @param userPassword the userPassword to set
     */
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String toString() {
        return "Mail{" + "properties=" + properties + ", session=" + session
                + ", from=" + from + ", recipients=" + recipient + ", subject=" + subject + ", mailText="
                + mailText + ", sendProtocol=" + sendProtocol + ", userName=" + userName + ", userPassword=" + userPassword + '}';
    }

    public void initSession() {
        this.session = Session.getDefaultInstance(properties);
    }

    public void createMail() throws MessagingException {
        message = new MimeMessage(session);
        message.setFrom(new InternetAddress(this.from));
        message.addRecipient(
                Message.RecipientType.TO,
                new InternetAddress(this.recipient));
        message.setSubject("System Configuration");
        message.setText(this.mailText);
    }

    public void sendMail() throws NoSuchProviderException, MessagingException {
        Transport t = session.getTransport(this.sendProtocol);
        t.connect(this.userName, this.userPassword);
        t.sendMessage(message, message.getAllRecipients());
    }
}
