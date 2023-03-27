package com.example.builday365;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UiFragment extends Fragment {
    private static final String TAG = "UiFragment";

    TextView tv_toolbar_cur_date, tv_google_name, tv_sidebar_cur_time, tv_timesection_cur_time,
            tv_timesection_click_time, tv_timesection_start_time;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_side_menu, ibtn_add_section, ibtn_sidebar_memo, ibtn_sidebar_activity,
            ibtn_timesection_ok, ibtn_timesection_cancel, ibtn_timesection_palette;
    DrawerLayout drawerLayout;
    ConstraintLayout layout_timebar, layout_dialog_section,
            layout_sidebar_total_time, layout_sidebar_cur_time, layout_sidebar_remain_time,
            layout_time_section, layout_timesection_cur_time, layout_timesection_remain_time,
            layout_palette;
    ImageView iv_google_photo, iv_timesection_ctrl, iv_palette_blue, iv_palette_red, iv_palette_green,
            iv_palette_black, iv_palette_yellow, iv_palette_purple, iv_palette_skyBlue, iv_palette_brown,
            iv_palette_pink, iv_palette_lightGreen;
    Button btn_dialog_section_ok, btn_dialog_section_cancel, btn_palette_ok, btn_palette_cancel;
    EditText dialog_section_et_memo;

    NavigationView navigationView;
    View headerView;

    int set_year, set_month, set_day, total_time_len, cur_time_len;
    int tv_time_margin_gap = 15;
    int layout_side_gap = 5;
    boolean is_timesection_touched = false;
    Calendar calendar;
    CalendarView calendarView;
    int palette_selected_color, palette_prev_palette_selected_color;
    String timesection_color;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui, container, false);

        tv_toolbar_cur_date = (TextView)view.findViewById(R.id.main_toolbar_tv_cur_date);
        tv_toolbar_cur_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        calendar = Calendar.getInstance();
        set_date(new Date(calendar.getTimeInMillis()));

        layout_timebar = (ConstraintLayout)view.findViewById(R.id.fragment_layout_timebar);
        ibtn_calendar = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_calendar);
        calendarView = (CalendarView)view.findViewById(R.id.fragment_calendarview);
        calendarView.setVisibility(View.GONE);

        ibtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_visibilty = calendarView.getVisibility();
                calendarView.setVisibility((cur_visibilty==View.VISIBLE)? View.GONE : View.VISIBLE);
                layout_timebar.setVisibility((cur_visibilty==View.VISIBLE)? View.VISIBLE : View.GONE);
                layout_dialog_section.setVisibility(View.GONE);
                layout_time_section.setVisibility(View.GONE);

//                if (drawerLayout.isDrawerOpen(Gravity.LEFT)){
//                    drawerLayout.closeDrawers();
//                }

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
                layout_timebar.setVisibility(View.VISIBLE);
            }
        });

        ibtn_day_prev = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_day_prev);
        ibtn_day_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_day_next = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_day_next);
        ibtn_day_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_month_prev = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_month_prev);
        ibtn_month_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_month_next = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_month_next);
        ibtn_month_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_side_menu = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_side_menu);
