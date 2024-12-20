package com.example.Mobile.viewmodels;

import android.content.Context;
import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Mobile.models.AccountModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class AccountViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isProfileUpdated = new MutableLiveData<>();

    public AccountViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<Boolean> isProfileUpdated() {
        return isProfileUpdated;
    }

    public void updateUserProfile(String name, String sex, String address, Uri imageUri, Context context) {
        if (name.isEmpty()) {
            errorMessage.setValue("未輸入姓名");
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        mAuth.getCurrentUser().updateProfile(profileUpdates)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (imageUri != null) {
                            uploadProfileImage(imageUri, userId);
                        }
                        updateUserInfoInDatabase(userId, name, sex, address);
                    } else {
                        errorMessage.setValue("更新資料失敗");
                    }
                });
    }

    private void uploadProfileImage(Uri imageUri, String userId) {
        StorageReference storageProfilePicsRef = FirebaseStorage.getInstance().getReference().child(userId).child("image");
        storageProfilePicsRef.putFile(imageUri)
                .continueWithTask(task -> storageProfilePicsRef.getDownloadUrl())
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String downloadUri  = task.getResult().toString();
                        mDatabase.getReference("Users").child(userId).child("image").setValue(downloadUri);
                    }
                });
    }

    private void updateUserInfoInDatabase(String userId, String name, String sex, String address) {
        mDatabase.getReference("Users").child(userId).child("name").setValue(name);
        mDatabase.getReference("Users").child(userId).child("gender").setValue(sex);
        mDatabase.getReference("Users").child(userId).child("address").setValue(address);
        isProfileUpdated.setValue(true);
    }

    public void getUserInfo(String userId, MutableLiveData<AccountModel> userLiveData) {
        mDatabase.getReference("Users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DataSnapshot dataSnapshot = task.getResult();
                String image = String.valueOf(dataSnapshot.child("image").getValue());
                String sex = String.valueOf(dataSnapshot.child("gender").getValue());
                String address = String.valueOf(dataSnapshot.child("address").getValue());
                String name = String.valueOf(dataSnapshot.child("name").getValue());

                AccountModel accountModel = new AccountModel(name, sex, address, image);
                userLiveData.setValue(accountModel);
            }
        });
    }
}