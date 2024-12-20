package com.example.Mobile.activitys;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Mobile.adapter.HelpAdapter;
import com.example.Mobile.databinding.ActivityHelpBinding;
import com.example.Mobile.models.HelpModel;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

public class HelpActivity extends AppCompatActivity {

    private HelpAdapter helpAdapter;
    private FirebaseDatabase mDatabase;
    private ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHelpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mDatabase = FirebaseDatabase.getInstance();

        binding.search.setIconified(true);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        binding.search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        binding.search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return false;
            }
        });

        binding.btnEmail.setOnClickListener(v -> startActivity(new Intent(HelpActivity.this, EmailActivity.class)));

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<HelpModel> options = new FirebaseRecyclerOptions.Builder<HelpModel>()
                .setQuery(mDatabase.getReference("Q&A"), HelpModel.class)
                .build();

        helpAdapter = new HelpAdapter(options);
        binding.recyclerview.setAdapter(helpAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (helpAdapter != null) {
            helpAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (helpAdapter != null) {
            helpAdapter.stopListening();
        }
    }

    private void search(String query) {
        FirebaseRecyclerOptions<HelpModel> options = new FirebaseRecyclerOptions.Builder<HelpModel>()
                .setQuery(mDatabase.getReference("Q&A").orderByChild("title").startAt(query).endAt(query + "\uf8ff"), HelpModel.class)
                .build();

        helpAdapter.updateOptions(options);
        helpAdapter.startListening();
        binding.recyclerview.setAdapter(helpAdapter);
    }

}
