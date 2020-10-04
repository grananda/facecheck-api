package com.grananda.facecheckapi.exceptions;

public class UnknownCollectionException extends FaceCheckException {
    public UnknownCollectionException(final String message) {
        super(message);
    }

    public UnknownCollectionException(final String message, Exception exception) {
        super(message, exception);
    }
}
