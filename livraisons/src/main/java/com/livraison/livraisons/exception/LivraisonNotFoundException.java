package com.livraison.livraisons.exception;
public class LivraisonNotFoundException extends RuntimeException {
    public LivraisonNotFoundException(String message) {
        super(message);
    }
}