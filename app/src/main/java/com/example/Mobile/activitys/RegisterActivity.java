package com.example.Mobile.activitys;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.Mobile.databinding.ActivityRegisterBinding;
import com.example.Mobile.viewmodels.RegisterViewModel;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        viewModel.getRegistrationStatus().observe(this, status -> {
            if (status != null) {
                Toast.makeText(this, status, Toast.LENGTH_LONG).show();
                if (status.contains("成功")) {
                    finish();
                }
            }
        });

        binding.cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.etPassword.setTransformationMethod(null);
                binding.etCheckPassword.setTransformationMethod(null);
            } else {
                binding.etPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                binding.etCheckPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            }
        });

        binding.btnRegister.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String name = binding.etName.getText().toString();
            String password = binding.etPassword.getText().toString();
            String checkPassword = binding.etCheckPassword.getText().toString();

            viewModel.registerUser(email, name, password, checkPassword);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