//        drawerLayout = (DrawerLayout)view.findViewById(R.id.main_drawer);
//        drawerLayout.closeDrawers();
//        ibtn_side_menu.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!drawerLayout.isDrawerOpen(Gravity.LEFT)){
//                    drawerLayout.openDrawer(Gravity.LEFT);
//                    calendarView.setVisibility(View.GONE);
//                    layout_timebar.setVisibility(View.GONE);
//                    layout_time_section.setVisibility(View.GONE);
//                    layout_dialog_section.setVisibility(View.GONE);
//                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_OPEN);
//                    layout_palette.setVisibility(View.GONE);
//                    palette_prev_palette_selected_color = palette_selected_color;
//                }
//                else {
//                    drawerLayout.closeDrawers();
//                    layout_timebar.setVisibility(View.VISIBLE);
//                }
//            }
//        });
//
//        Intent intent = getActivity().getIntent();
//        String google_photo = intent.getStringExtra("google_photo");
//        String google_name = intent.getStringExtra("google_name");
//
//        navigationView = (NavigationView)view.findViewById(R.id.main_navigationView);
//        headerView = navigationView.getHeaderView(0);
//
//        iv_google_photo = headerView.findViewById(R.id.drawer_header_iv_google_photo);
//        Glide.with(this).load(google_photo).override(200, 200).into(iv_google_photo);
//
//        tv_google_name = headerView.findViewById(R.id.drawer_header_tv_google_name);
//        tv_google_name.setText(google_name);

        layout_dialog_section = (ConstraintLayout)view.findViewById(R.id.fragment_dialog_make_section);
        layout_dialog_section.setVisibility(View.GONE);

        tv_timesection_start_time = (TextView)view.findViewById(R.id.main_timesection_tv_start_time);
        tv_timesection_click_time = (TextView)view.findViewById(R.id.main_timesection_tv_click_time);
        tv_timesection_click_time.setVisibility(View.GONE);

        iv_timesection_ctrl = (ImageView)view.findViewById(R.id.main_timesection_iv_ctrl);
        palette_selected_color = R.color.red;
        palette_prev_palette_selected_color = palette_selected_color;
        tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
        timesection_color = getResources().getString(palette_selected_color);
        iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
        iv_timesection_ctrl.setVisibility(View.INVISIBLE);

        layout_time_section = (ConstraintLayout)view.findViewById(R.id.fragment_layout_timesection);
        layout_time_section.setVisibility(View.GONE);

        layout_timesection_remain_time = (ConstraintLayout)view.findViewById(R.id.main_timesection_layout_remain_time);
        layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
        layout_timesection_remain_time.setVisibility(View.GONE);

        layout_time_section.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int touch_action = motionEvent.getAction();
                float touch_y = motionEvent.getY();
                layout_palette.setVisibility(View.GONE);
                palette_prev_palette_selected_color = palette_selected_color;

                if((touch_action == motionEvent.ACTION_DOWN) || (touch_action == motionEvent.ACTION_MOVE)) {
                    int touch_calced_y = (int)(touch_y - layout_time_section.getY()
                            + layout_timesection_cur_time.getY());
                    double time_rate = (double)touch_calced_y / (double)total_time_len;

                    if ((touch_calced_y >= 0) && (touch_calced_y <= cur_time_len)) {
                        is_timesection_touched = true;
                        int touch_time = (int)(time_rate * 24 * 60);
                        String touch_hour = String.format("%02d", touch_time / 60);
                        String touch_min = String.format("%02d", touch_time % 60);
                        String touch_show_time = touch_hour + ":" + touch_min;

                        ConstraintLayout.LayoutParams layoutParams
                                = (ConstraintLayout.LayoutParams) tv_timesection_click_time.getLayoutParams();
                        layoutParams.topMargin = touch_calced_y - tv_time_margin_gap;
                        tv_timesection_click_time.setLayoutParams(layoutParams);
                        tv_timesection_click_time.setVisibility(View.VISIBLE);
                        tv_timesection_click_time.setText(touch_show_time);

                        iv_timesection_ctrl.setVisibility(View.VISIBLE);
                        layout_timesection_remain_time.setVisibility(View.VISIBLE);

                        if (touch_time == 0) {
                            iv_timesection_ctrl.setImageResource(R.drawable.builday_icon_timesection_down);

                            layoutParams = (ConstraintLayout.LayoutParams) layout_timesection_remain_time.getLayoutParams();
                            layoutParams.height = cur_time_len;
                            layoutParams.topMargin = 0;
                            layout_timesection_remain_time.setLayoutParams(layoutParams);

                        }
                        else if (touch_time == cur_time_len) {
                            iv_timesection_ctrl.setImageResource(R.drawable.builday_icon_timesection_up);
                        }
                        else {
                            iv_timesection_ctrl.setImageResource(R.drawable.builday_icon_timesection_updown);

                            layoutParams = (ConstraintLayout.LayoutParams) layout_timesection_cur_time.getLayoutParams();
                            layoutParams.height = Math.min(touch_calced_y, total_time_len);
                            layout_timesection_cur_time.setLayoutParams(layoutParams);

                            layoutParams = (ConstraintLayout.LayoutParams) layout_timesection_remain_time.getLayoutParams();
                            layoutParams.height = Math.max(cur_time_len - touch_calced_y - layout_side_gap, 1);
                            layoutParams.topMargin = Math.min(touch_calced_y + layout_side_gap, total_time_len);
                            layout_timesection_remain_time.setLayoutParams(layoutParams);
//
//                            Log.e("layoutParams height", String.valueOf(layoutParams.height));
//                            Log.e("layoutParams topMargin", String.valueOf(layoutParams.topMargin));
                        }
                    }
                }

                return true;
            }
        });

        ibtn_add_section = (ImageButton)view.findViewById(R.id.main_timebar_ibtn_add_section);
        @SuppressLint("ResourceType")
        String ibtn_add_section_color = getResources().getString(R.color.default_color);
        ibtn_add_section.setImageTintList(ColorStateList.valueOf(Color.parseColor(ibtn_add_section_color)));
        ibtn_add_section.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_time_section.setVisibility(View.VISIBLE);

                @SuppressLint("ResourceType")
                String icon_disable_color = getResources().getString(R.color.disable_blue);
                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_disable_color)));
                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_disable_color)));

                ibtn_sidebar_memo.setEnabled(false);
                ibtn_sidebar_activity.setEnabled(false);
            }
        });

        @SuppressLint("ResourceType")
        String select_color = getResources().getString(R.color.blue);
        tv_sidebar_cur_time = (TextView)view.findViewById(R.id.main_sidebar_tv_cur_time);
        tv_timesection_cur_time = (TextView)view.findViewById(R.id.main_timesection_tv_cur_time);

        ibtn_sidebar_memo = (ImageButton)view.findViewById(R.id.main_sidebar_ibtn_memo);
        ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
        ibtn_sidebar_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_dialog_section.setVisibility(View.VISIBLE);
            }
        });

        ibtn_sidebar_activity = (ImageButton)view.findViewById(R.id.main_sidebar_ibtn_activity);
        ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
        ibtn_sidebar_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_dialog_section.setVisibility(View.VISIBLE);
            }
        });

        dialog_section_et_memo = (EditText)view.findViewById(R.id.dialog_section_et_memo);

        btn_dialog_section_ok = (Button)view.findViewById(R.id.dialog_section_btn_ok);
        btn_dialog_section_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_dialog_section.setVisibility(View.GONE);
            }
        });

        btn_dialog_section_cancel = (Button)view.findViewById(R.id.dialog_section_btn_cancel);
        btn_dialog_section_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_dialog_section.setVisibility(View.GONE);
            }
        });

        ibtn_timesection_ok = (ImageButton) view.findViewById(R.id.main_timesection_ibtn_ok);
        ibtn_timesection_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_time_section.setVisibility(View.GONE);

                @SuppressLint("ResourceType")
                String icon_able_color = getResources().getString(R.color.blue);
                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));

                ibtn_sidebar_memo.setEnabled(true);
                ibtn_sidebar_activity.setEnabled(true);
            }
        });

        ibtn_timesection_cancel = (ImageButton)view.findViewById(R.id.main_timesection_ibtn_cancel);
        ibtn_timesection_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_time_section.setVisibility(View.GONE);

                @SuppressLint("ResourceType")
                String icon_able_color = getResources().getString(R.color.blue);
                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));

                ibtn_sidebar_memo.setEnabled(true);
                ibtn_sidebar_activity.setEnabled(true);
            }
        });

        layout_palette = (ConstraintLayout)view.findViewById(R.id.fragment_layout_palette);
        layout_palette.setVisibility(View.GONE);

        ibtn_timesection_palette = (ImageButton)view.findViewById(R.id.main_timesection_ibtn_palette);
        ibtn_timesection_palette.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                layout_palette.setVisibility(View.VISIBLE);
            }
        });

        iv_palette_blue = (ImageView)view.findViewById(R.id.palette_iv_blue);
        iv_palette_blue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.blue;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_red = (ImageView)view.findViewById(R.id.palette_iv_red);
        iv_palette_red.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.red;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_green = (ImageView)view.findViewById(R.id.palette_iv_green);
        iv_palette_green.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.green;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_black = (ImageView)view.findViewById(R.id.palette_iv_black);
        iv_palette_black.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.black;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_yellow = (ImageView)view.findViewById(R.id.palette_iv_yellow);
        iv_palette_yellow.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.yellow;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_purple = (ImageView)view.findViewById(R.id.palette_iv_purple);
        iv_palette_purple.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.purple;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_skyBlue = (ImageView)view.findViewById(R.id.palette_iv_skyBlue);
        iv_palette_skyBlue.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.sky_blue;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_brown = (ImageView)view.findViewById(R.id.palette_iv_brown);
        iv_palette_brown.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.brown;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_pink = (ImageView)view.findViewById(R.id.palette_iv_pink);
        iv_palette_pink.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.pink;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });
        iv_palette_lightGreen = (ImageView)view.findViewById(R.id.palette_iv_lightGreen);
        iv_palette_lightGreen.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                palette_selected_color = R.color.light_green;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
            }
        });

        btn_palette_ok = (Button)view.findViewById(R.id.palette_btn_ok);
        btn_palette_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                palette_prev_palette_selected_color = palette_selected_color;
                layout_palette.setVisibility(View.GONE);
            }
        });

        btn_palette_cancel= (Button)view.findViewById(R.id.palette_btn_cancel);
        btn_palette_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                palette_selected_color = palette_prev_palette_selected_color;
                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
                timesection_color = getResources().getString(palette_selected_color);
                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));

                layout_palette.setVisibility(View.GONE);
            }
        });

        layout_sidebar_total_time = (ConstraintLayout)view.findViewById(R.id.main_sidebar_layout_total_time);
        layout_sidebar_cur_time = (ConstraintLayout)view.findViewById(R.id.main_sidebar_layout_cur_time);
        layout_timesection_cur_time = (ConstraintLayout)view.findViewById(R.id.main_timesection_layout_cur_time);
        layout_sidebar_remain_time = (ConstraintLayout)view.findViewById(R.id.main_sidebar_layout_remain_time);
        ViewTreeObserver viewTreeObserver = layout_sidebar_total_time.getViewTreeObserver();

        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout_sidebar_total_time.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    update_time();
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        calendarView = (CalendarView)view.findViewById(R.id.fragment_calendarview);
        super.onViewCreated(view, savedInstanceState);
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
//        Log.e(TAG, "get_time() " + cur_time);

        return cur_time;
    }

    public void update_time() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
                String cur_time = get_time();
                tv_sidebar_cur_time.setText(cur_time);
                tv_timesection_cur_time.setText(cur_time);
                total_time_len = layout_sidebar_total_time.getHeight();

                int cur_hour = Integer.parseInt(cur_time.split(":")[0]);
                int cur_min = Integer.parseInt(cur_time.split(":")[1]);
                double time_rate = (cur_hour * 60 + cur_min) / (24 * 60.0);

                /* ***************** TIME DEBUG ****************** */
                cur_time_len = (int)(time_rate * total_time_len);
                cur_time_len = (int)(0.6 * total_time_len);
                /***************************************************/

                ConstraintLayout.LayoutParams layoutParams
                        = (ConstraintLayout.LayoutParams) tv_sidebar_cur_time.getLayoutParams();
                layoutParams.topMargin = cur_time_len - tv_time_margin_gap;
                tv_sidebar_cur_time.setLayoutParams(layoutParams);

                layoutParams = (ConstraintLayout.LayoutParams) tv_timesection_cur_time.getLayoutParams();
                layoutParams.topMargin = cur_time_len - tv_time_margin_gap;
                tv_timesection_cur_time.setLayoutParams(layoutParams);

                layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                layoutParams.height = cur_time_len;
                layout_sidebar_cur_time.setLayoutParams(layoutParams);

                layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_remain_time.getLayoutParams();
                layoutParams.topMargin = cur_time_len + layout_side_gap;
                layoutParams.height = total_time_len - cur_time_len - layout_side_gap;
                layout_sidebar_remain_time.setLayoutParams(layoutParams);

                if (!is_timesection_touched) {
                    layoutParams = (ConstraintLayout.LayoutParams) layout_timesection_cur_time.getLayoutParams();
                    layoutParams.height = cur_time_len;
                    layout_timesection_cur_time.setLayoutParams(layoutParams);
                }
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
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

    public int get_border_color(int color) {
        if (color == R.color.blue) {
            return R.drawable.border_all_dir_blue;
        }
        else if (color == R.color.red) {
            return R.drawable.border_all_dir_red;
        }
        else if (color == R.color.green) {
            return R.drawable.border_all_dir_green;
        }
        else if (color == R.color.black) {
            return R.drawable.border_all_dir_black;
        }
        else if (color == R.color.yellow) {
            return R.drawable.border_all_dir_yellow;
        }
        else if (color == R.color.purple) {
            return R.drawable.border_all_dir_purple;
        }
        else if (color == R.color.sky_blue) {
            return R.drawable.border_all_dir_sky_blue;
        }
        else if (color == R.color.brown) {
            return R.drawable.border_all_dir_brown;
        }
        else if (color == R.color.pink) {
            return R.drawable.border_all_dir_pink;
        }
        else {
            return R.drawable.border_all_dir_light_green;
        }
    }
}