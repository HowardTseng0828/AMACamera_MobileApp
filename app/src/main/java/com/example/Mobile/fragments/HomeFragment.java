package com.example.Mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.Mobile.activitys.HelpActivity;
import com.example.Mobile.activitys.RecordActivity;
import com.example.Mobile.adapter.NewsAdapter;
import com.example.Mobile.databinding.FragmentHomeBinding;
import com.example.Mobile.viewmodels.HomeViewModel;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private NewsAdapter newsAdapter;
    private HomeViewModel homeViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);

        binding.recyclerview.setHasFixedSize(true);
        binding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        homeViewModel.getNewsOptions().observe(getViewLifecycleOwner(), options -> {
            if (newsAdapter == null) {
                newsAdapter = new NewsAdapter(options);
                binding.recyclerview.setAdapter(newsAdapter);
            } else {
                newsAdapter.updateOptions(options);
            }
        });

        binding.btnRecord.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), RecordActivity.class));
        });

        binding.btnHelp.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), HelpActivity.class));
        });

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (newsAdapter != null) {
            newsAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (newsAdapter != null) {
            newsAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
