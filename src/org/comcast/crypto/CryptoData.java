/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.crypto;

/**
 *
 * @author Quality of Service
 */
public class CryptoData implements Comparable<CryptoData> {

    private String fileName;
    private String original;
    private String cryptoFile;
    private String publicKey;
    private String privateKey;
    private String originalExtension;

    public CryptoData(String fileName, String original, String cryptoFile, String publicKey, String privateKey, String extension) {
        this.fileName = fileName;
        this.original = original;
        this.cryptoFile = cryptoFile;
        this.publicKey = publicKey;
        this.privateKey = privateKey;
        this.originalExtension = extension;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @param fileName the fileName to set
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * @return the original
     */
    public String getOriginal() {
        return original;
    }

    /**
     * @param original the original to set
     */
    public void setOriginal(String original) {
        this.original = original;
    }

    /**
     * @return the cryptoFile
     */
    public String getCryptoFile() {
        return cryptoFile;
    }

    /**
     * @param cryptoFile the cryptoFile to set
     */
    public void setCryptoFile(String cryptoFile) {
        this.cryptoFile = cryptoFile;
    }

    /**
     * @return the publicKey
     */
    public String getPublicKey() {
        return publicKey;
    }

    /**
     * @param publicKey the publicKey to set
     */
    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    /**
     * @return the privateKey
     */
    public String getPrivateKey() {
        return privateKey;
    }

    /**
     * @param privateKey the privateKey to set
     */
    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    @Override
    public String toString() {
        return "CryptoData{" + "fileName=" + fileName + ", original=" + original + ""
                + ", cryptoFile=" + cryptoFile + ", publicKey=" + publicKey + ", privateKey=" + privateKey + ", originalExtension=" + getOriginalExtension() + '}';
    }

    @Override
    public int compareTo(CryptoData o) {
        return this.getOriginal().compareToIgnoreCase(o.getOriginal());
    }

    /**
     * @return the originalExtension
     */
    public String getOriginalExtension() {
        return originalExtension;
    }

    /**
     * @param originalExtension the originalExtension to set
     */
    public void setOriginalExtension(String originalExtension) {
        this.originalExtension = originalExtension;
    }
}
