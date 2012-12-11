/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.builder;

import java.util.Properties;
import javax.mail.Session;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.logic.Validator;

/**
 *
 * @author Quality of Service
 */
public class MailBuilder {

    public static final int PROPERTIES = 1;
    public static final int FROM = 2;
    public static final int RECIPIENTS = 4;
    public static final int SUBJECT = 8;
    public static final int MAIL_TEXT = 16;
    public static final int SEND_PROTOCOL = 32;
    public static final int USER_NAME = 64;
    public static final int USER_PASSWORD = 128;
    private Mail mail;
    private int requiredElements;

    public MailBuilder() {
        this.mail = new Mail();
    }

    public void buildProperties(Properties props) {

        if (props != null && props.containsKey("mail.smtp.host")) {
            this.mail.setProperties(props);
        }
    }

    public void buildFrom(String from) {

        if (from != null && Validator.isMail(from)) {
            this.mail.setFrom(from);
        }
    }

    public void buildRecipient(String recipient) {

        if (recipient != null && Validator.isMail(recipient)) {
            this.mail.setRecipient(recipient);
        }
    }

    public void buildSubject(String subject) {

        if (subject != null && Validator.isMailSubject(subject)) {
            this.mail.setSubject(subject);
        }
    }

    public void buildMailText(String mailText) {

        if (mailText != null && !Validator.isTextEmpty(mailText)) {
            this.mail.setMailText(mailText);
        }
    }

    public void buildSendProtocol(String protocol) {

        if (protocol != null && Validator.isText(protocol)) {
            this.mail.setSendProtocol(protocol);
        }
    }

    public void buildMailUserName(String mailUserName) {

        if (mailUserName != null && Validator.isMail(mailUserName)) {
            this.mail.setMailUserName(mailUserName);
        }
    }

    public void buildMailUserPassword(String mailUserPassword) {

        if (mailUserPassword != null && Validator.isPassword(mailUserPassword)) {
            this.mail.setMailUserPassword(mailUserPassword);
        }
    }

    public Mail getMail() throws InformationRequiredException {
        this.requiredElements = 0;

        if (mail.getProperties() == null) {
            this.requiredElements += PROPERTIES;
        }

        if (mail.getFrom() == null) {
            this.requiredElements += FROM;
        }

        if (mail.getRecipient() == null) {
            this.requiredElements += RECIPIENTS;
        }

        if (mail.getSubject() == null) {
            this.requiredElements += SUBJECT;
        }

        if (mail.getMailText() == null) {
            this.requiredElements += MAIL_TEXT;
        }

        if (mail.getSendProtocol() == null) {
            this.requiredElements += SEND_PROTOCOL;
        }

        if (mail.getMailUserName() == null) {
            this.requiredElements += USER_NAME;
        }

        if (mail.getMailUserPassword() == null) {
            this.requiredElements += USER_PASSWORD;
        }

        if (this.requiredElements > 0) {
            throw new InformationRequiredException("Faltan datos requeridos para enviar el mail");
        }

        return this.mail;
    }

    public int getRequiredElements() {
        return this.requiredElements;
    }
}
