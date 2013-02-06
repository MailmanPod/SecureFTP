package org.comcast.exceptions;

/**
 * Este tipo de excepciones son lanzadas cuando existen datos incorrectos.
 *
 * @author Damian Bruera.
 * @version 1.0
 * @since Java 7
 */
public class InformationRequiredException extends GlobalException {

    public InformationRequiredException() {
        super();
    }

    public InformationRequiredException(String s) {
        super(s);
    }
}
