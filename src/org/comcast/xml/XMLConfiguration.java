package org.comcast.xml;

import com.sun.org.apache.xml.internal.utils.DefaultErrorHandler;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Properties;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.comcast.builder.Client;
import org.comcast.crypto.CryptoData;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.OpenAdressingHashTable;
import org.comcast.structures.SimpleList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

/**
 * Clase que maneja la conexion y movimiento de datos u un archivo XML.<br> Este
 * archivo XML de configuracion que se crea, almacena los datos de un usuario,
 * Nombre y contrasenia, utilizando el sistema DOM, el cual crea en memoria,
 * luego de realizar el parseo, un esquema de arbol n-ario.
 *
 * @author Federico Bruera TSB 2010.
 * @version 2.0
 * @since 1.6
 */
public final class XMLConfiguration {

    /**
     * Atributo que contiene el archivo XML en memoria, luego del parseo.
     */
    private static Document root;
    /**
     * Atributo que representa el nodo raiz del arbol.
     */
    private static Element userElement;
    /**
     *
     */
    private static final String JAXP_SCHEMA_LANGUAGE = "http://java.sun.com/xml/jaxp/properties/schemaLanguage";
    private static final String JAXP_SCHEMA_SOURCE = "http://java.sun.com/xml/jaxp/properties/schemaSource";
    private static final String W3C_XML_SCHEMA = "http://www.w3.org/2001/XMLSchema";
    /**
     *
     */
    public static final String SERVER_CONFIG = "./xml/serverconfig.xml";
    public static final String SERVER_CONFIG_SCHEMA = "./xml/serverConfigValid.xsd";
    /**
     *
     */
    public static final String CLIENT_CONFIG = "./xml/clientconfig.xml";
    public static final String CLIENT_CONFIG_SCHEMA = "./xml/clientConfigValid.xsd";
    /**
     *
     */
    public static final String MAIL_PROPERTIES = "./xml/mailproperties.xml";
    public static final String MAIL_PROPERTIES_SCHEMA = "./xml/mailpropertiesValid.xsd";
    /**
     *
     */
    public static final String MAIL_CONTENT = "./xml/mailcontent.xml";
    public static final String MAIL_CONTENT_SCHEMA = "./xml/mailContentValid.xsd";
    /**
     *
     */
    public static final String CRYPTO = "./xml/cryptodata.xml";
    public static final String CRYPTO_SCHEMA = "./xml/cryptoDataValid.xsd";

    /**
     * Constructor sin parametros, por default.
     */
    public XMLConfiguration() {
    }

    /**
     * Metodo que tiene como finalidad establecer la conexion con el archivo
     * XML. <br> En este metodo se realiza el parseo del archivo de
     * configuracion XML, para luego crear el arbol n-ario representado por
     * <code>userElement</code>. <br> Principalmente el archivo XML contendra
     * los nombres de usuario y sus respectivas contrasenias.
     *
     * @throws ParserConfigurationException si no se puede llevar u cabo el
     * parseo
     * @throws SAXException si ocurre algun problema durante la operacion.
     * @throws IOException si ocurre algun problema durante la operacion.
     */
    public void createConection(String schema, String xmlFile) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        File fileSchema = new File(schema);
        File fileXML = new File(xmlFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(true);
        factory.setAttribute(JAXP_SCHEMA_LANGUAGE, W3C_XML_SCHEMA);
        factory.setAttribute(JAXP_SCHEMA_SOURCE, fileSchema);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder constructor = factory.newDocumentBuilder();/*constructor es el parser*/
        constructor.setErrorHandler(new DefaultErrorHandler());
//        File configFile = new File(xmlFile);
        root = constructor.parse(fileXML);

        userElement = root.getDocumentElement();/*apunta u la raiz del arbol*/
    }

    public void createConection(String xmlFile) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        File fileXML = new File(xmlFile);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setValidating(false);
        factory.setIgnoringElementContentWhitespace(true);

