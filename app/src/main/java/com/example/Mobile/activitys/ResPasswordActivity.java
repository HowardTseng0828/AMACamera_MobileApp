package com.example.Mobile.activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.Mobile.databinding.ActivityRespasswordBinding;
import com.example.Mobile.models.ResPasswordModel;
import com.example.Mobile.viewmodels.ResPasswordViewModel;

public class ResPasswordActivity extends AppCompatActivity {

    private ActivityRespasswordBinding binding;
    private ResPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityRespasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(ResPasswordViewModel.class);

        binding.btnSend.setOnClickListener(view -> sendPasswordReset());

        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getSuccessMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            finish();
        });

        viewModel.getErrorMessage().observe(this, message -> {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        });
    }

    private void sendPasswordReset() {
        String email = binding.etEmail.getText().toString().trim();
        ResPasswordModel request = new ResPasswordModel(email);

        if (!request.isValidEmail()) {
            binding.etEmail.setError("請輸入有效的電子信箱");
            binding.etEmail.requestFocus();
        } else {
            viewModel.sendPasswordReset(request);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
