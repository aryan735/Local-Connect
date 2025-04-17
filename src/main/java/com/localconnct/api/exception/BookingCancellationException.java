package com.localconnct.api.exception;

public class BookingCancellationException extends RuntimeException{
    public BookingCancellationException(String error){
        super(error);
    }
}
