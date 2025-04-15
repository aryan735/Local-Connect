package com.localconnct.api.exception;

public class BookingNotFoundException extends RuntimeException {
   public BookingNotFoundException(String error){
        super(error);
    }
}
