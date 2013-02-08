package org.comcast.xml;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import org.comcast.builder.Client;
import org.comcast.builder.ClientBuilder;
import org.comcast.builder.Mail;
import org.comcast.builder.MailBuilder;
import org.comcast.crypto.CryptoData;
import org.comcast.exceptions.EmptyHashTableException;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.exceptions.InvalidEntryException;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.OpenAdressingHashTable;
import org.xml.sax.SAXException;

/**
 * Clase que brinda soporte para la carga, modificacion y eliminacion de
 * informacion almacenada en archivos xml.<br> Forma parte de la implementacion
 * del patron Singleton.
 *
 * @author Damian Bruera
 * @version 2.0
 * @since Java 7
 */
public class Loader {

    private XMLConfiguration general;

    /**
     * Constructor de la clase.
     */
    public Loader() {
        general = new XMLConfiguration();
    }

    /**
     * Obtiene la configuracion necesaria para establecer un vinculo con el
     * servidor ftp.
     *
     * @return Configuracion.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public ServerConfig getServerConfiguration() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.SERVER_CONFIG_SCHEMA, XMLConfiguration.SERVER_CONFIG);
        ServerConfig config = general.getServerConfig();
        general.closeConection(XMLConfiguration.SERVER_CONFIG);

        return config;
    }

    /**
     * Obtiene la configuracion del cliente de la aplicacion.
     *
     * @return Configuracion.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     * @throws InformationRequiredException
     */
    public Client getClientConfiguration() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException, InformationRequiredException {

        general.createConection(XMLConfiguration.CLIENT_CONFIG_SCHEMA, XMLConfiguration.CLIENT_CONFIG);
        Properties config = general.getClientConfig();
        ClientBuilder builder = new ClientBuilder();

        builder.buildClientName(config.getProperty("comcast.name"));
        builder.buildClientLastName(config.getProperty("comcast.last"));
        builder.buildOrganization(config.getProperty("comcast.org"));
        builder.buildLocale(config.getProperty("comcast.locale"));
        builder.buildDownloadPath(config.getProperty("comcast.download"));
        builder.buildPublicStorage(config.getProperty("comcast.public"));
        builder.buildPrivateStorage(config.getProperty("comcast.private"));
        boolean aux = (config.getProperty("comcast.setup").compareToIgnoreCase("true") == 0) ? true : false;
        builder.buildSetupRun(aux);

        Client client = builder.getCLient();

        general.closeConection(XMLConfiguration.CLIENT_CONFIG);

        return client;
    }

    /**
     * Obtiene la configuracion necesaria para establecer un vinculo entre las
     * maquina local y el servidor smtp y asi poder enviar el mail de aviso de
     * conclusion (o no) de una tarea programada.
     *
     * @return Configuracion completa.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     * @throws InformationRequiredException
     * @throws Exception
     */
    public Mail getMail() throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException, InformationRequiredException, Exception {

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

    /**
     * Permite verificar si un archivo, mediante su nombre, ya fue subido al
     * servidor ftp.<br> Dicha verificacion es llevada a cabo por una tabla de
     * hash, cuya clave de busqueda es el mismo nombre del archivo.
     *
     * @param destination Nombre del archivo que se quiere verificar.
     * @return null si el archivo no se subio, o el objeto si ya se subio.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     * @throws EmptyHashTableException
     * @throws InvalidEntryException
     */
    public CryptoData getCryptoData(String destination) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException, EmptyHashTableException, InvalidEntryException {

        general.createConection(XMLConfiguration.CRYPTO_SCHEMA, XMLConfiguration.CRYPTO);
        OpenAdressingHashTable<CryptoData, String> map = general.getCryptoData();
        general.closeConection(XMLConfiguration.CRYPTO);

//        System.out.println(map.toString());

        CryptoData cd = null;

        if (map.containsKey(destination)) {
            cd = map.get(destination);
        }
        return cd;
    }

    /**
     * Añade el archivo subido al servidor ftp.
     *
     * @param newData Datos del archivo.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     * @throws EmptyHashTableException
     */
    public void appendCryptoData(CryptoData newData) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException, EmptyHashTableException {

        general.createConection(XMLConfiguration.CRYPTO);
        general.appendCryptoData(newData);
        general.closeConection(XMLConfiguration.CRYPTO);
    }

    /**
     * Elimina el archivo, cuando este ya fue subido.
     *
     * @param fileName Archivo a eliminar.
     * @param extension Extension del archivo.
     * @deprecated Sin uso. Reservado para futuras implementaciones.<br> Usar
     * con cuidado.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public void removeCryptoData(String fileName, String extension) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.CRYPTO);
        general.removeCryptoData(fileName, extension);
        general.closeConection(XMLConfiguration.CRYPTO);
    }

    /**
     * Modifica los parametros de coneccion al servidor ftp.
     *
     * @param config Nueva configuracion.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public void setServerConfiguration(ServerConfig config) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.SERVER_CONFIG_SCHEMA, XMLConfiguration.SERVER_CONFIG);
        general.setServerConfig(config);
        general.closeConection(XMLConfiguration.SERVER_CONFIG);
    }

    /**
     * Modifica la informacion del usuario de la aplicacion.
     *
     * @param config Neuva informacion.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public void setClientConfiguration(Client config) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.CLIENT_CONFIG_SCHEMA, XMLConfiguration.CLIENT_CONFIG);
        general.setClientConfig(config);
        general.closeConection(XMLConfiguration.CLIENT_CONFIG);
    }

    /**
     * Modifica solamente el nombre del usuario del servicio smtp.
     *
     * @param mailUser Nuevo nombre de usuario.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public void setMailProperties(String mailUser) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.MAIL_PROPERTIES_SCHEMA, XMLConfiguration.MAIL_PROPERTIES);
        general.setMailProperties(mailUser);
        general.closeConection(XMLConfiguration.MAIL_PROPERTIES);
    }

    /**
     * MOdifica los parametros necesarios para construir el mail de aviso.
     *
     * @param content Con el nuevo contenido del mail.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws TransformerConfigurationException
     * @throws TransformerException
     * @throws URISyntaxException
     */
    public void setMailContent(Properties content) throws ParserConfigurationException, SAXException, IOException,
            TransformerConfigurationException, TransformerException, URISyntaxException {

        general.createConection(XMLConfiguration.MAIL_CONTENT_SCHEMA, XMLConfiguration.MAIL_CONTENT);
        general.setMailContent(content);
        general.closeConection(XMLConfiguration.MAIL_CONTENT);
    }
}
