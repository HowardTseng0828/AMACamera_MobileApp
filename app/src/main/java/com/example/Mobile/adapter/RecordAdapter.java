package com.example.Mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Mobile.R;
import com.example.Mobile.models.RecordModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class RecordAdapter extends FirebaseRecyclerAdapter<RecordModel, RecordAdapter.MyViewHolder> {

    public RecordAdapter(FirebaseRecyclerOptions<RecordModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull RecordModel recordModel) {
        holder.txtRecord.setText(recordModel.getTime());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_record, parent, false));
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView txtRecord;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txtRecord = view.findViewById(R.id.txtRecord);
        }
    }
}