package org.comcast.strategy;

import org.comcast.router.Message;

/**
 * Interfaz que proporciona las operaciones basicas para listar los archivos
 * tanto locales como remotos.<br> Define operaciones para todas las clases de
 * listado del patron strategy.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 1.0
 */
public interface ListingStrategy {

    public static final int ASC = 1;
    public static final int DESC = 2;

    /**
     * Retorna el listado de archivos locales, segun algun criterio de
     * ordenacion.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de archivos ordenado segun algun criterio.
     */
    public Message[] listLocalMessages(String pathName);

    /**
     * Retorna el listado de archivos remotos, segun algun criterio de
     * ordenacion.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de archivos ordenado segun algun criterio.
     */
    public Message[] listRemoteFiles(String pathName);

    /**
     * Retorna el listado de los nombres de los directorios remotos.
     *
     * @param pathName Con la ruta absoluta al directorio que se quiere listar.
     * @return Listado de los nombres de los directorios remotos.
     */
    public String[] getRemoteDirectories(String pathName);
}
