package com.localconnct.api.exception;

public class UnauthorizedAccessException extends RuntimeException {
   public UnauthorizedAccessException(String error){
        super(error);
    }
}
