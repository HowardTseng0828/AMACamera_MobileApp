package com.example.Mobile.viewmodels;

import android.app.Application;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.Mobile.models.RegisterModel;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterViewModel extends AndroidViewModel {

    private final FirebaseAuth mAuth;
    private final FirebaseDatabase mDatabase;

    private final MutableLiveData<String> registrationStatus = new MutableLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public LiveData<String> getRegistrationStatus() {
        return registrationStatus;
    }

    public void registerUser(String email, String name, String password, String checkPassword) {
        if (TextUtils.isEmpty(email)) {
            registrationStatus.setValue("未輸入電子信箱");
            return;
        }
        if (TextUtils.isEmpty(name)) {
            registrationStatus.setValue("未輸入姓名");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            registrationStatus.setValue("未輸入密碼");
            return;
        }
        if (TextUtils.isEmpty(checkPassword)) {
            registrationStatus.setValue("未輸入確認密碼");
            return;
        }
        if (!password.equals(checkPassword)) {
            registrationStatus.setValue("確認密碼與密碼不相符");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                AuthResult result = task.getResult();
                if (result != null && result.getUser() != null) {
                    String uid = result.getUser().getUid();

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build();

                    result.getUser().updateProfile(profileUpdates).addOnCompleteListener(updateTask -> {
                        if (updateTask.isSuccessful()) {
                            saveUserToDatabase(uid, email, name);
                            registrationStatus.setValue("註冊成功，請先驗證電子信箱");
                            result.getUser().sendEmailVerification();
                        }
                    });
                }
            } else {
                handleFirebaseError(task.getException());
            }
        });
    }

    private void saveUserToDatabase(String uid, String email, String name) {
        mDatabase.getReference("Users").child(uid).setValue(new RegisterModel(name, email, uid));
    }

    private void handleFirebaseError(Exception exception) {
        if (exception instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) exception).getErrorCode();
            switch (errorCode) {
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    registrationStatus.setValue("該電子郵件地址已被另一個帳戶使用");
                    break;
                case "ERROR_WEAK_PASSWORD":
                    registrationStatus.setValue("密碼無效，必須至少6個字符");
                    break;
                default:
                    registrationStatus.setValue("註冊失敗：" + exception.getMessage());
                    break;
            }
        } else {
            registrationStatus.setValue("未知錯誤：" + exception.getMessage());
        }
    }
}
