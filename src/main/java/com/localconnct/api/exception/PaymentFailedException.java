package com.localconnct.api.exception;

public class PaymentFailedException extends RuntimeException {
   public PaymentFailedException(String error){
        super(error);
    }
}
