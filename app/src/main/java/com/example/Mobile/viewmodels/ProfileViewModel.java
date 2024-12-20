package com.example.Mobile.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private MutableLiveData<String> userName = new MutableLiveData<>();
    private MutableLiveData<String> userImage = new MutableLiveData<>();

    public ProfileViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserImage() {
        return userImage;
    }

    public void loadUserProfile() {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.getReference("Users").child(userId).get().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult().exists()) {
                DataSnapshot dataSnapshot = task.getResult();
                String name = String.valueOf(dataSnapshot.child("name").getValue());
                String image = String.valueOf(dataSnapshot.child("image").getValue());

                userName.setValue(name);
                userImage.setValue(image);
            }
        });
    }

    public void signOut() {
        mAuth.signOut();
    }
}
