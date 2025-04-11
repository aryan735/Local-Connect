package com.localconnct.api.exception;

public class RatingNotFoundException extends RuntimeException{
    public RatingNotFoundException(String error) {
        super(error);
    }
}
