package com.example.adminzerdeapp.modules;

public class Users {
    String fullName, email, phone, password, ruqsat, userId;

    public Users(){}

    public Users(String fullName, String email, String phone, String password, String ruqsat, String userId) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.ruqsat = ruqsat;
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRuqsat() {
        return ruqsat;
    }

    public void setRuqsat(String ruqsat) {
        this.ruqsat = ruqsat;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
