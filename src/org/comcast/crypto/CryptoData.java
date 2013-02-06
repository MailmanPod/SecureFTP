package org.comcast.crypto;

/**
 * Clase que representa datos almacenados en un archivo xml.<br> Dicho archivo
 * contiene datos que luego seran pasados al cifrador.
 *
 * @author Damian Bruera.
 * @since Java 7
 * @version 1.1
 */
public class CryptoData implements Comparable<CryptoData> {

    private String fileName;
    private String original;
    private String cryptoFile;
    private String publicKey;
    private String privateKey;
    private String originalExtension;
    private String destination;

    /**
     * Constructor de la clase.
     *
     * @param fileName Nombre del archivo, sin su extension.
     * @param original Ruta absoluta del archivo original.
     * @param cryptoFile Ruta absoluta del archivo encriptado.
     * @param publicKey Ruta absoluta del archivo de clave publica.
     * @param privateKey Ruta absoluta del archivo de clave privada.
     * @param extension Extension original del archivo.
     * @param destination Nombre del archivo encriptado, con su extension.
     */
    public CryptoData(String fileName, String original, String cryptoFile, String publicKey, String privateKey, String extension, String destination) {
        this.fileName = fileName;
        this.original = original;
        this.cryptoFile = cryptoFile;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.originalExtension = extension;
        this.destination = destination;
    }

    /**
     * Metodo get, retorna el nombre del archivo, sin extension.
     *
     * @return String con el nombre.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Metodo set, modifica el nombre del archivo, sin extension.
     *
     * @param fileName Con el nuevo nombre
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Metodo get, retorna la ruta absoluta del archivo original.
     *
     * @return String con la ruta absoluta.
     */
    public String getOriginal() {
        return original;
    }

    /**
     * Metodo set, modifica la ruta absoluta del archivo original.
     *
     * @param original Con la nueva ruta.
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * Metodo get, retorna la ruta absoluta del archivo encriptado.
     *
     * @return String con la ruta absoluta.
     */
    public String getCryptoFile() {
        return cryptoFile;
    }

    /**
     * Metodo set, modifica la ruta absoluta del archivo encriptado.
     *
     * @param cryptoFile Con la nueva ruta.
     */
    public void setCryptoFile(String cryptoFile) {
        this.cryptoFile = cryptoFile;
    }

    /**
     * Metodo get, retorna la ruta absoluta del archivo de clave publica.
     *
     * @return String con la ruta absoluta.
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * Metodo set, modifica la ruta absoluta del archivo de clave publica.
     *
     * @param publicKey Con la nueva ruta.
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * Metodo get, retorna la ruta absoluta del archivo de clave privada.
     *
     * @return String con la ruta absoluta.
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * Metodo set, modifica la ruta absoluta del archivo de clave privada.
     *
     * @param privateKey Con la nueva ruta.
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    /**
     * Metodo toString personalizado, para mostrar los datos de la configuracion
     * del cifrador.
     *
     * @return String con la cadena de datos.
     * @overrides toString.
     */
    @Override
    public String toString() {
        return "CryptoData{" + "fileName=" + fileName + ", original=" + original
                + ", cryptoFile=" + cryptoFile + ", publicKey=" + publicKey + ", privateKey=" + privateKey
                + ", originalExtension=" + originalExtension + ", destination=" + destination + '}';
    }

    @Override
    public int compareTo(CryptoData o) {
        return this.getOriginal().compareToIgnoreCase(o.getOriginal());
    }

    /**
     * Metodo get, retorna la extension original del archivo.
     *
     * @return String con la extension del archivo.
     */
    public String getOriginalExtension() {
        return originalExtension;
    }

    /**
     * Metodo set, modifica la extension original del archivo.
     *
     * @param originalExtension con la nueva extension.
     */
    public void setOriginalExtension(String originalExtension) {
        this.originalExtension = originalExtension;
    }

    /**
     * Metodo get, retorna el nombre del archivo encriptado, con su extension.
     *
     * @return String con el nombre completo.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Metodo set, modifica el nombre del archivo encriptado, con su extension.
     *
     * @param destination Con el nuevo nombre completo.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }
}
