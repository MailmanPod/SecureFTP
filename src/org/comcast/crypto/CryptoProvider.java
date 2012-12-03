/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.comcast.crypto;

/**
 *
 * @author Quality of Service
 */
public class CryptoProvider {

    private static Crypto instance = null;

    private CryptoProvider() {
    }

    public static Crypto getInstance() {
        if (instance == null) {
            instance = new Crypto();
        }

        return instance;
    }
}
