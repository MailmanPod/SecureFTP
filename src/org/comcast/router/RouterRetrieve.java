/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.router;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.SocketException;
import org.apache.commons.io.filefilter.FileFileFilter;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.comcast.logic.Client;
import org.comcast.logic.Server;
import org.comcast.logic.ServerConfig;
import org.comcast.structures.LocalIterator;
import org.comcast.structures.SimpleList;
import org.xml.sax.ContentHandler;

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
    public FTPFile[] getFiles(String dir) throws SocketException, IOException {
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
    public String[] getDirNames(String dir) throws SocketException, IOException {
        Message[] array = this.getAllFilesDir(dir).toArray(Message.class);
        int count = 0;

        for (Message m : array) {
            FTPFile ff = m.getEncapsulation();
            if (ff.isDirectory()) {
                count++;
            }
        }

        String[] names = new String[count];

        for (int i = 0, j = 0; i < array.length; i++) {
            Message aux = array[i];
            FTPFile ff = aux.getEncapsulation();

            if (ff.isDirectory()) {
                names[j] = aux.getRemotePath();
                j++;
            }
        }

        return names;
    }

    /**
     * Este trae archivos + directorios del directorio actual.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public SimpleList<Message> getSimpleList(String dir) throws SocketException, IOException {
        SimpleList<Message> buffer = new SimpleList<>();
        FTPFile[] ftp = getFiles(dir);

        for (FTPFile file : ftp) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName(), file);

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
     * Este trae los archivos del directorio actual. Construir la tabla.
     *
     * @param dir
     * @return
     * @throws SocketException
     * @throws IOException
     */
    public SimpleList<Message> getSimpleListCurrent(String dir) throws SocketException, IOException {
        SimpleList<Message> buffer = new SimpleList<>();
        FTPFile[] ftp = getFiles(dir);

        for (FTPFile file : ftp) {

            if (file.isFile()) {
                Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName(), file);
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
    public SimpleList<Message> getAllFilesDir(String dir) throws SocketException, IOException {
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
    private void storeFileDir(FTPFile file, String dir) throws SocketException, IOException {

        if (file.isFile()) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName(), file);
            server.downloadSingle(aux);
            String x = getType(aux.getLocalPath());
            aux.setFileType(x);
            this.recursiveDir.addInOrder(aux);
        }

        if (file.isDirectory()) {
            String dire = dir + file.getName() + "/";
            Message auxi = new Message(new Client(), Message.NORMAL_PRIORITY, "", dire, file);
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
    public SimpleList<Message> getAllFiles(String dir) throws SocketException, IOException {
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
    private void storeFiles(FTPFile file, String dir) throws SocketException, IOException {

        if (file.isFile()) {
            Message aux = new Message(new Client(), Message.NORMAL_PRIORITY, "C:\\Temp\\" + file.getName(), dir + file.getName(), file);
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
        String type = "unknown";
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

        } catch (Exception e) {
        } finally {
            if (is != null) {
                is.close();
            }
        }

        return type;
    }

    public SimpleList<File> getLocalFiles(String pathName) {
        File f = new File(pathName);
        SimpleList<File> list = new SimpleList<>();

        if (f.isDirectory()) {
            File[] array = f.listFiles();
            SimpleList<File> buffer = new SimpleList<>();

            list = buffer.toSimpleList(array);
        }

        return list;
    }

    public SimpleList<File> getLocalFiles(String pathName, FileFilter filter) {
        File f = new File(pathName);
        SimpleList<File> list = new SimpleList<>();

        if (f.isDirectory()) {
            File[] array = f.listFiles(filter);
            SimpleList<File> buffer = new SimpleList<>();

            list = buffer.toSimpleList(array);
        }

        return list;
    }

    public SimpleList<Message> getLocalMessages(String pathName, String destin, FileFilter filter) throws IOException {
        SimpleList<Message> list = new SimpleList<>();
        SimpleList<File> fileList = null;
        if (filter != null) {
            fileList = getLocalFiles(pathName, filter);
        } else {
            fileList = getLocalFiles(pathName);
        }

        LocalIterator<File> iter = fileList.getIterador();

        while (iter.hasMoreElements()) {
            File aux = iter.returnElement();

            if (aux.isFile()) {
                Message mes = new Message(new Client(), Message.NORMAL_PRIORITY, aux.getAbsolutePath(), destin + aux.getName(), new FTPFile());
                mes.setFileType(getType(aux.getAbsolutePath()));
                list.addLast(mes);
            }
        }

        return list;
    }
}
