package com.example.builday365.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
            tv_sidebar_ctrl_cur_time_hr, tv_sidebar_ctrl_cur_time_min,
            tv_sidebar_ctrl_memo_start_time_hr, tv_sidebar_ctrl_memo_start_time_min,
            tv_sidebar_ctrl_memo_end_time_hr, tv_sidebar_ctrl_memo_end_time_min;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_sidebar_ctrl_activity, ibtn_sidebar_ctrl_memo, ibtn_sidebar_memo, ibtn_sidebar_activity,
            ibtn_timesection_ok, ibtn_timesection_cancel, ibtn_timesection_palette;
    DrawerLayout drawerLayout;
    ConstraintLayout layout_timebar, layout_dialog_section,
            layout_sidebar_total_time, layout_sidebar_cur_time, layout_sidebar_remain_time,
            layout_time_section,
            layout_sidebar_ctrl, layout_sidebar_startTime_ctrl, layout_sidebar_endTime_ctrl,

    layout_sidebar_ctrl_bar, layout_sidebar_blank, layout_diaglog_box;
    LinearLayout layout_sidebar_ctrl_cur_time_tv, layout_sidebar_time_list, layout_sidebar_ctrl_ui_btn,
            layout_sidebar_ctrl_layout_memo_start_time_tv, layout_sidebar_ctrl_layout_memo_end_time_tv;
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
    long sidebar_delay = 0;

    String App_Section_Color, App_Activity, App_Memo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui, container, false);

        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        tv_toolbar_cur_date = (TextView) view.findViewById(R.id.main_toolbar_tv_cur_date);
        tv_toolbar_cur_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                set_date(new Date(calendar.getTimeInMillis()));

                is_timesection_touched = false;
                update_time();

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    layout_timebar.setVisibility(View.VISIBLE);
                }
            }
        });

        calendar = Calendar.getInstance();
        set_date(new Date(calendar.getTimeInMillis()));

        layout_timebar = (ConstraintLayout) view.findViewById(R.id.fragment_layout_sidebar_ctrl);
        ibtn_calendar = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_calendar);
        calendarView = (CalendarView) view.findViewById(R.id.fragment_calendarview);
        calendarView.setVisibility(View.GONE);

        ibtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_visibilty = calendarView.getVisibility();
                calendarView.setVisibility((cur_visibilty == View.VISIBLE) ? View.GONE : View.VISIBLE);
                layout_timebar.setVisibility((cur_visibilty == View.VISIBLE) ? View.VISIBLE : View.GONE);
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
                tv_toolbar_cur_date.setText(String.format("%d.%02d.%02d", year, month + 1, day));

                set_year = year;
                set_month = month + 1;
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

        ibtn_day_prev = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_day_prev);
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

        ibtn_day_next = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_day_next);
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

        ibtn_month_prev = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_month_prev);
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

        ibtn_month_next = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_month_next);
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

        layout_sidebar_total_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_total_time);
        layout_time_section = (ConstraintLayout) view.findViewById(R.id.fragment_layout_sidebar);
        layout_sidebar_ctrl_ui_btn = (LinearLayout) view.findViewById(R.id.sidebar_ctrl_layout_ui_btn);
        layout_sidebar_ctrl_cur_time_tv = (LinearLayout) view.findViewById(R.id.sidebar_ctrl_layout_cur_time_tv);
        layout_sidebar_time_list = (LinearLayout) view.findViewById(R.id.sidebar_layout_time_list);

        layout_sidebar_cur_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_cur_time);
        layout_sidebar_total_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_total_time);
        layout_sidebar_remain_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_remain_time);

        layout_sidebar_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_ctrl_layout);
        layout_sidebar_startTime_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_startTime_ctrl_layout);
        layout_sidebar_endTime_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_endTime_ctrl_layout);
        layout_sidebar_startTime_ctrl.setVisibility(View.GONE);
        layout_sidebar_endTime_ctrl.setVisibility(View.GONE);

        tv_sidebar_ctrl_cur_time_hr = (TextView) view.findViewById(R.id.sidebar_ctrl_tv_cur_time_hr);
        tv_sidebar_ctrl_cur_time_min = (TextView) view.findViewById(R.id.sidebar_ctrl_tv_cur_time_min);
        ViewTreeObserver viewTreeObserver = layout_sidebar_total_time.getViewTreeObserver();


        ibtn_sidebar_ctrl_activity = (ImageButton) view.findViewById(R.id.sidebar_ctrl_ibtn_activity);
        ibtn_sidebar_ctrl_memo = (ImageButton) view.findViewById(R.id.sidebar_ctrl_ibtn_memo);
        layout_diaglog_box = (ConstraintLayout) view.findViewById(R.id.fragment_dialog_box);
        layout_diaglog_box.setVisibility(View.GONE);
        btn_dialog_section_ok = (Button) view.findViewById((R.id.dialog_section_btn_ok));
        btn_dialog_section_cancel = (Button) view.findViewById((R.id.dialog_section_btn_cancel));


        ibtn_sidebar_ctrl_memo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layout_sidebar_ctrl.setVisibility(View.GONE);
