package com.example.womensafetyalert;

public class OtpManager {

    public static String currentOtp;

    public static String generateOtp() {
        currentOtp = String.valueOf((int)(Math.random() * 900000) + 100000);
        return currentOtp;
    }
}