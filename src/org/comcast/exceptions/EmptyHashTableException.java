package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br>
 * Esta excepcion se lanza en situaciones en donde la tabla de hash queda sin ningun
 * elemento cargado y se quiere hacer alguna operacion sobre la tabla.
 * @author Federico Bruera TSB 2010.
 */
public class EmptyHashTableException extends GlobalException {

    public EmptyHashTableException() {
        super();
    }

    public EmptyHashTableException(String s) {
        super(s);
    }
}