//                layout_sidebar_startTime_ctrl.setVisibility(View.VISIBLE);
//                layout_sidebar_endTime_ctrl.setVisibility(View.VISIBLE);

                layout_diaglog_box.setVisibility(View.VISIBLE);

            }
        });

        btn_dialog_section_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                layout_sidebar_ctrl.setVisibility(View.VISIBLE);
                layout_sidebar_startTime_ctrl.setVisibility(View.GONE);
                layout_sidebar_endTime_ctrl.setVisibility(View.GONE);
                layout_diaglog_box.setVisibility(View.GONE);

            }
        });

        btn_dialog_section_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_sidebar_ctrl.setVisibility(View.VISIBLE);
                layout_sidebar_startTime_ctrl.setVisibility(View.GONE);
                layout_sidebar_endTime_ctrl.setVisibility(View.GONE);

                layout_diaglog_box.setVisibility(View.GONE);
            }
        });


        layout_sidebar_ctrl_bar = (ConstraintLayout) view.findViewById(R.id.sidebar_ctrl_layout_bar);
        layout_sidebar_blank = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_blank);
        layout_sidebar_blank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                is_timesection_touched = false;
                update_time();
                return false;
            }
        });


        layout_sidebar_ctrl_cur_time_tv.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ResourceType")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                Log.d("sideBar", "layout_sidebar_ctrl_cur_time_tv touch event !!");
                int[] touch_location = new int[2];
                layout_sidebar_total_time.getLocationOnScreen(touch_location);
                int touch_calced_y = (int) (motionEvent.getRawY() - touch_location[1]);

                total_time_len = layout_sidebar_total_time.getHeight();
                double time_rate = Math.max(0, Math.min(1, (double) touch_calced_y / (double) total_time_len));
                sidebar_touch_time = (int) (time_rate * 24 * 60);
                int total_len_buffer = 40;

                if ((touch_calced_y > 0) && (touch_calced_y < total_time_len + total_len_buffer)) {
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

                    App_Section_Color = getResources().getString(R.color.gray);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray);
                } else if (touch_calced_y <= 0) {
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

                    App_Section_Color = getResources().getString(R.color.gray);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray);
                } else if (touch_calced_y >= total_time_len + total_len_buffer) {
                    is_timesection_touched = false;
                    update_time();
                }

                return true;
            }
        });

        layout_sidebar_time_list.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint({"ResourceAsColor", "ResourceType"})
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("sideBar", "layout_sidebar_time_list touch event !!");

                int[] touch_location = new int[2];
                layout_sidebar_total_time.getLocationOnScreen(touch_location);

                int touch_calced_y = (int) (motionEvent.getRawY() - touch_location[1]);

                total_time_len = layout_sidebar_total_time.getHeight();
                double time_rate = Math.max(0, Math.min(1, (double) touch_calced_y / (double) total_time_len));
                sidebar_touch_time = (int) (time_rate * 24 * 60);
                int total_len_buffer = 40;

                if ((touch_calced_y > 0) && (touch_calced_y < total_time_len + total_len_buffer)) {
                    is_timesection_touched = true;
                    sidebar_touch_time = (sidebar_touch_time + 30) / 30 * 30;

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

                    layoutParams = (ConstraintLayout.LayoutParams) layout_sidebar_cur_time.getLayoutParams();
                    layoutParams.height = touch_calced_y;
                    layout_sidebar_cur_time.setLayoutParams(layoutParams);

                    App_Section_Color = getResources().getString(R.color.blue);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue);
                } else if (touch_calced_y <= 0) {
                    is_timesection_touched = true;
                    touch_calced_y = 1;
                    tv_sidebar_ctrl_cur_time_hr.setText("00");
                    tv_sidebar_ctrl_cur_time_min.setText("00");

                    ConstraintLayout.LayoutParams layoutParams
                            = (ConstraintLayout.LayoutParams) layout_sidebar_ctrl.getLayoutParams();
                    layoutParams.topMargin = touch_calced_y - (layout_sidebar_ctrl.getHeight() / 2);
                    layout_sidebar_ctrl.setLayoutParams(layoutParams);

                    App_Section_Color = getResources().getString(R.color.blue);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue);
                } else if (touch_calced_y >= total_time_len + total_len_buffer) {
                    is_timesection_touched = true;
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

                    App_Section_Color = getResources().getString(R.color.blue);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue);
                }
                return true;
            }
        });

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
        calendarView = (CalendarView) view.findViewById(R.id.fragment_calendarview);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart is called");
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
        } else {
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
            @SuppressLint("ResourceType")
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
                    cur_time_len = (int) (time_rate * total_time_len);

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

                    App_Section_Color = getResources().getString(R.color.gray);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray);
                }
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
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
        } else if (color == R.color.red) {
            return R.drawable.border_all_dir_red;
        } else if (color == R.color.green) {
            return R.drawable.border_all_dir_green;
        } else if (color == R.color.black) {
            return R.drawable.border_all_dir_black;
        } else if (color == R.color.yellow) {
            return R.drawable.border_all_dir_yellow;
        } else if (color == R.color.purple) {
            return R.drawable.border_all_dir_purple;
        } else if (color == R.color.sky_blue) {
            return R.drawable.border_all_dir_sky_blue;
        } else if (color == R.color.brown) {
            return R.drawable.border_all_dir_brown;
        } else if (color == R.color.pink) {
            return R.drawable.border_all_dir_pink;
        } else {
            return R.drawable.border_all_dir_light_green;
        }
    }
}