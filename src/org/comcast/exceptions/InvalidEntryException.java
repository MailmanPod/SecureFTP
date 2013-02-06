package org.comcast.exceptions;

/**
 * Clase que representa una excepcion. <br> Esta excepcion se lanza en
 * situaciones en donde se quiere ingresar a una entrada no valida en la tabla
 * de hash, o se quiere eliminar o buscar una entrada que no existe.
 *
 * @author Federico Bruera TSB 2010.
 * @since Java 6
 */
public class InvalidEntryException extends GlobalException {

    public InvalidEntryException() {
        super();
    }

    public InvalidEntryException(String e) {
        super(e);
    }
}
