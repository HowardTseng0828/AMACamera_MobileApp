package com.example.Mobile.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Mobile.R;
import com.example.Mobile.models.HelpModel;

public class QnaActivity extends AppCompatActivity {

    private TextView txtTitle, txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qna);

        HelpModel helpModel = (HelpModel) getIntent().getSerializableExtra("helpModel");

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);

        txtTitle.setText(helpModel.getTitle());
        txtContent.setText(helpModel.getContent());
    }
}