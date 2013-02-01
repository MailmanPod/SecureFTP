package org.comcast.builder;

import org.comcast.exceptions.NullObjectParameterException;
import org.comcast.router.InputChannel;

/**
 * Clase que representa un objeto Client.<br> Este objeto se utilizara con el
 * fin de guardar informacion relevante sobre el propietario del programa.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since 2012.
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

    /**
     * Constructor vacio de la clase Client.
     */
    public Client() {
    }

    /**
     * Metodo que se ocupa de comparar dos objetos client, por su apellido.
     *
     * @param o Con el objeto a comparar
     * @return int (0) si los apellidos son iguales, (-) si el apellido de este
     * objeto esta primero, <br> (+) si el apellido de este objeto esta despues.
     * @overrides compareTo de Comparator.
     */
    @Override
    public int compareTo(Client o) {
        if (o == null) {
            throw new NullObjectParameterException("El objeto Cliente no tiene ningun valor asociado");
        }

        return this.getClientLastName().compareTo(o.getClientLastName());
    }

    /**
     * Metodo get, retorna el nombre del cliente.
     *
     * @return String Con el nombre del cliente.
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Metodo set, modifica el nombre de cliente.
     *
     * @param clientName Con el nuevo nombre.
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * Metodo get, retorna el apellido del cliente.
     *
     * @return String Con el apellido del cliente.
     */
    public String getClientLastName() {
        return clientLastName;
    }

    /**
     * Metodo set, modifica el apellido del cliente.
     *
     * @param clientLastName Con el nuevo apellido.
     */
    public void setClientLastName(String clientLastName) {
        this.clientLastName = clientLastName;
    }

    /**
     * Metodo get, retorna el nombre de la organizacion a la cual pertenece el
     * cliente.
     *
     * @return String Con el nombre de la organizacion.
     */
    public String getOrganization() {
        return organization;
    }

    /**
     * Metodo set, modifica el nombre de la organizacion a la cual pertenece el
     * cliente.
     *
     * @param organization Con el nuevo nombre de la organizacion.
     */
    public void setOrganization(String organization) {
        this.organization = organization;
    }

    /**
     * Metodo get, retorna la ruta absoluta hacia donde seran almacenados los
     * archivos.
     *
     * @return String con la ruta absoluta.
     */
    public String getDownloadPath() {
        return downloadPath;
    }

    /**
     * Metodo set, modifica la ruta absoluta hacia donde seran almacenados los
     * archivos.
     *
     * @param downloadPath Con la nueva ruta.
     */
    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    /**
     * Metodo toString personalizado, para mostrar los datos del cliente.
     *
     * @return String con la cadena con todos los datos.
     * @overrides toString
     */
    @Override
    public String toString() {
        return "Client{" + "clientName=" + clientName + ", clientLastName=" + clientLastName + ", organization=" + organization
                + ", setupRun=" + setupRun + ", localization=" + localization + ", downloadPath=" + downloadPath
                + ", publicStorage=" + publicStorage + ", privateStorage=" + privateStorage + '}';
    }

    /**
     * Metodo que retorna si el setup se ha corrido. Reservado para uso futuro /
     * de prueba.
     *
     * @return True si se ha corrido, False si no.
     */
    public boolean isSetupRun() {
        return setupRun;
    }

    /**
     * Metodo set, modifica el valor para plasmar la ejecucion o no ejecucion
     * del Setup.<br> Reservado para uso futuro / de prueba.
     *
     * @param setupRun the setupRun to set
     */
    public void setSetupRun(boolean setupRun) {
        this.setupRun = setupRun;
    }

    /**
     * Retorna el idioma actual de la aplicacion.
     *
     * @return String con el idioma.
     */
    public String getLocalization() {
        return localization;
    }

    /**
     * Modifica el idioma actual de la aplicacion.
     *
     * @param localization Con el idioma nuevo.
     */
    public void setLocalization(String localization) {
        this.localization = localization;
    }

    /**
     * Metodo get, retorna la ruta absoluta donde se almacenan las claves
     * publicas.
     *
     * @return String con la ruta absoluta de la clave publica.
     */
    public String getPublicStorage() {
        return publicStorage;
    }

    /**
     * Metodo set, modifica la ruta absoluta donde se almacenan las claves
     * publicas.
     *
     * @param publicStorage Con la nueva ruta absoluta.
     */
    public void setPublicStorage(String publicStorage) {
        this.publicStorage = publicStorage;
    }

    /**
     * Metodo get, retorna la ruta absoluta donde se almacenan las claves
     * privadas.
     *
     * @return String con la ruta absoluta de la clave privada.
     */
    public String getPrivateStorage() {
        return privateStorage;
    }

    /**
     * Metodo set, modifica la ruta absoluta donde se almacenan las claves
     * privadas.
     *
     * @param publicStorage Con la nueva ruta absoluta.
     */
    public void setPrivateStorage(String privateStorage) {
        this.privateStorage = privateStorage;
    }
}
