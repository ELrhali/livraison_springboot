package com.livraison.livreurs.exception;


public class LivreurNotFoundException  extends RuntimeException {
    public LivreurNotFoundException(String message) {
        super(message);
    }
}