package com.example.parceltrackingapi.exception;

public class ParcelAlreadyExistsException extends RuntimeException {
    public ParcelAlreadyExistsException(String message) {
        super(message);
    }
}
