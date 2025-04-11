package com.localconnct.api.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OtpData {
    private String otp;
    private LocalDateTime expiryTime;

    public OtpData(String otp, LocalDateTime expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }
}
