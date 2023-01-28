package com.example.builday365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

        Toolbar toolbar = findViewById (R.id.toolbar);
        setSupportActionBar (toolbar);

        Intent intent = getIntent();
        String google_nickname = intent.getStringExtra("google_nickname");
        String google_photo = intent.getStringExtra("google_photo");

        tv_google_nickname = findViewById(R.id.main_tv_google_nickname);
        tv_google_nickname.setText(google_nickname);

        iv_google_photo = findViewById(R.id.main_iv_google_photo);
        Glide.with(this).load(google_photo).into(iv_google_photo);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId ()) {
            case R.id.appbar_top_ib_side_menu:
                return true;
            case R.id.appbar_top_ib_month_prev:
                return true;
            case R.id.appbar_top_ib_day_prev:
                return true;
            case R.id.appbar_top_tv_cur_date:
                return true;
            case R.id.appbar_top_ib_day_next:
                return true;
            case R.id.appbar_top_ib_month_next:
                return true;
            case R.id.appbar_top_ib_calendar:
                return true;
            default:
                return super.onOptionsItemSelected (item);
        }
    }
}