package com.example.womensafetyalert;

public class UserResponse {

    private String name;
    private String email;
    private String phone;
    private String contact1;
    private String contact2;
    private String contact3;

    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setContact1(String contact1) { this.contact1 = contact1; }
    public void setContact2(String contact2) { this.contact2 = contact2; }
    public void setContact3(String contact3) { this.contact3 = contact3; }

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getContact1() { return contact1; }
    public String getContact2() { return contact2; }
    public String getContact3() { return contact3; }
}