package com.grananda.facecheckapi.exceptions;

public class FaceCheckException extends Exception {

    private final String message;

    public FaceCheckException(String message) {
        super(message);
        this.message = message;
    }

    public FaceCheckException(final String message, final Exception exception) {
        super(exception);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
