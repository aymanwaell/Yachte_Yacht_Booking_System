package com.elsokhna.Yakhte.exception;

public class PhotoRetrievalException extends Throwable {
    public PhotoRetrievalException(String errorRetrievingPhoto) {
        super(errorRetrievingPhoto);
    }
}
