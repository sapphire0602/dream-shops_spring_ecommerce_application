package com.firstspringtutorial.dreamshops.exceptions;

public class productNotFoundException extends RuntimeException {
    public productNotFoundException(String message) {
        super(message);
    }
}
