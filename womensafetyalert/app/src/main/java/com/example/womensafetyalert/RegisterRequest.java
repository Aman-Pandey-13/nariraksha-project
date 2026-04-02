package com.example.womensafetyalert;

public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String contact1;
    private String contact2;
    private String contact3;

    public RegisterRequest(String name, String email, String password,
                           String phone,
                           String contact1, String contact2, String contact3) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.contact1 = contact1;
        this.contact2 = contact2;
        this.contact3 = contact3;
    }
}