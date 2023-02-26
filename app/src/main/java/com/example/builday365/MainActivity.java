package com.example.builday365;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;

import android.util.Log;

public class MainActivity extends AppCompatActivity
        implements OnMapReadyCallback,
        ActivityCompat.OnRequestPermissionsResultCallback {

    private static final String TAG = "MainActivity";
    String[] REQUIRED_PERMISSIONS = {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 100;

    TextView tv_toolbar_cur_date, tv_google_name, tv_sidebar_cur_time;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_side_menu, ibtn_add_section, ibtn_sidebar_memo, ibtn_sidebar_activity;
    DrawerLayout drawerLayout;
    ConstraintLayout timeBarLayout, makeSectionLayout,
                    layout_sidebar_total_time, layout_sidebar_cur_time, layout_sidebar_remain_time;
    ImageView iv_google_photo;
    Button btn_dialog_section_ok, btn_dialog_section_cancel;
    EditText dialog_section_et_memo;

    NavigationView navigationView;
    View headerView;

    int set_year, set_month, set_day;
    Calendar calendar;
    CalendarView calendarView;

    private GoogleMap mGoogleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_toolbar_cur_date = (TextView)findViewById(R.id.main_toolbar_tv_cur_date);
        tv_toolbar_cur_date.setOnClickListener(new View.OnClickListener() {
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
                makeSectionLayout.setVisibility(View.GONE);

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
                tv_toolbar_cur_date.setText(String.format("%d.%02d.%02d", year, month+1, day));

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
                    makeSectionLayout.setVisibility(View.GONE);

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

        makeSectionLayout = (ConstraintLayout)findViewById(R.id.main_dialog_make_section);
        makeSectionLayout.setVisibility(View.GONE);

        ibtn_add_section = (ImageButton)findViewById(R.id.main_timebar_ibtn_add_section);
        @SuppressLint("ResourceType")
        String ibtn_add_section_color = getResources().getString(R.color.default_color);
        ibtn_add_section.setImageTintList(ColorStateList.valueOf(Color.parseColor(ibtn_add_section_color)));
        ibtn_add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        @SuppressLint("ResourceType")
        String select_color = getResources().getString(R.color.blue);
        tv_sidebar_cur_time = (TextView)findViewById(R.id.main_sidebar_tv_cur_time);

        ibtn_sidebar_memo = (ImageButton)findViewById(R.id.main_sidebar_ibtn_memo);
        ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
        ibtn_sidebar_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSectionLayout.setVisibility(View.VISIBLE);
            }
        });

        ibtn_sidebar_activity = (ImageButton)findViewById(R.id.main_sidebar_ibtn_activity);
        ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
        ibtn_sidebar_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSectionLayout.setVisibility(View.VISIBLE);
            }
        });

        dialog_section_et_memo = (EditText)findViewById(R.id.dialog_section_et_memo);

        btn_dialog_section_ok = (Button)findViewById(R.id.dialog_section_btn_ok);
        btn_dialog_section_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSectionLayout.setVisibility(View.GONE);
            }
        });

        btn_dialog_section_cancel = (Button)findViewById(R.id.dialog_section_btn_cancel);
        btn_dialog_section_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeSectionLayout.setVisibility(View.GONE);
            }
        });


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        layout_sidebar_total_time = (ConstraintLayout)findViewById(R.id.main_sidebar_layout_total_time);
        layout_sidebar_cur_time = (ConstraintLayout)findViewById(R.id.main_sidebar_layout_cur_time);
        layout_sidebar_remain_time = (ConstraintLayout)findViewById(R.id.main_sidebar_layout_remain_time);
        ViewTreeObserver viewTreeObserver = layout_sidebar_total_time.getViewTreeObserver();

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout_sidebar_total_time.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    update_time(layout_sidebar_total_time.getHeight());
                }
            });
        }
    }

    public void set_date(Date date) {
        String getCurDate = new SimpleDateFormat("yyyy.MM.dd").format(date);

        if ((getCurDate.split("\\.")).length == 3) {
            set_year = Integer.parseInt(getCurDate.split("\\.")[0]);
            set_month = Integer.parseInt(getCurDate.split("\\.")[1]);
            set_day = Integer.parseInt(getCurDate.split("\\.")[2]);

            tv_toolbar_cur_date.setText(getCurDate);
        }

        else {
            tv_toolbar_cur_date.setText("Cal Err.");
        }
    }

    public String get_time() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        String cur_time = simpleDateFormat.format(date);
        Log.e(TAG, "get_time() " + cur_time);

        return cur_time;
    }

    public void update_time(int total_time_len) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                String cur_time = get_time();
                tv_sidebar_cur_time.setText(cur_time);
//                Log.e(TAG, "handleMessage " + cur_time);

                int cur_hour = Integer.parseInt(cur_time.split(":")[0]);
                int cur_min = Integer.parseInt(cur_time.split(":")[1]);
                double time_rate = (cur_hour * 60 + cur_min) / (24 * 60.0);
                int cur_time_len = (int)(time_rate * total_time_len);
//                Log.e(TAG, "time_rate " + time_rate);
//                Log.e(TAG, "total_time_len " + total_time_len);
//                Log.e(TAG, "cur_time_len " + cur_time_len);

                ConstraintLayout.LayoutParams layoutParams
                        = (ConstraintLayout.LayoutParams) tv_sidebar_cur_time.getLayoutParams();
                layoutParams.topMargin = cur_time_len;
                tv_sidebar_cur_time.setLayoutParams(layoutParams);

                layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                layoutParams.height = cur_time_len;
                layout_sidebar_cur_time.setLayoutParams(layoutParams);

                layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_remain_time.getLayoutParams();
//                Log.e(TAG, "layoutParams.height " + layoutParams.height);
//                Log.e(TAG, "layoutParams.topMargin " + layoutParams.topMargin);
                int layout_side_gap = 5;
                layoutParams.topMargin = cur_time_len + layout_side_gap;
                layoutParams.height = total_time_len - cur_time_len - layout_side_gap;
                layout_sidebar_remain_time.setLayoutParams(layoutParams);
//                Log.e(TAG, "layoutParams.height " + layoutParams.height);
//                Log.e(TAG, "layoutParams.topMargin - cur_time_len" + layoutParams.topMargin);
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                    }
                    catch (InterruptedException e) {
                        Log.e(TAG, "thread error " + e);
                    }

                    handler.sendEmptyMessage(1);
                }
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setZoomGesturesEnabled(true);

        LatLng lastLatLng = getLastLocation();
        if (lastLatLng == null) {
            Log.d(TAG, "[onMapReady] request permission");
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            return;
        }
        updateLocation(lastLatLng);
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);

        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "[onRequestPermissionsResult] permission is denied");
                    return;
                }
            }
            LatLng lastLatLng = getLastLocation();
            if (lastLatLng != null) {
                updateLocation(lastLatLng);
            }
        }
    }

    private LatLng getLastLocation() {
        // permission check
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "[getLastLocation] permission denied");
            return null;
        }
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location lastLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        if (lastLocation == null) {
            Log.d(TAG, "[getLastLocation] the last location info is not exist");
            return null;
        }
        double lastLatitude = lastLocation.getLatitude();
        double lastLongtitude  = lastLocation.getLongitude();

        LatLng lastLocationLatLng = new LatLng(lastLatitude, lastLongtitude);
        return lastLocationLatLng;
    }

    private void updateLocation(LatLng location) {
        Log.d(TAG, "[updateLocation] " + location.toString());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(location);
        markerOptions.draggable(true);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        mGoogleMap.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15);
        mGoogleMap.moveCamera(cameraUpdate);
    }
}