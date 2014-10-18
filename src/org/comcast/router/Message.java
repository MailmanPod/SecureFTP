package org.comcast.router;

import org.apache.commons.net.ftp.FTPFile;
import org.comcast.exceptions.NullObjectParameterException;

import java.io.File;
import java.io.Serializable;

/**
 * Esta clase representa un paquete que contiene el archivo a bajar o a subir.
 * <br> Almacena otra informacion referida al archivo, como por ejemplo: <br>
 * -Prioridad del archivo en la cola de prioridad. <br> -Si es un archivo a
 * subir, contiene informacion del path local. <br> -Si es un archivo a bajar,
 * contiene informacion del path remoto.
 *
 * @author Damian Bruera
 * @since Java 7
 * @version 3.0
 */
public class Message implements Serializable, Comparable<Message> {

    public static final int HIGH_PRIORITY = 1;
    public static final int NORMAL_PRIORITY = 2;
    public static final int LOW_PRIORITY = 3;
    private InputChannel source;
    private int priority;
    private String localPath;
    private String remotePath;
    private String fileType;
    private FTPFile ftpFile;
    private File localFile;

    /**
     * Contructor de la clase.
     *
     * @param source Cliente que desea subir o bajar archivos.
     * @param priority Prioridad del archivo.
     * @param localPath Ruta absoluta local.
     * @param remotePath Ruta absoluta remota.
     */
    public Message(InputChannel source, int priority, String localPath, String remotePath) {
        this.source = source;
        this.priority = priority;
        this.localPath = localPath;
        this.remotePath = remotePath;
        this.fileType = "unknown";
    }

    /**
     * Retorna el origen / cliente de la aplicacion.
     *
     * @return el cliente.
     */
    public InputChannel getSource() {
        return source;
    }

    /**
     * Modifica el origen / cliente de la aplicaion.
     *
     * @param source Origen nuevo.
     */
    public void setSource(InputChannel source) {
        this.source = source;
    }

    /**
     * Retorna la prioridad del archivo en particular.
     *
     * @return int con la prioridad.
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Metodo toString personalizado, para mostrar los datos del cliente.
     *
     * @return String con la cadena con todos los datos.
     * @overrides toString
     */
    @Override
    public String toString() {
        return "Message{" + "source=" + source + ", priority=" + priority
                + ", localPath=" + localPath + ", remotePath=" + remotePath + '}';
    }

    /**
     * Metodo que se ocupa de comparar dos objetos Message, por su prioridad.
     *
     * @param o Con el objeto a comparar
     * @return int (0) si las prioridades son iguales, (-) si la prioridad de
     * este objeto esta primero, <br> (+) si la prioridad de este objeto esta
     * despues.
     * @overrides compareTo de Comparator.
     */
    @Override
    public int compareTo(Message o) {
        if (o == null) {
            throw new NullObjectParameterException("No hay mensaje para comparar <br> El mensaje esta vacio");
        }

        return this.getPriority() - o.getPriority();
    }

    /**
     * Retorna la ruta absoluta local.
     *
     * @return La ruta absoluta local.
     */
    public String getLocalPath() {
        return localPath;
    }

    /**
     * Modifica la ruta absoluta local.
     *
     * @param localPath Con la nueva ruta.
     */
    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    /**
     * Retorna la ruta absoluta remota.
     *
     * @return La ruta absoluta remota.
     */
    public String getRemotePath() {
        return remotePath;
    }

    /**
     * Modifica la ruta absoluta remota.
     *
     * @param remotePath Con la nueva ruta.
     */
    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    /**
     * Retorna el tipo MIME del archivo.
     *
     * @return El tipo de archivo.
     */
    public String getFileType() {
        return this.fileType;
    }

    /**
     * Modifica el tipo MIME del archivo.
     *
     * @param fileType Con el nuevo tipo de archivo.
     */
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    /**
     * Retorna el archivo version remota.
     *
     * @return el archivo remoto.
     */
    public FTPFile getFtpFile() {
        return ftpFile;
    }

    /**
     * Modifica el archivo version remota.
     *
     * @param ftpFile Nuevo archivo remoto.
     */
    public void setFtpFile(FTPFile ftpFile) {
        this.ftpFile = ftpFile;
    }

    /**
     * Retorna el archivo version local.
     *
     * @return el archivo local.
     */
    public File getLocalFile() {
        return localFile;
    }

    /**
     * Modifica el archivo version local.
     *
     * @param localFile Nuevo archivo local.
     */
    public void setLocalFile(File localFile) {
        this.localFile = localFile;
    }

    /**
     * Modifica la prioridad del archivo.
     *
     * @param priority con la nueva prioridad.
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Retorna la prioridad del archivo, pero version string.
     *
     * @return Un string con la prioridad.
     */
    public String getPriorityString() {

        switch (this.getPriority()) {
            case HIGH_PRIORITY:
                return "High";
            case NORMAL_PRIORITY:
                return "Normal";
            case LOW_PRIORITY:
                return "Low";
            default:
                return "null";
        }
    }
}
