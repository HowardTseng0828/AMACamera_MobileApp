package com.example.Mobile.models;

public class ResPasswordModel {

    private String email;

    public ResPasswordModel(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isValidEmail() {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
