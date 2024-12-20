package com.example.Mobile.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Mobile.R;
import com.example.Mobile.activitys.QnaActivity;
import com.example.Mobile.models.HelpModel;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HelpAdapter extends FirebaseRecyclerAdapter<HelpModel, HelpAdapter.MyViewHolder> {

    public HelpAdapter(FirebaseRecyclerOptions<HelpModel> options) {
        super(options);
    }

    @NonNull
    @Override
    public HelpAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new HelpAdapter.MyViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerview_help, parent, false)
        );
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull HelpModel helpModel) {
        holder.txtTitle.setText(helpModel.getTitle());

        holder.itemView.setOnClickListener(view -> view.getContext().startActivity(new Intent(view.getContext(), QnaActivity.class)
                .putExtra("helpModel", helpModel)
        ));
    }

    public void updateOptions(FirebaseRecyclerOptions<HelpModel> options) {
        super.updateOptions(options);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout btnHelp;
        private TextView txtTitle;

        public MyViewHolder(@NonNull View view) {
            super(view);
            txtTitle = view.findViewById(R.id.txtTitle);
            btnHelp = view.findViewById(R.id.btnHelp);
        }
    }
}
