package org.comcast.router;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.comcast.builder.Client;
import org.comcast.exceptions.FTPConectionRefusedException;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.SimpleList;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * Clase que se encarga de realizar la recuperacion de los archivos, con el fin
 * de realizar busquedas, ordenamientos, listados.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 3.5
 */
public class RouterRetrieve {

    private Server server;
    private ServerConfig config;
    private SimpleList<Message> recursiveFiles;
    private SimpleList<Message> recursiveDir;

    public RouterRetrieve(ServerConfig c) {
        this.config = c;
        this.server = new Server(c);
    }

    /**
     * Este metodo recupera todos los archivos que estan en un directorio remoto
     * especificado.
     *
     * @param dir Con el directorio Remoto
     * @return Una lista de archivos dentro de ese directorio.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public FTPFile[] getFiles(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        return server.retrieveMesseges(dir);
    }

    /**
     * Este Metodo recupera el nombre de todos los directorios remotos qu se
     * encuentran bajo un directorio remoto.
     *
     * @param dir Con el directorio remoto.
     * @return Listado de todos los nombres de los directorios.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public String[] getDirNamesCurrent(String path) throws SocketException, IOException, FTPConectionRefusedException {
        FTPFile[] retrieveMesseges = this.server.retrieveDirectories(path);
        int count = 0;
        for (FTPFile f : retrieveMesseges) {
            if (f.isDirectory()) {
                count++;
            }
        }

        String[] s = new String[count];
        int i = 0;

        for (FTPFile f : retrieveMesseges) {
            if (f.isDirectory()) {
                s[i] = f.getName();
                i++;
            }
        }
        return s;
    }

    /**
     * Este Metodo recupera archivos + directorios del directorio remoto actual.
     *
     * @param dir Con el directorio remoto.
     * @return Listado de archivos y directorios.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public SimpleList<Message> getSimpleList(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        SimpleList<Message> buffer = new SimpleList<>();
        FTPFile[] ftp = getFiles(dir);

        for (FTPFile file : ftp) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName());
            aux.setFtpFile(file);

            if (file.isFile()) {
                server.downloadSingle(aux);
                String x = getType(aux.getLocalPath());
                aux.setFileType(x);
            }

            buffer.addLast(aux);
        }

        return buffer;
    }

    /**
     * Este Metodo recupera los archivos del directorio actual del Servidor.
     *
     * @param dir Con el directorio remoto.
     * @return Lista de todos los archivos bajo ese mismo directorio.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public SimpleList<Message> getSimpleListCurrent(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        SimpleList<Message> buffer = new SimpleList<>();
        FTPFile[] ftp = getFiles(dir);

        for (FTPFile file : ftp) {

            if (file.isFile()) {
                Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName());
                aux.setFtpFile(file);
                aux.setLocalFile(new File(aux.getLocalPath()));
                server.downloadSingle(aux);
                String x = getType(aux.getLocalPath());
                aux.setFileType(x);
                buffer.addLast(aux);
            }
        }

        return buffer;
    }

    /**
     * Este metodo recupera todos los directorios mas los archivos. Formateados
     * segun en que carpeta estan.
     *
     * @param dir Con el directorio remoto.
     * @return Lista de todos los archivos bajo ese mismo directorio.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public SimpleList<Message> getAllFilesDir(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        this.recursiveDir = new SimpleList<>();
        FTPFile[] files = this.server.retrieveMesseges(dir);

        for (FTPFile ff : files) {
            storeFileDir(ff, dir);
        }

        return recursiveDir;
    }

    /**
     * Soporte Recursivo. Procesa Directorios y Archivos
     *
     * @param file Archivo o Directorio actual a la recursion.
     * @param dir Con el directorio remoto.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    private void storeFileDir(FTPFile file, String dir) throws SocketException, IOException, FTPConectionRefusedException {

        if (file.isFile()) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName());
            aux.setFtpFile(file);
            server.downloadSingle(aux);
            String x = getType(aux.getLocalPath());
            aux.setFileType(x);
            this.recursiveDir.addInOrder(aux);
        }

        if (file.isDirectory()) {
            String dire = dir + file.getName() + "/";
            Message auxi = new Message(new Client(), Message.NORMAL_PRIORITY, "", dire);
            auxi.setFtpFile(file);
            this.recursiveDir.addInOrder(auxi);

            FTPFile[] p = getFiles(dire);

            for (FTPFile s : p) {
                storeFileDir(s, dire);
            }
        }
    }

    /**
     * Este metodo recupera todos los archivos del servidor. Busqueda
     * aproximacion de un archivo en todo el servidor.
     *
     * @@param dir Con el directorio remoto.
     * @return Lista de todos los archivos del servidor.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    public SimpleList<Message> getAllFiles(String dir) throws SocketException, IOException, FTPConectionRefusedException {
        this.recursiveFiles = new SimpleList<>();
        FTPFile[] files = this.server.retrieveMesseges(dir);

        for (FTPFile ff : files) {
            storeFiles(ff, dir);
        }

        return recursiveFiles;
    }

    /**
     * Soporte Recursivo. Procesa solo archivos.
     *
     * @param file Archivo o Directorio actual a la recursion.
     * @param dir Con el directorio remoto.
     * @throws SocketException Si hay error de conexion con el servidor.
     * @throws IOException Por errores de entrada o salida.
     * @throws FTPConectionRefusedException Si el servidor rechaza la coneccion.
     */
    private void storeFiles(FTPFile file, String dir) throws SocketException, IOException, FTPConectionRefusedException {

        if (file.isFile()) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName());
            aux.setFtpFile(file);
            server.downloadSingle(aux);
            String x = getType(aux.getLocalPath());
            aux.setFileType(x);
            this.recursiveFiles.addInOrder(aux);
        }

        if (file.isDirectory()) {
            String dire = dir + file.getName() + "/";
            FTPFile[] p = getFiles(dire);

            for (FTPFile s : p) {
                storeFiles(s, dire);
            }
        }
    }

    /**
     * Metodo que obtiene el tipo MIME de los metadatos del archivo.
     *
     * @param fileName Con la ubicacion del archivo.
     * @return El tipo MIME.
     * @throws IOException Error de lectura del archivo.
     */
    private String getType(String fileName) throws IOException {

        FileInputStream is = null;
        String type = "application/unregistered";
        try {
            File f = new File(fileName);
            is = new FileInputStream(f);

            ContentHandler contenthandler = new BodyContentHandler(20 * 2048 * 2048);
            Metadata metadata = new Metadata();
            metadata.set(Metadata.RESOURCE_NAME_KEY, f.getName());
            Parser parser = new AutoDetectParser();
            ParseContext co = new ParseContext();
            parser.parse(is, contenthandler, metadata, co);
            //System.out.println("Mime: " + metadata.get(Metadata.CONTENT_TYPE));
            type = metadata.get(Metadata.CONTENT_TYPE);

        } catch (IOException | SAXException | TikaException e) {
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return type;
    }

    /**
     * Este metodo recupera todos los archivos bajo un mismo directorio
     * local.<br> Formato File.
     *
     * @param pathName Ubicacion del directorio.
     * @return Listado con todos los archivos.
     */
    private SimpleList<File> getLocalFiles(String pathName) {
        File f = new File(pathName);
        SimpleList<File> list = new SimpleList<>();

        if (f.isDirectory()) {
            File[] array = f.listFiles();
            for (File aux : array) {
                list.addInOrder(aux);
            }
        }

        return list;
    }

    /**
     * Este metodo recupera todos los archivos bajo un mismo directorio
     * local.<br> Formato Message.
     *
     * @param pathName Ubicacion del directorio.
     * @return Listado con todos los archivos.
     */
    public SimpleList<Message> getLocalMessages(String pathName) throws IOException {
        SimpleList<Message> list = new SimpleList<>();
        SimpleList<File> fileList = getLocalFiles(pathName);

        LocalIterator<File> iter = fileList.getIterador();

        while (iter.hasMoreElements()) {
            File aux = iter.returnElement();

            if (aux.isFile()) {
                Message mes = new Message(new Client(), Message.NORMAL_PRIORITY, aux.getAbsolutePath(), "");
                mes.setFtpFile(new FTPFile());
                mes.setLocalFile(aux);
                mes.setFileType(getType(aux.getAbsolutePath()));
                list.addLast(mes);
            }
        }

        return list;
    }

    /**
     * Este metodo recupera todos los archivos bajo un mismo directorio
     * local.<br> Formato Message.<br> Agrega un destino remoto como parametro.
     *
     * @param pathName Ubicacion del directorio.
     * @param destin Con el destino remoto.
     * @return Listado con todos los archivos.
     */
    public SimpleList<Message> getLocalMessages(String pathName, String destin) throws IOException {
        SimpleList<Message> list = new SimpleList<>();
        SimpleList<File> fileList = getLocalFiles(pathName);

        LocalIterator<File> iter = fileList.getIterador();

        while (iter.hasMoreElements()) {
            File aux = iter.returnElement();

            if (aux.isFile()) {
                Message mes = new Message(new Client(), Message.NORMAL_PRIORITY, aux.getAbsolutePath(), destin + aux.getName());
                mes.setFtpFile(new FTPFile());
                mes.setLocalFile(aux);
                mes.setFileType(getType(aux.getAbsolutePath()));
                list.addLast(mes);
            }
        }

        return list;
    }

    /**
     * Realiza una prueba de coneccion al servidor ftp.
     *
     * @throws SocketException Si hay error de comunicacion.
     * @throws IOException Si hay error de lectura/escritura del disco.
     * @throws FTPConectionRefusedException Si el servidor rechazo la coneccion.
     */
    public void testConnection() throws SocketException, IOException, FTPConectionRefusedException {
        this.server.testConnection();
    }
}
