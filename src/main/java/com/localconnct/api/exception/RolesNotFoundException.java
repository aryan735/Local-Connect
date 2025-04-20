package com.localconnct.api.exception;

public class RolesNotFoundException extends RuntimeException{
    public RolesNotFoundException(String error){
        super(error);
    }
}
