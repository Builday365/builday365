package com.example.builday365.View;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.example.builday365.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UiFragment extends Fragment {
    private static final String TAG = "UiFragment";

    TextView tv_toolbar_cur_date, tv_google_name, tv_sidebar_cur_time, tv_timesection_cur_time,
            tv_timesection_click_time, tv_timesection_start_time,
            tv_sidebar_ctrl_cur_time_hr, tv_sidebar_ctrl_cur_time_min;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_side_menu, ibtn_add_section, ibtn_sidebar_memo, ibtn_sidebar_activity,
            ibtn_timesection_ok, ibtn_timesection_cancel, ibtn_timesection_palette;
    DrawerLayout drawerLayout;
    ConstraintLayout layout_timebar, layout_dialog_section,
            layout_sidebar_total_time,layout_sidebar_cur_time, layout_sidebar_remain_time,
            layout_time_section, layout_sidebar_ctrl, layout_timesection_remain_time,
            layout_palette;
    LinearLayout layout_sidebar_ctrl_cur_time_tv;
    ImageView iv_google_photo, iv_timesection_ctrl, iv_palette_blue, iv_palette_red, iv_palette_green,
            iv_palette_black, iv_palette_yellow, iv_palette_purple, iv_palette_skyBlue, iv_palette_brown,
            iv_palette_pink, iv_palette_lightGreen;
    Button btn_dialog_section_ok, btn_dialog_section_cancel, btn_palette_ok, btn_palette_cancel;
    EditText dialog_section_et_memo;

    int set_year, set_month, set_day, total_time_len, cur_time_len;
    boolean is_timesection_touched = false;
    Calendar calendar;
    CalendarView calendarView;
    int sidebar_touch_time = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                hideKeyboard();
                return false;
            }
        });

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

        layout_timebar = (ConstraintLayout)view.findViewById(R.id.fragment_layout_sidebar_ctrl);
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

