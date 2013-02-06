/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Quality of Service
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
     * Este trae todo lo que esta en un directorio especificado.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public FTPFile[] getFiles(String dir) throws SocketException, IOException, FTPConectionRefusedException{
        return server.retrieveMesseges(dir);
    }

    /**
     * Este trae el nombre de todos los directorios del servidor. Para armar un
     * arbol de directorios remotos.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public String[] getDirNamesCurrent(String path) throws SocketException, IOException, FTPConectionRefusedException{
        FTPFile[] retrieveMesseges = this.server.retrieveDirectories(path);
        int count = 0;
        for(FTPFile f : retrieveMesseges){
            if(f.isDirectory()){
                count++;
            }
        }
        
        String[] s = new String[count];
        int i = 0;
        
        for(FTPFile f : retrieveMesseges){
            if(f.isDirectory()){
                s[i] = f.getName();
                i++;
            }
        }
        return s;
    }

    /**
     * Este trae archivos + directorios del directorio actual.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
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
     * Este trae los archivos del directorio actual del Servidor. Construir la tabla.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
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
     * Este trae todos los directorios mas los archivos. Formateados segun en
     * que carpeta estan.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
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
     * Soporte Recursivo
     *
     * @param file
     * @param dir
     * @throws SocketException
     * @throws IOException
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
     * Este solo trae todos los archivos del servidor. Busqueda aproximacion de
     * un archivo en todo el servidor.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
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
     * Soporte recursivo.
     *
     * @param file
     * @param dir
     * @throws SocketException
     * @throws IOException
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
    
    public void testConnection() throws SocketException, IOException, FTPConectionRefusedException{
        this.server.testConnection();
    }
}
