package org.comcast.exceptions;

/**
 * Esta excepcion es lanzada cuando se intenta aceder a contenedores que se
 * encuentran <br> vacios, como por ejemplo colas, colas de prioridad o pilas.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since 2012.
 */
public class UnderflowException extends GlobalException {

    public UnderflowException() {
        super();
    }

    public UnderflowException(String s) {
        super(s);
    }
}
