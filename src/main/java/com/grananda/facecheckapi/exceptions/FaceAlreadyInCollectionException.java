package com.grananda.facecheckapi.exceptions;

public class FaceAlreadyInCollectionException extends FaceCheckException {
    public FaceAlreadyInCollectionException(final String message) {
        super(message);
    }

    public FaceAlreadyInCollectionException(final String message, Exception exception) {
        super(message, exception);
    }
}
