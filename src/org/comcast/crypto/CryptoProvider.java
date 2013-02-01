package org.comcast.crypto;

/**
 * Metodo que representa la aplicacion del patron Singleton.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since 2012.
 */
public class CryptoProvider {

    private static Crypto instance = null;

    /**
     * Constructor privada de la clase. Para evitar que se creen mas de un
     * objeto.
     */
    private CryptoProvider() {
    }

    /**
     * Metodo que crea un solo objeto de la clase.
     *
     * @return Crypto El unico objeto que se crea.
     */
    public static Crypto getInstance() {
        if (instance == null) {
            instance = new Crypto();
        }

        return instance;
    }
}
