package com.example.builday365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    int set_year, set_month, set_day;
    Calendar calendar;
    TextView toolbar_tv_cur_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar_tv_cur_date = (TextView)findViewById(R.id.main_toolbar_tv_cur_date);

        calendar = Calendar.getInstance();
        set_date(new Date(calendar.getTimeInMillis()));

        ImageButton ibtn_side_menu = (ImageButton)findViewById(R.id.main_toolbar_ibtn_side_menu);
        DrawerLayout drawerLayout = (DrawerLayout)findViewById(R.id.main_drawer);
        ibtn_side_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
                    drawerLayout.openDrawer(Gravity.LEFT);
                }
                else {
                    drawerLayout.closeDrawers();
                }
            }
        });

        ImageButton ibtn_calendar = (ImageButton)findViewById(R.id.main_toolbar_ibtn_calendar);
        CalendarView calendarView = (CalendarView)findViewById(R.id.main_calendarview);
        calendarView.setVisibility(View.GONE);
        ibtn_calendar.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 int cur_visibilty = calendarView.getVisibility();
                 calendarView.setVisibility((cur_visibilty==View.VISIBLE)? View.GONE : View.VISIBLE);

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
            }
        });

        ImageButton ibtn_day_prev = (ImageButton)findViewById(R.id.main_toolbar_ibtn_day_prev);
        ibtn_day_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                set_date(new Date(calendar.getTimeInMillis()));
            }
        });

        ImageButton ibtn_day_next = (ImageButton)findViewById(R.id.main_toolbar_ibtn_day_next);
        ibtn_day_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                set_date(new Date(calendar.getTimeInMillis()));
            }
        });

        ImageButton ibtn_month_prev = (ImageButton)findViewById(R.id.main_toolbar_ibtn_month_prev);
        ibtn_month_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                set_date(new Date(calendar.getTimeInMillis()));
            }
        });

        ImageButton ibtn_month_next = (ImageButton)findViewById(R.id.main_toolbar_ibtn_month_next);
        ibtn_month_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, +1);
                set_date(new Date(calendar.getTimeInMillis()));
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