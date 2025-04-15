package com.localconnct.api.enums;

public enum BookingStatus {
    REQUESTED,   // User has sent the request, pending provider response
    ACCEPTED,
    CONFIRMED,   // Provider accepted
    REJECTED,    // Provider rejected
    CANCELLED,   // User or provider cancelled
    COMPLETED    // Provider marked job as done
}

