package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br>
 * Esta excepcion se lanza en situaciones en donde la lista simple queda sin ningun
 * elemento cargado y se quiere hacer alguna operacion sobre la lista.
 * @author Federico Bruera TSB 2010.
 */
public class EmptyListException extends GlobalException {

    public EmptyListException() {
        super();
    }

    public EmptyListException(String e) {
        super(e);
    }
}
