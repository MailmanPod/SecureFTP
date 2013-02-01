package org.comcast.exceptions;

/**
 * Esta excepcion se lanza cuando se intenta conecta al servidor, y este la
 * rechaza <br> dicha coneccion. Ademas, los objetos de esta clase contienen
 * datos sobre las <br> causas del porque el servidor rechazo la coneccion.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since 2012
 */
public class FTPConectionRefusedException extends GlobalException {

    public FTPConectionRefusedException() {
        super();
    }

    public FTPConectionRefusedException(String s) {
        super(s);
    }
}
