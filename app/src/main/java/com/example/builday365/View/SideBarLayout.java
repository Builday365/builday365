package com.example.builday365.View;

import android.annotation.SuppressLint;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.builday365.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SideBarLayout {

    private Context context;
    private static final String TAG = "SideBarFragment";

    boolean is_timesection_touched = false;
    int total_time_len, cur_time_len, sidebar_touch_time = 0;
    String App_Section_Color;

    ConstraintLayout layout_sidebar, layout_sidebar_ctrl_bar, layout_sidebar_blank, layout_sidebar_total_time, layout_sidebar_ctrl, layout_sidebar_cur_time, layout_sidebar_startTime_ctrl, layout_sidebar_endTime_ctrl;
    LinearLayout layout_sidebar_ctrl_cur_time_tv, layout_sidebar_time_list, layout_sidebar_ctrl_ui_btn;
    TextView tv_sidebar_ctrl_cur_time_hr, tv_sidebar_ctrl_cur_time_min;
    ImageButton ibtn_sidebar_ctrl_activity, ibtn_sidebar_ctrl_memo;

    SideBarLayout(View view)
    {

        Log.d(TAG,"onCreateView called");

        layout_sidebar = (ConstraintLayout) view.findViewById(R.id.fragment_layout_sidebar_ctrl);
        layout_sidebar_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_ctrl_layout);
        layout_sidebar_ctrl_bar = (ConstraintLayout) view.findViewById(R.id.sidebar_ctrl_layout_bar);
        layout_sidebar_blank = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_blank);
        layout_sidebar_total_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_total_time);
        layout_sidebar_cur_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_cur_time);
        layout_sidebar_startTime_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_startTime_ctrl_layout);
        layout_sidebar_endTime_ctrl = (ConstraintLayout) view.findViewById(R.id.sidebar_endTime_ctrl_layout);

        layout_sidebar_ctrl_ui_btn = (LinearLayout) view.findViewById(R.id.sidebar_ctrl_layout_ui_btn);
        layout_sidebar_ctrl_cur_time_tv = (LinearLayout) view.findViewById(R.id.sidebar_ctrl_layout_cur_time_tv);
        layout_sidebar_time_list = (LinearLayout) view.findViewById(R.id.sidebar_layout_time_list);

        tv_sidebar_ctrl_cur_time_hr = (TextView) view.findViewById(R.id.sidebar_ctrl_tv_cur_time_hr);
        tv_sidebar_ctrl_cur_time_min = (TextView) view.findViewById(R.id.sidebar_ctrl_tv_cur_time_min);

        ibtn_sidebar_ctrl_activity = (ImageButton) view.findViewById(R.id.sidebar_ctrl_ibtn_activity);
        ibtn_sidebar_ctrl_memo = (ImageButton) view.findViewById(R.id.sidebar_ctrl_ibtn_memo);


        layout_sidebar_startTime_ctrl.setVisibility(View.GONE);
        layout_sidebar_endTime_ctrl.setVisibility(View.GONE);

        }

    public void init(){
        Log.d(TAG,"SideBarLayout Init");
        sideBarLayoutListeners();
    }

    public void sideBarLayoutListeners(){
        ViewTreeObserver layout_sidebar_viewTreeObserver = layout_sidebar_total_time.getViewTreeObserver();

        layout_sidebar_blank.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                is_timesection_touched = false;
                update_time();
                return false;
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

                    App_Section_Color = context.getResources().getString(R.color.blue);
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

                    App_Section_Color = context.getResources().getString(R.color.blue);
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

                    App_Section_Color = context.getResources().getString(R.color.blue);
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue);
                    ibtn_sidebar_ctrl_activity.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    ibtn_sidebar_ctrl_memo.setImageTintList(ColorStateList.valueOf(Color.parseColor(App_Section_Color)));
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue);
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue);
                }
                return true;
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

                    App_Section_Color = context.getResources().getString(R.color.gray);
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

                    App_Section_Color = context.getResources().getString(R.color.gray);
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


        if (layout_sidebar_viewTreeObserver.isAlive()) {
            layout_sidebar_viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    layout_sidebar_total_time.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    update_time();
                }
            });
        }

    }

    public void setContext(Context context) {
        this.context = context;
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

                    App_Section_Color = context.getResources().getString(R.color.gray);
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


}
