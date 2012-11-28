package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br>
 * Esta excepcion se lanza en situaciones en donde el puntero que va recorriendo
 * una lista, apunta mas alla del final de la lista; o incluso cuando se pasa un indice
 * como parametro y este es negativo.
 * @author Federico Bruera TSB 2010.
 */
public class ListPointerOutOfBoundsException extends GlobalException {

    public ListPointerOutOfBoundsException() {
        super();
    }

    public ListPointerOutOfBoundsException(String e) {
        super(e);
    }
}
