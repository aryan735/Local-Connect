package com.localconnct.api.exception;

public class ServiceNotFoundException extends RuntimeException {
   public ServiceNotFoundException(String error){
        super(error);
    }
}
