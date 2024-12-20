    package com.example.Mobile.activitys;

    import android.content.Intent;
    import android.net.Uri;
    import android.os.Bundle;
    import android.widget.Toast;

    import androidx.annotation.Nullable;
    import androidx.appcompat.app.AppCompatActivity;
    import androidx.lifecycle.MutableLiveData;
    import androidx.lifecycle.ViewModelProvider;

    import com.bumptech.glide.Glide;
    import com.example.Mobile.R;
    import com.example.Mobile.databinding.ActivityAccountBinding;
    import com.example.Mobile.models.AccountModel;
    import com.example.Mobile.viewmodels.AccountViewModel;
    import com.google.firebase.auth.FirebaseAuth;
    import com.theartofdev.edmodo.cropper.CropImage;

    public class AccountActivity extends AppCompatActivity {

        private ActivityAccountBinding binding;
        private AccountViewModel accountViewModel;
        private Uri imageUri;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            binding = ActivityAccountBinding.inflate(getLayoutInflater());
            setContentView(binding.getRoot());

            accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);

            binding.updateProfile.setOnClickListener(view -> {
                CropImage.activity().setAspectRatio(1, 1).start(AccountActivity.this);
            });

            binding.btnUpdate.setOnClickListener(view -> {
                String name = binding.etName.getText().toString();
                String sex = binding.spSex.getSelectedItem().toString();
                String address = binding.etAddress.getText().toString();
                accountViewModel.updateUserProfile(name, sex, address, imageUri, this);
            });

            accountViewModel.getErrorMessage().observe(this, errorMessage -> {
                if (errorMessage != null) {
                    Toast.makeText(AccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });

            accountViewModel.isProfileUpdated().observe(this, isUpdated -> {
                if (isUpdated != null && isUpdated) {
                    Toast.makeText(AccountActivity.this, "更新資料成功", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AccountActivity.this, MenuActivity.class));
                    finish();
                }
            });

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            MutableLiveData<AccountModel> userLiveData = new MutableLiveData<>();

            accountViewModel.getUserInfo(userId, userLiveData);

            userLiveData.observe(this, accountModel -> {
                if (accountModel != null) {
                    binding.etName.setText(accountModel.getName());
                    binding.etAddress.setText(accountModel.getAddress());
                    binding.txtEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                    Glide.with(AccountActivity.this)
                            .load(accountModel.getImage())
                            .placeholder(R.mipmap.ic_launcher_round)
                            .circleCrop()
                            .fitCenter()
                            .error(R.mipmap.ic_launcher_round)
                            .into(binding.updateProfile);

                    if ("生理男".equals(accountModel.getGender())) {
                        binding.spSex.setSelection(1);
                    } else if ("生理女".equals(accountModel.getGender())) {
                        binding.spSex.setSelection(2);
                    } else {
                        binding.spSex.setSelection(0);
                    }
                }
            });
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                imageUri = result.getUri();
                binding.updateProfile.setImageURI(imageUri);
            } else {
                Toast.makeText(AccountActivity.this, "發生錯誤，請稍後再試", Toast.LENGTH_SHORT).show();
            }
        }
    }