        DocumentBuilder constructor = factory.newDocumentBuilder();/*constructor es el parser*/
        constructor.setErrorHandler(new DefaultErrorHandler());
//        File configFile = new File(xmlFile);
        root = constructor.parse(fileXML);

        userElement = root.getDocumentElement();/*apunta u la raiz del arbol*/
    }

    /**
     * Metodo que tiene como finalidad cerrar la conexion con el archivo de
     * configuracion. <br> Este metodo se utiliza si ocurrio alguna modificacion
     * en el arbol de memoria y se quiere que salga reflejada en el mismo
     * archivo. Estas modificaciones pueden ser tanto dde insercion,
     * modificacion, o remocion de algun elemento del archivo XML.
     *
     * @throws TransformerConfigurationException si ocurre algun problema
     * durante la operacion.
     * @throws TransformerException si ocurre algun problema durante la
     * operacion.
     */
    public void closeConection(String xmlFile) throws TransformerConfigurationException, TransformerException, IOException, URISyntaxException {
        root.setXmlStandalone(false);

        File fileXML = new File(xmlFile);

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource source = new DOMSource(root);
        StreamResult result = new StreamResult(fileXML);
        transformer.transform(source, result);
    }

    public ServerConfig getServerConfig() throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        ServerConfig config = null;

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList serverN = user.getElementsByTagName("serverName");
            Text serverName = (Text) serverN.item(0).getFirstChild();

            NodeList serverU = user.getElementsByTagName("serverUser");
            Text serverUser = (Text) serverU.item(0).getFirstChild();

            NodeList serverP = user.getElementsByTagName("serverPassword");
            Text serverPassword = (Text) serverP.item(0).getFirstChild();

            config = new ServerConfig(serverName.getData());
            config.setUserLogin(serverUser.getData());
            config.setPassLogin(serverPassword.getData());
        }
        //closeConection();
        return config;
    }

    public void setServerConfig(ServerConfig newServerConfig) throws ParserConfigurationException,
            SAXException, IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList serverN = user.getElementsByTagName("serverName");
            Text serverName = (Text) serverN.item(0).getFirstChild();

            NodeList serverU = user.getElementsByTagName("serverUser");
            Text serverUser = (Text) serverU.item(0).getFirstChild();

            NodeList serverP = user.getElementsByTagName("serverPassword");
            Text serverPassword = (Text) serverP.item(0).getFirstChild();

            serverName.setData(newServerConfig.getHostName());
            serverUser.setData(newServerConfig.getUserLogin());
            serverPassword.setData(newServerConfig.getPassLogin());
        }
        //closeConection();
    }

    public Properties getClientConfig() throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        Properties config = new Properties();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList cn = user.getElementsByTagName("clientName");
            Text clientName = (Text) cn.item(0).getFirstChild();
            String att_name = cn.item(0).getAttributes().item(0).getNodeValue();;

            NodeList cln = user.getElementsByTagName("clientLastName");
            Text clientLastName = (Text) cln.item(0).getFirstChild();
            String att_last = cln.item(0).getAttributes().item(0).getNodeValue();

            NodeList co = user.getElementsByTagName("clientOrganization");
            Text clientOrganization = (Text) co.item(0).getFirstChild();
            String att_org = co.item(0).getAttributes().item(0).getNodeValue();

            NodeList l = user.getElementsByTagName("localization");
            Text localization = (Text) l.item(0).getFirstChild();
            String att_locale = l.item(0).getAttributes().item(0).getNodeValue();

            NodeList dp = user.getElementsByTagName("downloadPath");
            Text downloadPath = (Text) dp.item(0).getFirstChild();
            String att_download = dp.item(0).getAttributes().item(0).getNodeValue();

            NodeList pu = user.getElementsByTagName("publicStorage");
            Text publicStorage = (Text) pu.item(0).getFirstChild();
            String att_public = pu.item(0).getAttributes().item(0).getNodeValue();

            NodeList pv = user.getElementsByTagName("privateStorage");
            Text privateStorage = (Text) pv.item(0).getFirstChild();
            String att_private = pv.item(0).getAttributes().item(0).getNodeValue();

            NodeList isr = user.getElementsByTagName("isSetupRun");
            Text isSetupRun = (Text) isr.item(0).getFirstChild();
            String att_setup = isr.item(0).getAttributes().item(0).getNodeValue();
