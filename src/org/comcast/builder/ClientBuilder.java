package org.comcast.builder;

import java.io.File;
import java.util.ResourceBundle;
import org.comcast.exceptions.InformationRequiredException;
import org.comcast.logic.Validator;

/**
 * Clase encargada de contruir un objeto Client.<br> Representa el uso de patron
 * Builder.<br> Por Cliente nos referimos a la persona que a adquirido este
 * producto.
 *
 * @author Damian Bruera
 * @version 1.1
 * @since Java 7
 */
public class ClientBuilder {

    public static final int CLIENT_NAME = 1;
    public static final int CLIENT_LAST_NAME = 2;
    public static final int ORGANIZATION = 4;
    public static final int LOCALE = 8;
    public static final int DOWNLOAD_PATH = 16;
    public static final int PUBLIC_STORAGE = 32;
    public static final int PRIVATE_STORAGE = 64;
    private ResourceBundle clientBuilder_es_ES;
    private Client client;
    private int requiredElements;

    /**
     * Contructor de la clase.<br> Crea un objeto Client vacio.
     */
    public ClientBuilder() {
        this.client = new Client();
    }

    /**
     * Metodo encargado de setear el nombre del cliente.
     *
     * @param clientName Con el nombre del cliente.
     */
    public void buildClientName(String clientName) {

        if (clientName != null && !Validator.isTextEmpty(clientName)) {
            client.setClientName(clientName);
        }
    }

    /**
     * Igual que el metodo buildClientName, con la diferencia que setea el
     * apellido.
     *
     * @param clientLastName Con el apellido del cliente.
     */
    public void buildClientLastName(String clientLastName) {

        if (clientLastName != null && !Validator.isTextEmpty(clientLastName)) {
            client.setClientLastName(clientLastName);
        }
    }

    /**
     * Metodo encargado de setear la organizacion a la que pertenece dicho
     * cliente.
     *
     * @param organization Con la organizacion.
     */
    public void buildOrganization(String organization) {

        if (organization != null && !Validator.isTextEmpty(organization)) {
            client.setOrganization(organization);
        }
    }

    /**
     * Este metodo se encarga de setear el idioma de la aplicacion.<br> Para
     * saber cuales son los idiomas disponibles consulte la ayuda del programa.
     *
     * @param locale Con el nuevo idioma.
     */
    public void buildLocale(String locale) {

        client.setLocalization(locale);
    }

    /**
     * Metodo que se encarga de setear la ruta de descarga de los archivos.<br>
     * Dicha ruta debe ser absoluta.
     *
     * @param path Con la ruta absoluta.
     */
    public void buildDownloadPath(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setDownloadPath(path);
        }
    }

    /**
     * Metodo que se encarga de setear la ruta de almacenamiento de las claves
     * publicas utilizadas para la encriptacion / desencriptacion.<br> Dicha
     * ruta debe ser absoluta.
     *
     * @param path
     */
    public void buildPublicStorage(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setPublicStorage(path);
        }
    }

    /**
     * Metodo que se encarga de setear la ruta de almacenamiento de las claves
     * privadas utilizadas para la encriptacion / desencriptacion.<br> Dicha
     * ruta debe ser absoluta.
     *
     * @param path
     */
    public void buildPrivateStorage(String path) {

        if (path != null && new File(path).isDirectory()) {
            client.setPrivateStorage(path);
        }
    }

    /**
     * Metodo que se utiliza solo con fines de prueba<br> El setup nunca se
     * correra debido a que es solo de prueba.
     *
     * @param setup True o False segun si se corrio el setup.
     */
    public void buildSetupRun(boolean setup) {

        client.setSetupRun(setup);
    }

    /**
     * Este metodo se encarga de validar que todos los datos esten correctos,
     * chequeando que la bandera sea siempre igual a cero.<br> Luego, contruye
     * el objeto Client. En caso contrario lanza una excepcion acusando de que
     * algunos datos son incorrectos.<br> Devuelve el objeto Client creado.
     *
     * @return Client Con el objeto Client creado.
     * @throws InformationRequiredException Si los datos que es utilizaron para
     * la construccion del objeto son erroneos.
     */
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

            switch (client.getLocalization()) {
                case "Español":
                    this.clientBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/ClientBuilder_es_ES");
                    break;
                case "Ingles":
                    this.clientBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/ClientBuilder_en_US");
                    break;
                default:
                    this.clientBuilder_es_ES = ResourceBundle.getBundle("org/comcast/locale/ClientBuilder_en_US");
                    break;
            }
            throw new InformationRequiredException(clientBuilder_es_ES.getString("FALTAN DATOS REQUERIDOS PARA CONSTRUIR EL CLIENTE"));
        }

        return this.client;
    }
}
