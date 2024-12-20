package com.example.Mobile.models;

public class RegisterModel {

    private String name;
    private String email;
    private String uid;
    private String gender;
    private String address;
    private String image;
    private String isAdmin;

    public RegisterModel(String name, String email, String uid) {
        this.name = name;
        this.email = email;
        this.uid = uid;
        this.gender = "未選擇";
        this.address = "";
        this.image = "";
        this.isAdmin = "0";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
