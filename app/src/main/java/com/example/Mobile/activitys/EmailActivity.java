package com.example.Mobile.activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Mobile.databinding.ActivityEmailBinding;
import com.example.Mobile.viewmodels.EmailViewModel;

public class EmailActivity extends AppCompatActivity {

    private ActivityEmailBinding binding;
    private final EmailViewModel emailViewModel = new EmailViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityEmailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        emailViewModel.getErrorMessage().observe(this, errorMessage -> {
            if (errorMessage != null) {
                Toast.makeText(EmailActivity.this, errorMessage, Toast.LENGTH_LONG).show();
            }
        });

        binding.btnSubmit.setOnClickListener(v -> {
            String message = binding.etDescribe.getText().toString();
            emailViewModel.sendEmail(EmailActivity.this, message);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
