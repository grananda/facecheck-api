package com.grananda.facecheckapi.exceptions;

public class FaceNotInCollectionException extends FaceCheckException {

    public FaceNotInCollectionException(final String message) {
        super(message);
    }

    public FaceNotInCollectionException(final String message, Exception exception) {
        super(message, exception);
    }
}
