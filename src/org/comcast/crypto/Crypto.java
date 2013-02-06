package org.comcast.crypto;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

/**
 * Clase que se encarga de realizar la encriptacion y a desencriptacion de los
 * archivos <br> que seran subidos al servidor ftp.<br> Su funcionamiento se
 * basa en los siguientes puntos: <br> 1) Se genera los pares de claves RSA de
 * 1024 bits. (Publica y Privada).<br> 2) Se encripta el contenido del archivo
 * con el mecanismo de clave simetrica AES.<br> 3) Se encripta la clave simetria
 * utilizada por AES con la clave publica de RSA.<br> 4) Se envia un archivo que
 * contiene el contenido cifrado + la clave AES encriptada con RSA Publico. 5)
 * Para desencriptar se utilizan los mismo pasos pero de manera inversa, ademas
 * que se utiliza <br> la llave privada para desencriptar la llave simetrica.
 *
 * @author Damian Bruera.
 * @since Java 7
 * @version 1.2.
 */
public class Crypto {

    private static final int KEYSIZE = 1024;

    /**
     * Constructor vacio de la clase.
     */
    public Crypto() {
    }

    /**
     * Metodo encargado de generar el par de llaves de RSA (Publica y
     * Privada).<br> Utiliza un random segura para generarlas.
     *
     * @param pathPublic Ruta absoluta donde se guardara la clave publica.
     * @param pathPrivate Ruta absoluta donde se guardara la clave privada.
     * @param numbBytes Cantidad de bytes que utilizara el generador de llaves.
     * @throws NoSuchAlgorithmException Si existen errores a nivel algoritmo.
     * @throws FileNotFoundException Si no existe el o los arcivos.
     * @throws IOException Por cualquier problema de escritura o lectura.
     */
    public void keyGenerateRSA(String pathPublic, String pathPrivate, int numbBytes) throws NoSuchAlgorithmException, FileNotFoundException, IOException {
        KeyPairGenerator pairgen = KeyPairGenerator.getInstance("RSA");
        SecureRandom random = new SecureRandom();
        byte[] generateSeed = random.generateSeed(numbBytes);
        random.setSeed(generateSeed);

        pairgen.initialize(KEYSIZE, random);
        KeyPair keyPair = pairgen.generateKeyPair();

        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(pathPublic));
        out.writeObject(keyPair.getPublic());

        ObjectOutputStream out1 = new ObjectOutputStream(new FileOutputStream(pathPrivate));
        out1.writeObject(keyPair.getPrivate());
    }

    /**
     * Metodo encargado de realizar la encriptacion de la clave simetrica a
     * traves de RSA.<br> Esta encriptacion se realiza segun clave publica.<br>
     * Luego este metodo configura el cifrador para la encriptacion con AES.
     *
     * @param pathNative Ruta absoluta del archivo a encriptar.
     * @param pathCrypto Ruta absoluta en donde se colocara el archivo
     * encriptado.
     * @param publicKeyFile Ruta absoluta en donde se encuentra la llave
     * publica.
     * @throws NoSuchAlgorithmException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws GeneralSecurityException
     */
    public void encryptRSA(String pathNative, String pathCrypto, String publicKeyFile) throws NoSuchAlgorithmException, IOException,
            ClassNotFoundException, NoSuchPaddingException, InvalidKeyException, GeneralSecurityException {

        KeyGenerator keygen = KeyGenerator.getInstance("AES");
        SecureRandom random = new SecureRandom();
        keygen.init(random);
        SecretKey key = keygen.generateKey();

        // wrap with RSA public key
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(publicKeyFile));
        DataOutputStream out = new DataOutputStream(new FileOutputStream(pathCrypto));
        InputStream in = new FileInputStream(pathNative);

        Key publicKey = (Key) keyIn.readObject();
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.WRAP_MODE, publicKey);
        byte[] wrappedKey = cipher.wrap(key);
        out.writeInt(wrappedKey.length);
        out.write(wrappedKey);

        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);
        crypt(in, out, cipher);

    }

    /**
     * Metodo encargado de realizar la desencriptacion de la clave simetrica a
     * traves de RSA.<br> Esta desencriptacion se realiza segun clave
     * privada.<br> Luego este metodo configura el cifrador para la
     * desencriptacion con AES.
     *
     * @param pathCrypto Ruta absoluta del archivo a desencriptar.
     * @param pathNative Ruta absoluta en donde se colocara el archivo
     * desencriptado.
     * @param privateKeyFile Ruta absoluta en donde se encuentra la llave
     * privada.
     * @throws FileNotFoundException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws NoSuchAlgorithmException
     * @throws NoSuchPaddingException
     * @throws InvalidKeyException
     * @throws GeneralSecurityException
     */
    public void decryptRSA(String pathCrypto, String pathNative, String privateKeyFile) throws FileNotFoundException, IOException, ClassNotFoundException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, GeneralSecurityException {

        DataInputStream in = new DataInputStream(new FileInputStream(pathCrypto));
        ObjectInputStream keyIn = new ObjectInputStream(new FileInputStream(privateKeyFile));
        OutputStream out = new FileOutputStream(pathNative);
        int length = in.readInt();
        byte[] wrappedKey = new byte[length];
        in.read(wrappedKey, 0, length);

        // unwrap with RSA private key
        Key privateKey = (Key) keyIn.readObject();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.UNWRAP_MODE, privateKey);
        Key key = cipher.unwrap(wrappedKey, "AES", Cipher.SECRET_KEY);

        cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        crypt(in, out, cipher);
    }

    /**
     * Este metodo se encarga tanto de encriptar como de desencriptar un
     * archivo.<br> Basicamente el proceso que utiliza para ambos procesos es la
     * de "dividir" el <br> el archivo en bloques de tamaño fijo para luego
     * encriptarlo o desencriptarlo, <br> segun la configuracion que posea el
     * cifrador.<br> En el caso de que el ultimo bloque sea menor al tamaño fijo
     * del bloque, <br> se recurre a una tecnica llamada padding, el cual
     * rellena con datos el ultimo bloque <br> para luego poder desencriptar el
     * archivo.
     *
     * @param in Flujo de entrada.
     * @param out Flujo de salida.
     * @param cipher Configuracion del cifrador.
     * @throws IOException
     * @throws GeneralSecurityException
     */
    private final void crypt(InputStream in, OutputStream out, Cipher cipher) throws IOException,
            GeneralSecurityException {
        int blockSize = cipher.getBlockSize();
        int outputSize = cipher.getOutputSize(blockSize);
        byte[] inBytes = new byte[blockSize];
        byte[] outBytes = new byte[outputSize];

        int inLength = 0;
        boolean more = true;
        while (more) {
            inLength = in.read(inBytes);
            if (inLength == blockSize) {
                int outLength = cipher.update(inBytes, 0, blockSize, outBytes);
                out.write(outBytes, 0, outLength);
            } else {
                more = false;
            }
        }
        if (inLength > 0) {
            outBytes = cipher.doFinal(inBytes, 0, inLength);
        } else {
            outBytes = cipher.doFinal();
        }
        out.write(outBytes);
    }
}
