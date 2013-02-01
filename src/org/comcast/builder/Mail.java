package org.comcast.builder;

import java.util.Properties;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Clase que representa un objeto Mail.<br> Este objeto se utilizara para avisar
 * al usuario el resultado de las operaciones programadas.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since 2012.
 */
public class Mail {

    private Properties properties;
    private String from;
    private String recipient;
    private String subject;
    private String mailText;
    private String sendProtocol;
    private String mailUserName;
    private String mailUserPassword;
    private Session session;
    private MimeMessage message;

    /**
     * Constructor vacio de la clase Mail.
     */
    public Mail() {
    }

    /**
     * Metodo get, retorna las propiedades del mail.
     *
     * @return Properties Con las propiedades.
     */
    public Properties getProperties() {
        return properties;
    }

    /**
     * Metodo set, modifica los parametros del mail.
     *
     * @param properties Con los nuevos parametros de envio.
     */
    public void setProperties(Properties properties) {
        this.properties = properties;
    }

    /**
     * Metodo get, retorna el emisor del mail.
     *
     * @return String Con el emisor del mensaje.
     */
    public String getFrom() {
        return from;
    }

    /**
     * Metodo set, modifica el emisor del mensaje.
     *
     * @param from Con el nuevo emisor.
     */
    public void setFrom(String from) {
        this.from = from;
    }

    /**
     * Metodo get, que devuelve el receptor del mail.
     *
     * @return String Con el receptor.
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
     * @return the mailUserName
     */
    public String getMailUserName() {
        return mailUserName;
    }

    /**
     * @param mailUserName the mailUserName to set
     */
    public void setMailUserName(String mailUserName) {
        this.mailUserName = mailUserName;
    }

    /**
     * @return the mailUserPassword
     */
    public String getMailUserPassword() {
        return mailUserPassword;
    }

    /**
     * @param mailUserPassword the mailUserPassword to set
     */
    public void setMailUserPassword(String mailUserPassword) {
        this.mailUserPassword = mailUserPassword;
    }

    @Override
    public String toString() {
        return "Mail{" + "properties=" + properties
                + ", from=" + from + ", recipients=" + recipient + ", subject=" + subject + ", mailText="
                + mailText + ", sendProtocol=" + sendProtocol + ", userName=" + mailUserName + ", userPassword=" + mailUserPassword + '}';
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


        BodyPart cuerpo = new MimeBodyPart();
        cuerpo.setText(this.mailText);

        MimeMultipart multiParte = new MimeMultipart();
        multiParte.addBodyPart(cuerpo);

        message.setSubject(this.subject);
        message.setContent(multiParte, "multipart/alternative");
    }

    public void sendMail() throws NoSuchProviderException, MessagingException {
        Transport t = session.getTransport(this.sendProtocol);
        t.connect(this.mailUserName, this.mailUserPassword);
        t.sendMessage(message, message.getAllRecipients());
    }

    public Properties getContent() {
        Properties p = new Properties();

        p.setProperty("comcast.from", from);
        p.setProperty("comcast.recipient", recipient);
        p.setProperty("comcast.subject", subject);
        p.setProperty("comcast.protocol", sendProtocol);
        p.setProperty("comcast.text", mailText);
        p.setProperty("comcast.user", mailUserName);
        p.setProperty("comcast.password", mailUserPassword);

        return p;
    }
}
