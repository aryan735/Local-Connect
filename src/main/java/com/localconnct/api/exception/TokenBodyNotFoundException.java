package com.localconnct.api.exception;

public class TokenBodyNotFoundException extends RuntimeException{
    public TokenBodyNotFoundException(String message){
        super(message);
    }
}
