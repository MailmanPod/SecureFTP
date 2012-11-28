package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br>
 * Esta excepcion representa a la jerarquia de excepciones creadas por el
 * programador.
 * @author Federico Bruera TSB 2010.
 */
public class GlobalException extends Exception {

    public GlobalException() {
        super();
    }

    public GlobalException(String s) {
        super(s);
    }
}
