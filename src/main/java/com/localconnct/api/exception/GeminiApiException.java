package com.localconnct.api.exception;

public class GeminiApiException extends RuntimeException{
    public GeminiApiException(String error,Exception e){
        super(error,e);
    }
}
