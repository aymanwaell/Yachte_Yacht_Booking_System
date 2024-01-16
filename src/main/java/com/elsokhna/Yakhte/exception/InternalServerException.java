package com.elsokhna.Yakhte.exception;

public class InternalServerException extends RuntimeException {
    public InternalServerException(String errorUpdatingYacht) {
        super(errorUpdatingYacht);
    }
}
