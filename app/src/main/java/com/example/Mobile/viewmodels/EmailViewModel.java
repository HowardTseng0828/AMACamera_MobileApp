package com.example.Mobile.viewmodels;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmailViewModel extends ViewModel {

    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void sendEmail(Context context, String message) {
        if (TextUtils.isEmpty(message)) {
            errorMessage.setValue("請輸入您遇到的問題");
            return;
        }

        Intent emailIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "AMACameraCustomerService@gmail.com"));
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "AMA Camera 問題求助");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);

        if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(emailIntent);
        } else {
            errorMessage.setValue("無法打開郵件應用");
        }
    }
}
