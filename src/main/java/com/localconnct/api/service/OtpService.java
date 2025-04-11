package com.localconnct.api.service;

import com.localconnct.api.dto.OtpData;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Data
@Service
public class OtpService {

    private Map<String, OtpData> otpStorage = new HashMap<>();

    public String generateOtp(String email){
        String otp = String.valueOf(100000+ new Random().nextInt(9000000));
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        otpStorage.put(email,new OtpData(otp,expiryTime));
        return otp;
    }

    public boolean validateOtp(String email, String otpFromUser){
        OtpData date = otpStorage.get(email);
        if (date!=null){
            return date.getOtp().equals(otpFromUser)&& LocalDateTime.now().isBefore(date.getExpiryTime());
        }
        return false;
    }
}
