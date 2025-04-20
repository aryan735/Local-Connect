package com.localconnct.api.exception;



public class EmailNotFoundException extends RuntimeException{
    public EmailNotFoundException(String error){
        super(error);
    }
}
