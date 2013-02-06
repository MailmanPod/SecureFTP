package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br> Esta excepcion se lanza en
 * situaciones en donde algun paramero de algun metodo es un objeto que tiene
 * una referencia nula.
 *
 * @author Federico Bruera TSB 2010.
 * @since Java 6
 */
public class NullObjectParameterException extends RuntimeException {

    public NullObjectParameterException() {
        super();
    }

    public NullObjectParameterException(String e) {
        super(e);
    }
}
