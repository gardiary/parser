package com.ef.exception;

/**
 * Created by gardiary on 02/04/18.
 */
public class ParserException extends Exception {

    public ParserException() {
        super();
    }

    public ParserException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ParserException(final String message) {
        super(message);
    }

    public ParserException(final Throwable cause) {
        super(cause);
    }

}
