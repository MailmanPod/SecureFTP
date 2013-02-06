package org.comcast.proxy;

import org.comcast.logic.DateScheduler;
import org.comcast.router.Message;
import org.comcast.structures.SimpleList;

/**
 * Clase que define la funcionalidad de la aplicacion.
 *
 * @author Damian Bruera
 * @since 2012
 * @version 1.5
 */
public interface InterfaceWorks {

    /**
     * Funcionalidad: encriptar y subir archivos a un servidor ftp.
     *
     * @param toTransfer Archivos a subir.
     * @param date Fecha en la que se realizara la tarea (Tarea Programada).
     * @throws Exception Errores durante la ejecucion de la tarea.
     */
    public void transferFiles(SimpleList<Message> toTransfer, DateScheduler date) throws Exception;

    /**
     * Funcionalidad: bajar archivos desde un servidor ftp.
     *
     * @param toDownload Archivos a bajar.
     * @param date Fecha en la que se realizara la tarea (Tarea Programada).
     * @throws Exception Errores durante la ejecuion de la tarea.
     */
    public void downloadFiles(SimpleList<Message> toDownload, DateScheduler date) throws Exception;

    /**
     * Funcionalidad: Desencriptar los archivos bajados.
     *
     * @param toDownload Archivos a desencriptar.
     * @throws Exception Errores durante la ejecucion.
     */
    public void decryptFiles(SimpleList<Message> toDownload) throws Exception;
}
