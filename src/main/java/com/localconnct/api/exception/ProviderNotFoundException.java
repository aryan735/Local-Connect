package com.localconnct.api.exception;



public class ProviderNotFoundException extends RuntimeException{
    public ProviderNotFoundException(String error){
        super(error);
    }
}
