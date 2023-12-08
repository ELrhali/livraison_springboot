package com.livraison.colis.exception;

public class ColisNotFoundException extends RuntimeException {
    public ColisNotFoundException(String message) {
        super(message);
    }
}