//        ibtn_side_menu = (ImageButton)view.findViewById(R.id.main_toolbar_ibtn_side_menu);
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
//
//        layout_dialog_section = (ConstraintLayout)view.findViewById(R.id.fragment_dialog_make_section);
//        layout_dialog_section.setVisibility(View.GONE);
//
//        tv_timesection_start_time = (TextView)view.findViewById(R.id.main_timesection_tv_start_time);
//        tv_timesection_click_time = (TextView)view.findViewById(R.id.main_timesection_tv_click_time);
//        tv_timesection_click_time.setVisibility(View.GONE);
//
//        iv_timesection_ctrl = (ImageView)view.findViewById(R.id.main_timesection_iv_ctrl);
//        palette_selected_color = R.color.red;
//        palette_prev_palette_selected_color = palette_selected_color;
//        tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//        timesection_color = getResources().getString(palette_selected_color);
//        iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//        iv_timesection_ctrl.setVisibility(View.INVISIBLE);

        layout_sidebar_total_time = (ConstraintLayout)view.findViewById(R.id.sidebar_layout_total_time);
        layout_time_section = (ConstraintLayout)view.findViewById(R.id.fragment_layout_sidebar);
        layout_sidebar_ctrl_cur_time_tv = (LinearLayout)view.findViewById(R.id.sidebar_ctrl_layout_cur_time_tv);

        layout_sidebar_cur_time = (ConstraintLayout)view.findViewById(R.id.sidebar_layout_cur_time);
        layout_sidebar_total_time = (ConstraintLayout)view.findViewById(R.id.sidebar_layout_total_time);
        layout_sidebar_remain_time = (ConstraintLayout)view.findViewById(R.id.sidebar_layout_remain_time);

        layout_sidebar_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_ctrl_layout);
        tv_sidebar_ctrl_cur_time_hr = (TextView)view.findViewById(R.id.sidebar_ctrl_tv_cur_time_hr);
        tv_sidebar_ctrl_cur_time_min = (TextView)view.findViewById(R.id.sidebar_ctrl_tv_cur_time_min);
        ViewTreeObserver viewTreeObserver = layout_sidebar_total_time.getViewTreeObserver();

        layout_sidebar_ctrl_cur_time_tv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int[] touch_location = new int[2];
                layout_sidebar_total_time.getLocationOnScreen(touch_location);
                int touch_calced_y = (int) (motionEvent.getRawY() - touch_location[1]);

                total_time_len = layout_sidebar_total_time.getHeight();
                double time_rate = Math.max(0, Math.min(1, (double)touch_calced_y / (double)total_time_len));
                sidebar_touch_time = (int)(time_rate * 24 * 60);;

                if ((touch_calced_y > 0) && (touch_calced_y < total_time_len)) {
                    is_timesection_touched = true;

                    touch_calced_y = Math.max(0, Math.min(touch_calced_y, total_time_len));
                    tv_sidebar_ctrl_cur_time_hr.setText(String.format("%02d", sidebar_touch_time / 60));
                    tv_sidebar_ctrl_cur_time_min.setText(String.format("%02d", sidebar_touch_time % 60));

                    ConstraintLayout.LayoutParams layoutParams
                            = (ConstraintLayout.LayoutParams) layout_sidebar_ctrl.getLayoutParams();
                    layoutParams.topMargin = touch_calced_y - (layout_sidebar_ctrl.getHeight() / 2);
                    layout_sidebar_ctrl.setLayoutParams(layoutParams);

                    layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                    layoutParams.height = touch_calced_y;
                    layout_sidebar_cur_time.setLayoutParams(layoutParams);
                }

                else if (touch_calced_y <= 0) {
                    touch_calced_y = 1;
                    tv_sidebar_ctrl_cur_time_hr.setText("00");
                    tv_sidebar_ctrl_cur_time_min.setText("00");

                    ConstraintLayout.LayoutParams layoutParams
                            = (ConstraintLayout.LayoutParams) layout_sidebar_ctrl.getLayoutParams();
                    layoutParams.topMargin = touch_calced_y - (layout_sidebar_ctrl.getHeight() / 2);
                    layout_sidebar_ctrl.setLayoutParams(layoutParams);

                    layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                    layoutParams.height = touch_calced_y;
                    layout_sidebar_cur_time.setLayoutParams(layoutParams);
                }

                else if (touch_calced_y >= total_time_len) {
                    touch_calced_y = total_time_len;
                    tv_sidebar_ctrl_cur_time_hr.setText("24");
                    tv_sidebar_ctrl_cur_time_min.setText("00");

                    ConstraintLayout.LayoutParams layoutParams
                            = (ConstraintLayout.LayoutParams) layout_sidebar_ctrl.getLayoutParams();
                    layoutParams.topMargin = touch_calced_y - (layout_sidebar_ctrl.getHeight() / 2);
                    layout_sidebar_ctrl.setLayoutParams(layoutParams);

                    layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                    layoutParams.height = touch_calced_y;
                    layout_sidebar_cur_time.setLayoutParams(layoutParams);
                }

        return true;
        }
    });
