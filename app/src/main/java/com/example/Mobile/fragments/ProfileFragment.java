package com.example.Mobile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.example.Mobile.R;
import com.example.Mobile.activitys.AccountActivity;
import com.example.Mobile.activitys.HelpActivity;
import com.example.Mobile.activitys.LoginActivity;
import com.example.Mobile.databinding.FragmentProfileBinding;
import com.example.Mobile.viewmodels.ProfileViewModel;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentProfileBinding.inflate(inflater, container, false);


        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);


        profileViewModel.getUserName().observe(getViewLifecycleOwner(), name -> {
            binding.txtName.setText(name);
        });

        profileViewModel.getUserImage().observe(getViewLifecycleOwner(), image -> {
            Glide.with(getContext())
                    .load(image)
                    .placeholder(R.mipmap.ic_launcher_round)
                    .circleCrop()
                    .fitCenter()
                    .error(R.mipmap.ic_launcher_round)
                    .into(binding.profileImage);
        });


        profileViewModel.loadUserProfile();


        binding.profileImage.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AccountActivity.class));
        });

        binding.btnReminders.setOnClickListener(view -> {

            startActivity(new Intent(Intent.ACTION_EDIT)
                    .setType("vnd.android.cursor.item/event")
                    .putExtra("title", "藥物提醒")
                    .putExtra("allDay", true));
        });

        binding.btnHelp.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), HelpActivity.class));
        });

        binding.btnAccount.setOnClickListener(view -> {
            startActivity(new Intent(getActivity(), AccountActivity.class));
        });

        binding.btnLogout.setOnClickListener(view -> {
            profileViewModel.signOut();
            startActivity(new Intent(getActivity(), LoginActivity.class));
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        binding = null;
    }
}
