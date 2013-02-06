package org.comcast.router;

import java.io.IOException;
import java.io.Serializable;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPFile;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.exceptions.UnderflowException;

/**
 * Clase que define cuales son las tareas que debe realizar un canal de salida.
 * <br> Canal de salida: Referido a aquellas clases que se encargan del flujo de
 * archivos entre el servidor ftp y la maquina local.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 1.2
 */
public interface OutputChannel extends Serializable {

    public static final int MAX_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;

    /**
     * Metodo encargado de subir los archivos al servidor ftp.
     *
     * @throws SocketException Error en la comunicacion via Socket al servidor.
     * @throws IOException Lectura y escritura en disco.
     * @throws UnderflowException Acceso a cola de prioridad vacia.
     * @throws FTPConectionRefusedException Servidor rechazo la coneccion.
     */
    public void uploadMessages() throws SocketException, IOException, UnderflowException, FTPConectionRefusedException;

    /**
     * Metodo encargado de bajar los archivos del servidor ftp.
     *
     * @throws SocketException Error en la comunicacion via Socket al servidor.
     * @throws IOException Lectura y escritura en disco.
     * @throws UnderflowException Acceso a cola de prioridad vacia.
     * @throws FTPConectionRefusedException Servidor rechazo la coneccion.
     */
    public void downloadMessages() throws SocketException, IOException, UnderflowException, FTPConectionRefusedException;

    /**
     * Metodo que recupera todos los archivos remotos, segun la ruta remota que
     * se pasa como parametro.<br> La diferencia radica en que devuelve un
     * vector con objetos FTPFile y no Message.
     *
     * @param dir Con la ruta remota donde se encuentran los archivos.
     * @return Un vector con los archivos remotos.
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public FTPFile[] retrieveMesseges(String dir) throws SocketException, IOException, FTPConectionRefusedException;

    /**
     * Metodo que recupera todos los directorios remotos, segun la ruta remota
     * que se pasa como parametro.<br> La diferencia radica en que devuelve un
     * vector con objetos FTPFile y no Message.
     *
     * @param dir Con la ruta remota donde se encuentran los directorios.
     * @return Un vector con los directorios remotos.
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public FTPFile[] retrieveDirectories(String dir) throws SocketException, IOException, FTPConectionRefusedException;
}
