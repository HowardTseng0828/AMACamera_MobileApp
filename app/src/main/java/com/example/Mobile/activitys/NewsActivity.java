package com.example.Mobile.activitys;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Mobile.R;
import com.example.Mobile.models.NewsModel;

public class NewsActivity extends AppCompatActivity {

    private TextView txtTitle, txtContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        NewsModel newsModel = (NewsModel) getIntent().getSerializableExtra("newsModel");

        txtTitle = findViewById(R.id.txtTitle);
        txtContent = findViewById(R.id.txtContent);

        txtTitle.setText(newsModel.getTitle());
        txtContent.setText(newsModel.getContent());
    }
}