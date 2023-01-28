package com.example.builday365;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MainActivity extends AppCompatActivity {
    private TextView tv_google_nickname;
    private ImageView iv_google_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String google_nickname = intent.getStringExtra("google_nickname");
        String google_photo = intent.getStringExtra("google_photo");

        tv_google_nickname = findViewById(R.id.main_tv_google_nickname);
        tv_google_nickname.setText(google_nickname);

        iv_google_photo = findViewById(R.id.main_iv_google_photo);
        Glide.with(this).load(google_photo).into(iv_google_photo);

    }
}