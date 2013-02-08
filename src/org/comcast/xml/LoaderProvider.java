package org.comcast.xml;

/**
 * Clase que representa la implementacion del patron Singleton.
 *
 * @author Damian Bruera
 * @since Java 6
 * @version 1.0
 */
public class LoaderProvider {

    private static Loader instance = null;

    private LoaderProvider() {
    }

    public static Loader getInstance() {
        if (instance == null) {
            instance = new Loader();
        }

        return instance;
    }
}
