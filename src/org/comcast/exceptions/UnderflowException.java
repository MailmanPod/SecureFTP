package org.comcast.exceptions;

/**
 * Exception class for access in empty containers such as stacks, queues, and
 * priority queues.
 *
 * @author Mark Allen Weiss
 */
public class UnderflowException extends GlobalException {

    public UnderflowException() {
        super();
    }

    public UnderflowException(String s) {
        super(s);
    }
}
