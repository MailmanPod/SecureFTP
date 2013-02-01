package org.comcast.builder;

import java.util.Properties;
import java.util.ResourceBundle;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.logic.Validator;
import org.comcast.xml.LoaderProvider;

/**
 * Clase que se encarga de construir un objeto Mail. <br>
 * Utiliza el patron Builder para crearlo.<br>
 * Ademas realiza chequeos / controles para segurar que las reglas del negocio se cumplan.
 * @author Damian Bruera
 * @version 1.1
 * @since 2012.
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
    private ResourceBundle mailBuilder_es_ES;
    private Mail mail;
    private int requiredElements;

    /**
     * Constructor de la clase.<br>
     * Crea un objeto mail vacio.
     */
    public MailBuilder() {
        this.mail = new Mail();
    }

    /**
     * Metodo que se encarga de colocar las propiedades que se utilizan para el envio del mail.<br>
     * Controla si las propiedades son nulas o si estan bien seteadas en le objeto Properties.
     * @param props Con las propiedades del mail.
     */
    public void buildProperties(Properties props) {

        if (props != null && props.containsKey("mail.smtp.host")) {
            this.mail.setProperties(props);
        }
    }

    /**
     * Se encarga de setear el emisor del mensaje.<br>
     * Controla si es nulo y si es valido.
     * @param from Con el emisor.
     */
    public void buildFrom(String from) {

        if (from != null && Validator.isMail(from)) {
            this.mail.setFrom(from);
        }
    }

    /**
     * Se encarga de setear el destinatario del mail.<br>
     * Controla si es nulo y si es un destinatario valido.
     * @param recipient Con el destinatario.
     */
    public void buildRecipient(String recipient) {

        if (recipient != null && Validator.isMail(recipient)) {
            this.mail.setRecipient(recipient);
        }
    }

    /**
     * Se encarga de setear el asunto del mail.<br>
     * Controla si es nulo y si es valido.
     * @param subject Con el asunto del mail.
     */
    public void buildSubject(String subject) {

        if (subject != null && Validator.isMailSubject(subject)) {
            this.mail.setSubject(subject);
        }
    }

    /**
     * Se encarga de introducir el texto que tendra el mail de aviso.<br>
     * Valida si es nulo y si es un texto valido.
     * @param mailText Con el texto del mail.
     */
    public void buildMailText(String mailText) {

        if (mailText != null && !Validator.isTextEmpty(mailText)) {
            this.mail.setMailText(mailText);
        }
    }

    /**
     * Se encarga de setear el protocolo que se utilizará para el envio del mail.<br>
     * La validacion es por nulo o por si es un texto valido.
     * @param protocol Con el protocolo a utilizar.
     */
    public void buildSendProtocol(String protocol) {

        if (protocol != null && Validator.isText(protocol)) {
            this.mail.setSendProtocol(protocol);
        }
    }

    /**
     * Se encarga de setear el nombre de usuario del mail.<br>
     * El nombre de usuario hace referencia al mail del propietario del programa.
     * @param mailUserName Con el mail(nombre de usuario) del propietario.
     */
    public void buildMailUserName(String mailUserName) {

        if (mailUserName != null && Validator.isMail(mailUserName)) {
            this.mail.setMailUserName(mailUserName);
        }
    }

    /**
     * Se encarga de setear el password asociado al nombre de usuario.<br>
     * La contraseña debe respetar ciertas condiciones. Para mas informacion consulte la ayuda del programa.
     * @param mailUserPassword Con la contrasenia.
     */
    public void buildMailUserPassword(String mailUserPassword) {

        if (mailUserPassword != null && Validator.isPassword(mailUserPassword)) {
            this.mail.setMailUserPassword(mailUserPassword);
        }
    }

    /**
     * Este metodo se encarga de validar que todos los datos esten correctos, chequeando que la bandera sea siempre igual a cero.<br>
     * Luego, contruye el objeto Mail. En caso contrario lanza una excepcion acusando de que algunos datos son incorrectos.<br>
     * Devuelve el objeto Mail creado.
     * @return Mail Con el objeto Mail creado.
     * @throws InformationRequiredException Si los datos que es utilizaron para la construccion del objeto son erroneos.
     * @throws Exception Por cualquier otro tipo de problemas.
     */
    public Mail getMail() throws InformationRequiredException, Exception {
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
            
            Client c = LoaderProvider.getInstance().getClientConfiguration();
            
            switch(c.getLocalization()){
                case "Español":
                    this.mailBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/MailBuilder_es_ES");
                    break;
                case "Ingles":
                    this.mailBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/MailBuilder_en_US");
                    break;
                default:
                    this.mailBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/MailBuilder_en_US");
                    break;
            }
            throw new InformationRequiredException(mailBuilder_es_ES.getString("FALTAN DATOS REQUERIDOS PARA ENVIAR EL MAIL"));
        }

        return this.mail;
    }

    /**
     * Este metodo se utiliza para realizar un control sobre los datos que se utilizan para el armado del objeto mail.
     * @deprecated Solo es un control.
     * @return int Si es cero todos los datos estan bien, si es distinto de cero, alguno de los datos no son correctos.
     */
    public int getRequiredElements() {
        return this.requiredElements;
    }
}
