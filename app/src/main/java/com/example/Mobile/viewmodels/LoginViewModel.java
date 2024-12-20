package com.example.Mobile.viewmodels;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Mobile.models.LoginModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class LoginViewModel extends ViewModel {

    private final MutableLiveData<LoginModel> loginModel = new MutableLiveData<>();
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();

    public LiveData<LoginModel> getLoginResult() {
        return loginModel;
    }

    public void loginUser(String email, String password) {
        if (TextUtils.isEmpty(email)) {
            loginModel.setValue(new LoginModel(false, "請輸入電子信箱"));
            return;
        }
        if (TextUtils.isEmpty(password)) {
            loginModel.setValue(new LoginModel(false, "請輸入密碼"));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (mAuth.getCurrentUser().isEmailVerified()) {
                    String userId = mAuth.getCurrentUser().getUid();
                    FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tokenTask -> {
                        if (tokenTask.isSuccessful()) {
                            String token = tokenTask.getResult();
                            mDatabase.getReference("Devices").child(userId).child("token").setValue(token);
                        }
                    });
                    loginModel.setValue(new LoginModel(true, "登入成功"));
                } else {
                    mAuth.getCurrentUser().sendEmailVerification();
                    loginModel.setValue(new LoginModel(false, "尚未驗證電子信箱"));
                }
            } else {
                String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();
                handleLoginError(errorCode);
            }
        });
    }

    private void handleLoginError(String errorCode) {
        String errorMessage = "登入失敗";
        switch (errorCode) {
            case "ERROR_INVALID_EMAIL":
                errorMessage = "電子郵件格式錯誤";
                break;
            case "ERROR_WRONG_PASSWORD":
                errorMessage = "密碼錯誤";
                break;
        }
        loginModel.setValue(new LoginModel(false, errorMessage));
    }
}
