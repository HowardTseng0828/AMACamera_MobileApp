package com.example.Mobile.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Mobile.models.NewsModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomeViewModel extends ViewModel {

    private final FirebaseAuth mAuth;
    private final FirebaseDatabase mDatabase;
    private final MutableLiveData<FirebaseRecyclerOptions<NewsModel>> newsOptionsLiveData;

    public HomeViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        newsOptionsLiveData = new MutableLiveData<>();
        loadNewsData();
        registerDeviceToken();
    }

    public LiveData<FirebaseRecyclerOptions<NewsModel>> getNewsOptions() {
        return newsOptionsLiveData;
    }

    private void loadNewsData() {
        FirebaseRecyclerOptions<NewsModel> options =
                new FirebaseRecyclerOptions.Builder<NewsModel>()
                        .setQuery(mDatabase.getReference("News"), NewsModel.class)
                        .build();
        newsOptionsLiveData.setValue(options);
    }

    private void registerDeviceToken() {
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();
                        String token = task.getResult();
                        mDatabase.getReference("Devices").child(userId).child("token").setValue(token);
                    }
                });
    }
}
