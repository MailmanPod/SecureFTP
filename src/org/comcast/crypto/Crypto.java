/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
 *
 * @author Quality of Service
 */
public class Crypto {

    private static final int KEYSIZE = 1024;

    public Crypto() {
    }

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