//
//        ibtn_add_section = (ImageButton)view.findViewById(R.id.main_timebar_ibtn_add_section);
//        @SuppressLint("ResourceType")
//        String ibtn_add_section_color = getResources().getString(R.color.default_color);
//        ibtn_add_section.setImageTintList(ColorStateList.valueOf(Color.parseColor(ibtn_add_section_color)));
//        ibtn_add_section.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_time_section.setVisibility(View.VISIBLE);
//
//                @SuppressLint("ResourceType")
//                String icon_disable_color = getResources().getString(R.color.disable_blue);
//                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_disable_color)));
//                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_disable_color)));
//
//                ibtn_sidebar_memo.setEnabled(false);
//                ibtn_sidebar_activity.setEnabled(false);
//            }
//        });
//
//        @SuppressLint("ResourceType")
//        String select_color = getResources().getString(R.color.blue);
//        tv_sidebar_cur_time = (TextView)view.findViewById(R.id.main_sidebar_tv_cur_time);
//        tv_timesection_cur_time = (TextView)view.findViewById(R.id.main_timesection_tv_cur_time);
//
//        ibtn_sidebar_memo = (ImageButton)view.findViewById(R.id.main_sidebar_ibtn_memo);
//        ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
//        ibtn_sidebar_memo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_dialog_section.setVisibility(View.VISIBLE);
//            }
//        });
//
//        ibtn_sidebar_activity = (ImageButton)view.findViewById(R.id.main_sidebar_ibtn_activity);
//        ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(select_color)));
//        ibtn_sidebar_activity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_dialog_section.setVisibility(View.VISIBLE);
//            }
//        });
//
//        dialog_section_et_memo = (EditText)view.findViewById(R.id.dialog_section_et_memo);
//
//        btn_dialog_section_ok = (Button)view.findViewById(R.id.dialog_section_btn_ok);
//        btn_dialog_section_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard();
//                layout_dialog_section.setVisibility(View.GONE);
//
//                vm.addMemo( new Date(System.currentTimeMillis()),dialog_section_et_memo.getText().toString());
//
//            }
//        });
//
//        btn_dialog_section_cancel = (Button)view.findViewById(R.id.dialog_section_btn_cancel);
//        btn_dialog_section_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                hideKeyboard();
//                layout_dialog_section.setVisibility(View.GONE);
//            }
//        });
//
//        ConstraintLayout timebarView = (ConstraintLayout)view.findViewById(R.id.fragment_layout_timebar);
//        TextView tv_sidebar_start_time = (TextView) view.findViewById(R.id.main_sidebar_tv_start_time);
//        ibtn_timesection_ok = (ImageButton) view.findViewById(R.id.main_timesection_ibtn_ok);
//        ibtn_timesection_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_time_section.setVisibility(View.GONE);
//
//                TextView tv_sidebar_new_section = new TextView(timebarView.getContext());
//                ConstraintLayout.LayoutParams layoutParams
//                        = (ConstraintLayout.LayoutParams) tv_timesection_click_time.getLayoutParams();
//                int tv_new_top_margin = layoutParams.topMargin;
//                Log.e(TAG, "tv_new_top_margin: " + tv_new_top_margin);
//
//                layoutParams = (ConstraintLayout.LayoutParams) tv_sidebar_start_time.getLayoutParams();
//
//                int bottom2bottom = layoutParams.bottomToBottom;
//                int bottom2top = layoutParams.bottomToTop;
//                int end2start = layoutParams.endToStart;
//                int end2end = layoutParams.endToEnd;
//                int start2start = layoutParams.startToStart;
//                int start2end = layoutParams.startToEnd;
//                int top2top = layoutParams.topToTop;
//                int top2bottom = layoutParams.topToBottom;
//                float horBias = layoutParams.horizontalBias;
//                float vertBias = layoutParams.verticalBias;
//                int topMargin = layoutParams.topMargin;
//
//                Log.e(TAG, "tv_sidebar_cur_time");
//                Log.e(TAG, "layoutParams.topMargin: " + layoutParams.topMargin);
//                Log.e(TAG, "layoutParams.bottomToBottom: " + layoutParams.bottomToBottom);
//                Log.e(TAG, "layoutParams.startToStart: " + layoutParams.startToStart);
//                Log.e(TAG, "layoutParams.topToTop: " + layoutParams.topToTop);
//                Log.e(TAG, "layoutParams.horizontalBias: " + layoutParams.horizontalBias);
//                Log.e(TAG, "layoutParams.verticalBias: " + layoutParams.verticalBias);
//
//
////                tv_sidebar_new_section.setLayoutParams(prev_layoutParams);
////                layoutParams = (ConstraintLayout.LayoutParams) tv_sidebar_new_section.getLayoutParams();
////                layoutParams.topMargin += tv_new_top_margin;
////                tv_sidebar_new_section.setLayoutParams(layoutParams);
//
//                layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT,
//                                    ConstraintLayout.LayoutParams.WRAP_CONTENT);
//
//                layoutParams.bottomToBottom = bottom2bottom;
//                layoutParams.endToStart = end2start;
//                layoutParams.startToStart = start2start;
//                layoutParams.topToTop = top2top;
//                layoutParams.horizontalBias = horBias;
//                layoutParams.verticalBias = vertBias;
//                layoutParams.topMargin = tv_new_top_margin + topMargin;
//
//                tv_sidebar_new_section.setText(timesection_touch_time);
//                tv_sidebar_new_section.setTextSize(12);
//                tv_sidebar_new_section.setTypeface(null, Typeface.BOLD);
//
//                Log.e(TAG, "tv_sidebar_new_section");
//                Log.e(TAG, "layoutParams.topMargin: " + layoutParams.topMargin);
//                Log.e(TAG, "layoutParams.bottomToBottom: " + layoutParams.bottomToBottom);
//                Log.e(TAG, "layoutParams.startToStart: " + layoutParams.startToStart);
//                Log.e(TAG, "layoutParams.topToTop: " + layoutParams.topToTop);
//                Log.e(TAG, "layoutParams.horizontalBias: " + layoutParams.horizontalBias);
//                Log.e(TAG, "layoutParams.verticalBias: " + layoutParams.verticalBias);
//
////                app:layout_constraintBottom_toBottomOf="parent"
////                app:layout_constraintEnd_toStartOf="@+id/main_sidebar_layout_total_time"
////                app:layout_constraintHorizontal_bias="0.88"
////                app:layout_constraintStart_toStartOf="parent"
////                app:layout_constraintTop_toTopOf="parent"
//
//                tv_sidebar_new_section.setLayoutParams(layoutParams);
//                tv_sidebar_new_section.setTextColor(getResources().getColor(palette_selected_color));
//                timebarView.addView(tv_sidebar_new_section);
//
//                @SuppressLint("ResourceType")
//                String icon_able_color = getResources().getString(R.color.blue);
//                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
//                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
//
//                ibtn_sidebar_memo.setEnabled(true);
//                ibtn_sidebar_activity.setEnabled(true);
//            }
//        });
//
//        ibtn_timesection_cancel = (ImageButton)view.findViewById(R.id.main_timesection_ibtn_cancel);
//        ibtn_timesection_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_time_section.setVisibility(View.GONE);
//
//                @SuppressLint("ResourceType")
//                String icon_able_color = getResources().getString(R.color.blue);
//                ibtn_sidebar_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
//                ibtn_sidebar_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(icon_able_color)));
//
//                ibtn_sidebar_memo.setEnabled(true);
//                ibtn_sidebar_activity.setEnabled(true);
//            }
//        });
//
//        layout_palette = (ConstraintLayout)view.findViewById(R.id.fragment_layout_palette);
//        layout_palette.setVisibility(View.GONE);
//
//        ibtn_timesection_palette = (ImageButton)view.findViewById(R.id.main_timesection_ibtn_palette);
//        ibtn_timesection_palette.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                layout_palette.setVisibility(View.VISIBLE);
//            }
//        });
//
//        iv_palette_blue = (ImageView)view.findViewById(R.id.palette_iv_blue);
//        iv_palette_blue.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint({"ResourceAsColor", "ResourceType"})
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.blue;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_red = (ImageView)view.findViewById(R.id.palette_iv_red);
//        iv_palette_red.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint({"ResourceAsColor", "ResourceType"})
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.red;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_green = (ImageView)view.findViewById(R.id.palette_iv_green);
//        iv_palette_green.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.green;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_black = (ImageView)view.findViewById(R.id.palette_iv_black);
//        iv_palette_black.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.black;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_yellow = (ImageView)view.findViewById(R.id.palette_iv_yellow);
//        iv_palette_yellow.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.yellow;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_purple = (ImageView)view.findViewById(R.id.palette_iv_purple);
//        iv_palette_purple.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.purple;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_skyBlue = (ImageView)view.findViewById(R.id.palette_iv_skyBlue);
//        iv_palette_skyBlue.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.sky_blue;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_brown = (ImageView)view.findViewById(R.id.palette_iv_brown);
//        iv_palette_brown.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.brown;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_pink = (ImageView)view.findViewById(R.id.palette_iv_pink);
//        iv_palette_pink.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.pink;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//        iv_palette_lightGreen = (ImageView)view.findViewById(R.id.palette_iv_lightGreen);
//        iv_palette_lightGreen.setOnClickListener(new View.OnClickListener() {
//            @SuppressLint("ResourceAsColor")
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = R.color.light_green;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//            }
//        });
//
//        btn_palette_ok = (Button)view.findViewById(R.id.palette_btn_ok);
//        btn_palette_ok.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                palette_prev_palette_selected_color = palette_selected_color;
//                layout_palette.setVisibility(View.GONE);
//            }
//        });
//
//        btn_palette_cancel= (Button)view.findViewById(R.id.palette_btn_cancel);
//        btn_palette_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                palette_selected_color = palette_prev_palette_selected_color;
//                layout_timesection_remain_time.setBackgroundResource(get_border_color(palette_selected_color));
//                tv_timesection_click_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_cur_time.setTextColor(getResources().getColor(palette_selected_color));
//                tv_timesection_start_time.setTextColor(getResources().getColor(palette_selected_color));
//                timesection_color = getResources().getString(palette_selected_color);
//                iv_timesection_ctrl.setImageTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//                layout_timesection_cur_time.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor(timesection_color)));
//
//                layout_palette.setVisibility(View.GONE);
//            }
//        });


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

    @Override
    public void onStart() {
        Log.d(TAG,"onStart is called");
        super.onStart();
    }

    public static Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
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

        return cur_time;
    }

    public void update_time() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message message) {
//                layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_remain_time.getLayoutParams();
//                layoutParams.topMargin = Math.min(total_time_len, cur_time_len + layout_side_gap);
//                layoutParams.height = Math.max(0, total_time_len - cur_time_len - layout_side_gap);
//                layout_sidebar_remain_time.setLayoutParams(layoutParams);
//
                if (!is_timesection_touched) {
                    String cur_time = get_time();
                    total_time_len = layout_sidebar_total_time.getHeight();

//                /* ***************** TIME DEBUG ****************** */
//                cur_time_len = (int)(0.6 * total_time_len);
//                /***************************************************/

                    int cur_hour = Integer.parseInt(cur_time.split(":")[0]);
                    int cur_min = Integer.parseInt(cur_time.split(":")[1]);
                    double time_rate = (cur_hour * 60 + cur_min) / (24 * 60.0);
                    cur_time_len = (int)(time_rate * total_time_len);

                    tv_sidebar_ctrl_cur_time_hr.setText(String.format("%02d", cur_hour));
                    tv_sidebar_ctrl_cur_time_min.setText(String.format("%02d", cur_min));

                    ConstraintLayout.LayoutParams layoutParams
                            = (ConstraintLayout.LayoutParams) layout_sidebar_ctrl.getLayoutParams();
                    int layout_sidebar_ctrl_top_margin = cur_time_len - (layout_sidebar_ctrl.getHeight() / 2);
                    layoutParams.topMargin = Math.max(0, Math.min(total_time_len, layout_sidebar_ctrl_top_margin));
                    layout_sidebar_ctrl.setLayoutParams(layoutParams);

                    layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                    layoutParams.height = cur_time_len;
                    layout_sidebar_cur_time.setLayoutParams(layoutParams);
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

    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
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