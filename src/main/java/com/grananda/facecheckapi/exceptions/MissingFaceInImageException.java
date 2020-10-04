package com.grananda.facecheckapi.exceptions;

public class MissingFaceInImageException extends FaceCheckException {

    public MissingFaceInImageException(final String message) {
        super(message);
    }

    public MissingFaceInImageException(final String message, Exception exception) {
        super(message, exception);
    }
}
