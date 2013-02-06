package org.comcast.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.exceptions.NullObjectParameterException;
import org.comcast.exceptions.UnderflowException;
import org.comcast.router.Message;
import org.comcast.router.OutputChannel;
import org.comcast.structures.BinaryHeap;

/**
 * Clase que se encarga de realizar la coneccion y transferencia de los archivos
 * <br> ya encriptados al servidor ftp.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 2.5
 */
public class Server implements Comparable<Server>, OutputChannel {

    private BinaryHeap<Message> messageToSend;
    private int serverPriority;
    private FTPClient client;
    private ServerConfig config;

    /**
     * Constructor principal de la clase.
     *
     * @param c Con la configuracion del servidor ftp.
     */
    public Server(ServerConfig c) {
        messageToSend = new BinaryHeap<>();
        client = new FTPClient();
        config = c;
    }

    /**
     * Sobrecarga del constructor principal.
     *
     * @param group Con la cola de prioridad de los archivos a transferir.
     * @param c Con la configuracion del servidor ftp.
     */
    public Server(BinaryHeap<Message> group, ServerConfig c) {
        messageToSend = group;
        client = new FTPClient();
        config = c;
    }

    /**
     * Segunda sobrecarga del constructor principal
     *
     * @param group Con los archivos a enviar.
     * @param c Con la configuracion del servidor ftp.
     */
    public Server(Message[] group, ServerConfig c) {
        messageToSend = new BinaryHeap<>(group);
        client = new FTPClient();
        config = c;
    }

    /**
     * Metodo que se encarga de comparar dos objetos Server.
     *
     * @param o Con el objeto a comparar. int (0) si los apellidos son iguales,
     * (-) si el apellido de este objeto esta primero, <br> (+) si el apellido
     * de este objeto esta despues.
     * @overrides compareTo de Comparator.
     */
    @Override
    public int compareTo(Server o) {
        if (o == null) {
            throw new NullObjectParameterException("El objeto servidor esta vacio");
        }

        return (this.getServerPriority() - o.getServerPriority());
    }

    /**
     * Metodo encargado de transferir los archivos desde la maquina local hacia
     * el servidor ftp.
     *
     * @param message El archivo a enviar.
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     */
    private synchronized void uploadMessage(Message message) throws SocketException, IOException {

        try {
            FileInputStream fis = null;

            String local = message.getLocalPath();
            String remote = message.getRemotePath();

            client.setFileTransferMode(FTP.LOCAL_FILE_TYPE);
            client.setFileType(FTPClient.BINARY_FILE_TYPE);

            fis = new FileInputStream(local);
            client.storeFile(remote, fis);

            client.noop();

            fis.close();

        } catch (SocketException ex) {
            closeConnection();
            throw new SocketException(ex.getLocalizedMessage());
        } catch (IOException ex) {
            closeConnection();
            throw new IOException(ex);
        }
    }

    /**
     * Metodo public encargado de procesar la cola de prioridad.
     *
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws UnderflowException Si la cola de prioridad esta vacia.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    @Override
    public synchronized void uploadMessages() throws SocketException, IOException, UnderflowException, FTPConectionRefusedException {
        Message toSend = null;

        openConnection();

        while (!this.messageToSend.isEmpty()) {
            toSend = this.messageToSend.deleteMin();
            uploadMessage(toSend);
        }

        closeConnection();
    }

    /**
     * Este metodo verifica el estado de la coneccion a traves de los codigos
     * que emite el servidor.
     *
     * @param serverReply Con el codigo en int.
     * @throws FTPConectionRefusedException Si el servidor envia un codigo no
     * satisfactorio.
     */
    private synchronized void ftpCodeChecker(int serverReply) throws FTPConectionRefusedException {
        if (!FTPReply.isPositiveCompletion(serverReply)) {
            throw new FTPConectionRefusedException(client.getReplyString());
        }
    }

    /**
     * Metodo encargado de abrir la coneccion hacia el servidor ftp.
     *
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    private synchronized void openConnection() throws SocketException, IOException, FTPConectionRefusedException {
        client.connect(config.getIpAddress(), 21);
        ftpCodeChecker(client.getReplyCode());

        client.login(config.getUserLogin(), config.getPassLogin());
        ftpCodeChecker(client.getReplyCode());

        client.enterLocalPassiveMode();
    }

    /**
     * Metodo que se encarga de cerrar la coneccion con el servidor ftp.
     *
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     */
    private synchronized void closeConnection() throws SocketException, IOException {
        client.logout();
        client.disconnect();
    }

    /**
     * Metodo encargado de transferir los archivos desde el servidor ftp local
     * hacia la maquina local.
     *
     * @param message El archivo a descargar.
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     */
    private synchronized void downloadMessage(Message message) throws SocketException, IOException {
        try {

            FileOutputStream fos = null;

            String local = message.getLocalPath();
            String remote = message.getRemotePath();

            fos = new FileOutputStream(local);
            client.retrieveFile(remote, fos);
            int noop = client.noop();
            fos.close();

        } catch (SocketException ex) {
            client.disconnect();
            throw new SocketException(ex.toString());
        } catch (IOException ex) {
            client.disconnect();
            throw new IOException(ex);
        }
    }

    /**
     * Metodo public encargado de procesar la cola de prioridad.
     *
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws UnderflowException Si la cola de prioridad esta vacia.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    @Override
    public synchronized void downloadMessages() throws SocketException, IOException, UnderflowException, FTPConectionRefusedException {
        Message toSend = null;

        openConnection();

        while (!this.messageToSend.isEmpty()) {
            toSend = this.messageToSend.deleteMin();
            downloadMessage(toSend);
        }

        closeConnection();
    }

    /**
     * Metodo encargado de descargar solo un archivo por vez.
     *
     * @param mess El archivo a descargar.
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public synchronized void downloadSingle(Message mess) throws SocketException, IOException, FTPConectionRefusedException {
        openConnection();

        this.downloadMessage(mess);

        closeConnection();
    }

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
    @Override
    public FTPFile[] retrieveMesseges(String dir) throws SocketException, IOException, FTPConectionRefusedException {

        openConnection();

        FTPFile[] buffer = client.mlistDir(dir);
        int noop = client.noop();

        closeConnection();

        return buffer;
    }

    /**
     * Metodo que devuelve la prioridad del servidor.<br> Reservado para uso
     * futuro / prueba.
     *
     * @return int Con la prioridad.
     */
    public int getServerPriority() {
        return serverPriority;
    }

    /**
     * Metodo que modifica la prioridad del servidor.<br> Reservado para uso
     * futuro / prueba.
     *
     * @param serverPriority Con la nueva prioridad.
     */
    public void setServerPriority(int serverPriority) {
        this.serverPriority = serverPriority;
    }

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
    @Override
    public FTPFile[] retrieveDirectories(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        openConnection();

        FTPFile[] buffer = client.listDirectories(dir);

        closeConnection();

        return buffer;
    }

    /**
     * Metodo que se utiliza para verificar que la coneccion hacia el servidor
     * sea correcta. <br> Se chequea que los parametros de configuracion sean
     * correctos para establecer la comunicacion con el servidor ftp.
     *
     * @throws SocketException Si se produce algun error de coneccion al
     * servidor.
     * @throws IOException Si se produce algun error de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public void testConnection() throws SocketException, IOException, FTPConectionRefusedException {
        openConnection();
        closeConnection();
    }
}
