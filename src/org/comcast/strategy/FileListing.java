package org.comcast.strategy;

import org.comcast.router.Message;

/**
 * Clase que es parte de la implementacion del patron strategy.<br> Brinda
 * metodos para el listado de archivos segun algun criterio especificado.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 1.0
 */
public class FileListing {

    private ListingStrategy strategy;

    /**
     * Constructor de la clase.
     */
    public FileListing() {
    }

    /**
     * Metodo que tiene como finalidad la de setear el criterio de ordenacion de
     * los listados.
     *
     * @param newStrategy Con el nuevo criterio.
     */
    public void setListingStrategy(ListingStrategy newStrategy) {
        this.strategy = newStrategy;
    }

    /**
     * Retorna el listado de archivos locales, segun algun criterio de
     * ordenacion.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de archivos ordenado segun algun criterio.
     */
    public Message[] getLocalMessage(String pathName) {
        return strategy.listLocalMessages(pathName);
    }

    /**
     * Retorna el listado de archivos remotos, segun algun criterio de
     * ordenacion.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de archivos ordenado segun algun criterio.
     */
    public Message[] getRemoteMessages(String pathName) {
        return strategy.listRemoteFiles(pathName);
    }

    /**
     * Retorna el listado de los nombres de los directorios remotos.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de los nombres de los directorios remotos.
     */
    public String[] getRemoteDirectories(String pathName) {
        return strategy.getRemoteDirectories(pathName);
    }
}
