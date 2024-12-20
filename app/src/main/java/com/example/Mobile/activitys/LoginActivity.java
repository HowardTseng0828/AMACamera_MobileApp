package com.example.Mobile.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import com.example.Mobile.databinding.ActivityLoginBinding;
import com.example.Mobile.models.LoginModel;
import com.example.Mobile.viewmodels.LoginViewModel;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "AMA CAMERA";
    private static final int SPLASH_TIME_OUT = 10;
    private ActivityLoginBinding binding;

    private final LoginViewModel loginViewModel = new LoginViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences sharedPreferences = getSharedPreferences(PREFS_NAME, 0);
        boolean hasLoggedIn = sharedPreferences.getBoolean("hasLoggedIn", false);

        new Handler().postDelayed(() -> {
            if (FirebaseAuth.getInstance().getCurrentUser() != null && hasLoggedIn) {
                startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                finish();
            }
        }, SPLASH_TIME_OUT);

        binding.cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                binding.etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            } else {
                binding.etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });

        binding.txtResPassword.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, ResPasswordActivity.class)));

        binding.txtRegister.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginViewModel.getLoginResult().observe(this, new Observer<LoginModel>() {
            @Override
            public void onChanged(LoginModel loginModel) {
                if (loginModel.isSuccess()) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("hasLoggedIn", true);
                    editor.apply();
                    Toast.makeText(LoginActivity.this, "登入成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MenuActivity.class));
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, loginModel.getErrorMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });

        binding.btnLogin.setOnClickListener(view -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();
            loginViewModel.loginUser(email, password);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}