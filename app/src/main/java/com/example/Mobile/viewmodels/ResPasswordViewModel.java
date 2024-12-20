package com.example.Mobile.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.Mobile.models.ResPasswordModel;
import com.google.firebase.auth.FirebaseAuth;

public class ResPasswordViewModel extends AndroidViewModel {

    private final FirebaseAuth mAuth;
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public ResPasswordViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
    }

    public MutableLiveData<String> getSuccessMessage() {
        return successMessage;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void sendPasswordReset(ResPasswordModel request) {
        mAuth.sendPasswordResetEmail(request.getEmail())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        successMessage.setValue("已發送重置密碼郵件");
                    } else {
                        errorMessage.setValue("未查詢到此電子信箱");
                    }
                });
    }
}
