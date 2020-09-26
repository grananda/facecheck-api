package com.grananda.facecheckapi.exceptions;

public class MultipleFacesInImageException extends FaceCheckException {

    public MultipleFacesInImageException(final String message) {
        super(message);
    }

    public MultipleFacesInImageException(final String message, Exception exception) {
        super(message, exception);
    }
}
