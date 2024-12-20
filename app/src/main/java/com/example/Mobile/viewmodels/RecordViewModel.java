package com.example.Mobile.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.Mobile.models.RecordModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RecordViewModel extends ViewModel {

    private final FirebaseAuth mAuth;
    private final FirebaseDatabase mDatabase;
    private final MutableLiveData<FirebaseRecyclerOptions<RecordModel>> recordsLiveData;

    public RecordViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        recordsLiveData = new MutableLiveData<>();
        loadRecordsData();
    }

    public LiveData<FirebaseRecyclerOptions<RecordModel>> getRecordsLiveData() {
        return recordsLiveData;
    }

    private void loadRecordsData() {
        String userId = mAuth.getCurrentUser().getUid();
        FirebaseRecyclerOptions<RecordModel> options =
                new FirebaseRecyclerOptions.Builder<RecordModel>()
                        .setQuery(mDatabase.getReference("Records").child(userId).orderByChild("time"), RecordModel.class)
                        .build();
        recordsLiveData.setValue(options);
    }

    public void setDelayer(String dateTime) {
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.getReference("Devices").child(userId).child("delayer").setValue(dateTime);
    }
}
