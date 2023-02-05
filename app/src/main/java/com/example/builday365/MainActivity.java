package com.example.builday365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView toolbar_tv_cur_date, tv_google_name;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_side_menu, ibtn_add_section;
    DrawerLayout drawerLayout;
    ConstraintLayout timeBarLayout;
    ImageView iv_google_photo;

    NavigationView navigationView;
    View headerView;

    int set_year, set_month, set_day;
    Calendar calendar;
    CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_tv_cur_date = (TextView)findViewById(R.id.main_toolbar_tv_cur_date);
        toolbar_tv_cur_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        calendar = Calendar.getInstance();
        set_date(new Date(calendar.getTimeInMillis()));

        timeBarLayout = (ConstraintLayout)findViewById(R.id.main_layout_timebar);
        ibtn_calendar = (ImageButton)findViewById(R.id.main_toolbar_ibtn_calendar);
        calendarView = (CalendarView)findViewById(R.id.main_calendarview);
        calendarView.setVisibility(View.GONE);
        ibtn_calendar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int cur_visibilty = calendarView.getVisibility();
                 calendarView.setVisibility((cur_visibilty==View.VISIBLE)? View.GONE : View.VISIBLE);
                 timeBarLayout.setVisibility((cur_visibilty==View.VISIBLE)? View.VISIBLE : View.GONE);

                 if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
                     drawerLayout.closeDrawers();
                 }

                 String date_str = String.format("%d-%d-%d", set_year, set_month, set_day);
                 SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

                 try {
                     Date date = transFormat.parse(date_str);
                     calendarView.setDate(date.getTime());
                 } catch (ParseException e) {
                     e.printStackTrace();
                 }
             }
         });

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                toolbar_tv_cur_date.setText(String.format("%d.%02d.%02d", year, month+1, day));

                set_year = year;
                set_month = month+1;
                set_day = day;

                String date_str = String.format("%d-%d-%d", set_year, set_month, set_day);
                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");

                try {
                    Date date = transFormat.parse(date_str);
                    calendar.setTime(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendarView.setVisibility(View.GONE);
                timeBarLayout.setVisibility(View.VISIBLE);
            }
        });

        ibtn_day_prev = (ImageButton)findViewById(R.id.main_toolbar_ibtn_day_prev);
        ibtn_day_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        ibtn_day_next = (ImageButton)findViewById(R.id.main_toolbar_ibtn_day_next);
        ibtn_day_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        ibtn_month_prev = (ImageButton)findViewById(R.id.main_toolbar_ibtn_month_prev);
        ibtn_month_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        ibtn_month_next = (ImageButton)findViewById(R.id.main_toolbar_ibtn_month_next);
        ibtn_month_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                }
            }
        });

        ibtn_side_menu = (ImageButton)findViewById(R.id.main_toolbar_ibtn_side_menu);
        drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        ibtn_side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.openDrawer(Gravity.LEFT);
                    timeBarLayout.setVisibility(View.GONE);

                    if (calendarView.getVisibility() == View.VISIBLE) {
                        calendarView.setVisibility(View.GONE);
                    }
                }
                else {
                    drawerLayout.closeDrawers();
                    timeBarLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        Intent intent = getIntent();
        String google_photo = intent.getStringExtra("google_photo");
        String google_name = intent.getStringExtra("google_name");

        navigationView = (NavigationView)findViewById(R.id.main_navigationView);
        headerView = navigationView.getHeaderView(0);

        iv_google_photo = headerView.findViewById(R.id.drawer_header_iv_google_photo);
        Glide.with(this).load(google_photo).override(200, 200).into(iv_google_photo);

        tv_google_name = headerView.findViewById(R.id.drawer_header_tv_google_name);
        tv_google_name.setText(google_name);

        ibtn_add_section = (ImageButton)findViewById(R.id.main_timebar_ibtn_add_section);
        ibtn_add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
            }
        });
    }

    void set_date(Date date) {
        String getCurDate = new SimpleDateFormat("yyyy.MM.dd").format(date);

        if ((getCurDate.split("\\.")).length == 3) {
            set_year = Integer.parseInt(getCurDate.split("\\.")[0]);
            set_month = Integer.parseInt(getCurDate.split("\\.")[1]);
            set_day = Integer.parseInt(getCurDate.split("\\.")[2]);

            toolbar_tv_cur_date.setText(getCurDate);
        }

        else {
            toolbar_tv_cur_date.setText("Cal Err.");
        }
    }
}