//            boolean aux = (isSetupRun.getData().compareToIgnoreCase("true") == 0) ? true : false;

            config.setProperty(att_name, clientName.getData());
            config.setProperty(att_last, clientLastName.getData());
            config.setProperty(att_org, clientOrganization.getData());
            config.setProperty(att_locale, localization.getData());
            config.setProperty(att_download, downloadPath.getData());
            config.setProperty(att_public, publicStorage.getData());
            config.setProperty(att_private, privateStorage.getData());
            config.setProperty(att_setup, isSetupRun.getData());

        }
        //closeConection();
        return config;
    }

    public void setClientConfig(Client config) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            System.out.println("Item: " + NodoUsers.item(i).getNodeName());

            Element user = (Element) NodoUsers.item(i);

            NodeList cn = user.getElementsByTagName("clientName");
            Text clientName = (Text) cn.item(0).getFirstChild();

            NodeList cln = user.getElementsByTagName("clientLastName");
            Text clientLastName = (Text) cln.item(0).getFirstChild();

            NodeList co = user.getElementsByTagName("clientOrganization");
            Text clientOrganization = (Text) co.item(0).getFirstChild();

            NodeList l = user.getElementsByTagName("localization");
            Text localization = (Text) l.item(0).getFirstChild();

            NodeList dp = user.getElementsByTagName("downloadPath");
            Text downloadPath = (Text) dp.item(0).getFirstChild();

            NodeList pu = user.getElementsByTagName("publicStorage");
            Text publicStorage = (Text) pu.item(0).getFirstChild();

            NodeList pv = user.getElementsByTagName("privateStorage");
            Text privateStorage = (Text) pv.item(0).getFirstChild();

            NodeList isr = user.getElementsByTagName("isSetupRun");
            Text isSetupRun = (Text) isr.item(0).getFirstChild();

            clientName.setData(config.getClientName());
            clientLastName.setData(config.getClientLastName());
            clientOrganization.setData(config.getOrganization());
            localization.setData(config.getLocalization());
            downloadPath.setData(config.getDownloadPath());
            publicStorage.setData(config.getPublicStorage());
            privateStorage.setData(config.getPrivateStorage());

            String aux = (config.isSetupRun()) ? "true" : "false";
            isSetupRun.setData(aux);
        }
        //closeConection();
    }

    public Properties getMailProperties() throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        Properties config = new Properties();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList h = user.getElementsByTagName("host");
            Text host = (Text) h.item(0).getFirstChild();
            String att_host = h.item(0).getAttributes().item(0).getNodeValue();

            NodeList t = user.getElementsByTagName("tls");
            Text tls = (Text) t.item(0).getFirstChild();
            String att_tls = t.item(0).getAttributes().item(0).getNodeValue();

            NodeList p = user.getElementsByTagName("port");
            Text port = (Text) p.item(0).getFirstChild();
            String att_port = p.item(0).getAttributes().item(0).getNodeValue();

            NodeList u = user.getElementsByTagName("user");
            Text userMail = (Text) u.item(0).getFirstChild();
            String att_userMail = u.item(0).getAttributes().item(0).getNodeValue();

            NodeList a = user.getElementsByTagName("authentication");
            Text authentication = (Text) a.item(0).getFirstChild();
            String att_authentication = a.item(0).getAttributes().item(0).getNodeValue();

            config.setProperty(att_host, host.getData());
            config.setProperty(att_tls, tls.getData());
            config.setProperty(att_port, port.getData());
            config.setProperty(att_userMail, userMail.getData());
            config.setProperty(att_authentication, authentication.getData());
        }
        //closeConection();
        return config;
    }

    public void setMailProperties(String mailUser) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            System.out.println("Item: " + NodoUsers.item(i).getNodeName());

            Element user = (Element) NodoUsers.item(i);

            NodeList u = user.getElementsByTagName("user");
            Text userMail = (Text) u.item(0).getFirstChild();

            userMail.setData(mailUser);
        }
        //closeConection();
    }

    public Properties getMailContent() throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        Properties config = new Properties();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList f = user.getElementsByTagName("from");
            Text from = (Text) f.item(0).getFirstChild();
            String att_from = f.item(0).getAttributes().item(0).getNodeValue();

            NodeList r = user.getElementsByTagName("recipient");
            Text recipient = (Text) r.item(0).getFirstChild();
            String att_recipient = r.item(0).getAttributes().item(0).getNodeValue();

            NodeList s = user.getElementsByTagName("subject");
            Text subject = (Text) s.item(0).getFirstChild();
            String att_subject = s.item(0).getAttributes().item(0).getNodeValue();

            NodeList p = user.getElementsByTagName("protocol");
            Text protocol = (Text) p.item(0).getFirstChild();
            String att_protocol = p.item(0).getAttributes().item(0).getNodeValue();

            NodeList t = user.getElementsByTagName("text");
            Text text = (Text) t.item(0).getFirstChild();
            String att_text = t.item(0).getAttributes().item(0).getNodeValue();

            NodeList u = user.getElementsByTagName("user");
            Text userMail = (Text) u.item(0).getFirstChild();
            String att_userMail = u.item(0).getAttributes().item(0).getNodeValue();

            NodeList pw = user.getElementsByTagName("password");
            Text password = (Text) pw.item(0).getFirstChild();
            String att_password = pw.item(0).getAttributes().item(0).getNodeValue();

            config.setProperty(att_from, from.getData());
            config.setProperty(att_recipient, recipient.getData());
            config.setProperty(att_subject, subject.getData());
            config.setProperty(att_protocol, protocol.getData());
            config.setProperty(att_text, text.getData());
            config.setProperty(att_userMail, userMail.getData());
            config.setProperty(att_password, password.getData());
        }
        //closeConection();
        return config;
    }

    public void setMailContent(Properties c) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList f = user.getElementsByTagName("from");
            Text from = (Text) f.item(0).getFirstChild();
            String att_from = f.item(0).getAttributes().item(0).getNodeValue();

            NodeList r = user.getElementsByTagName("recipient");
            Text recipient = (Text) r.item(0).getFirstChild();
            String att_recipient = r.item(0).getAttributes().item(0).getNodeValue();

            NodeList s = user.getElementsByTagName("subject");
            Text subject = (Text) s.item(0).getFirstChild();
            String att_subject = s.item(0).getAttributes().item(0).getNodeValue();

            NodeList p = user.getElementsByTagName("protocol");
            Text protocol = (Text) p.item(0).getFirstChild();
            String att_protocol = p.item(0).getAttributes().item(0).getNodeValue();

            NodeList t = user.getElementsByTagName("text");
            Text text = (Text) t.item(0).getFirstChild();
            String att_text = t.item(0).getAttributes().item(0).getNodeValue();

            NodeList u = user.getElementsByTagName("user");
            Text userMail = (Text) u.item(0).getFirstChild();
            String att_userMail = u.item(0).getAttributes().item(0).getNodeValue();

            NodeList pw = user.getElementsByTagName("password");
            Text password = (Text) pw.item(0).getFirstChild();
            String att_password = pw.item(0).getAttributes().item(0).getNodeValue();

            from.setData(c.getProperty(att_from));
            recipient.setData(c.getProperty(att_recipient));
            subject.setData(c.getProperty(att_subject));
            protocol.setData(c.getProperty(att_protocol));
            text.setData(c.getProperty(att_text));
            userMail.setData(c.getProperty(att_userMail));
            password.setData(c.getProperty(att_password));
        }
        //closeConection();
    }

    public OpenAdressingHashTable<CryptoData, String> getCryptoData() throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        //createConection();
        CryptoData config = null;
        OpenAdressingHashTable<CryptoData, String> list = new OpenAdressingHashTable<CryptoData, String>();

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            String algo = NodoUsers.item(i).getNodeName();

            if (algo.equalsIgnoreCase("data")) {
                Element user = (Element) NodoUsers.item(i);

                NodeList h = user.getElementsByTagName("fileName");
                Text fileName = (Text) h.item(0).getFirstChild();

                NodeList t = user.getElementsByTagName("original");
                Text original = (Text) t.item(0).getFirstChild();

                NodeList p = user.getElementsByTagName("cryptoFile");
                Text cryptoFile = (Text) p.item(0).getFirstChild();

                NodeList u = user.getElementsByTagName("publicKey");
                Text publicKey = (Text) u.item(0).getFirstChild();

                NodeList a = user.getElementsByTagName("privateKey");
                Text privateKey = (Text) a.item(0).getFirstChild();

                NodeList e = user.getElementsByTagName("extension");
                Text extension = (Text) e.item(0).getFirstChild();

                NodeList d = user.getElementsByTagName("destination");
                Text destination = (Text) d.item(0).getFirstChild();

                config = new CryptoData(fileName.getData(), original.getData(), cryptoFile.getData(),
                        publicKey.getData(), privateKey.getData(), extension.getData(), destination.getData());

                list.putByKey(config, config.getDestination());
            }
        }
        //closeConection();
        return list;
    }

    public void appendCryptoData(CryptoData newData) {

        Element data = root.createElement("data");

        Element fileName = root.createElement("fileName");
        Element original = root.createElement("original");
        Element cryptoFile = root.createElement("cryptoFile");
        Element publicKey = root.createElement("publicKey");
        Element privateKey = root.createElement("privateKey");
        Element extension = root.createElement("extension");
        Element destination = root.createElement("destination");

        Text fn = root.createTextNode(newData.getFileName());
        Text o = root.createTextNode(newData.getOriginal());
        Text cf = root.createTextNode(newData.getCryptoFile());
        Text pu = root.createTextNode(newData.getPublicKey());
        Text pv = root.createTextNode(newData.getPrivateKey());
        Text e = root.createTextNode(newData.getOriginalExtension());
        Text d = root.createTextNode(newData.getDestination());

        fileName.appendChild(fn);
        original.appendChild(o);
        cryptoFile.appendChild(cf);
        publicKey.appendChild(pu);
        privateKey.appendChild(pv);
        extension.appendChild(e);
        destination.appendChild(d);

        data.appendChild(fileName);
        data.appendChild(original);
        data.appendChild(cryptoFile);
        data.appendChild(publicKey);
        data.appendChild(privateKey);
        data.appendChild(extension);
        data.appendChild(destination);

        userElement.appendChild(data);
    }

    public void removeCryptoData(String fileName, String extension) throws ParserConfigurationException, SAXException,
            IOException, TransformerConfigurationException, TransformerException, URISyntaxException {

        NodeList NodoUsers = userElement.getChildNodes();/*devuelve los hijos de la raiz (en este caso)*/

        for (int i = 0; i < NodoUsers.getLength(); i++) {

            Element user = (Element) NodoUsers.item(i);

            NodeList fn = user.getElementsByTagName("fileName");
            Text fname = (Text) fn.item(0).getFirstChild();

            NodeList e = user.getElementsByTagName("extension");
            Text ext = (Text) e.item(0).getFirstChild();

            if (fname.getData().contains(fileName) && ext.getData().contains(extension)) {
                userElement.removeChild(user);
                break;
            }
        }
    }
}
