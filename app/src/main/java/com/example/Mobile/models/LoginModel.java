package com.example.Mobile.models;

public class LoginModel {
    private final boolean success;
    private final String errorMessage;

    public LoginModel(boolean success, String errorMessage) {
        this.success = success;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
