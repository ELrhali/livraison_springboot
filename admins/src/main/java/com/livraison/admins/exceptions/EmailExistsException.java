package com.livraison.admins.exceptions;

public class EmailExistsException extends RuntimeException {

    public EmailExistsException(String message) {
        super(message);
    }
}