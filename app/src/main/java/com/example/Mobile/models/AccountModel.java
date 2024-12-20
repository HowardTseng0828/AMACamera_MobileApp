package com.example.Mobile.models;

public class AccountModel {

    private String name;
    private String gender;
    private String address;
    private String image;

    public AccountModel(String name, String gender, String address, String image) {
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public String getImage() {
        return image;
    }
}
