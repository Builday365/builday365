package com.example.builday365.view;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.builday365.R;
import com.example.builday365.model.Timeline.TimeLine;
import com.example.builday365.viewmodel.SideBarViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UiFragment extends Fragment {
    private static final String TAG = "UiFragment";
    private UiFragmentListener uiFragmentListener;
    private SideBarLayout sideBarLayout;
    SideBarViewModel sideBarViewModel;

    public interface UiFragmentListener {
        void onInputBSent(CharSequence input);
    }

    TextView tv_toolbar_cur_date, tv_google_name, tv_sidebar_cur_time, tv_timesection_cur_time,
            tv_timesection_click_time, tv_timesection_start_time,
            tv_sidebar_ctrl_memo_start_time_hr, tv_sidebar_ctrl_memo_start_time_min,
            tv_sidebar_ctrl_memo_end_time_hr, tv_sidebar_ctrl_memo_end_time_min;
    ImageButton ibtn_calendar, ibtn_day_prev, ibtn_day_next, ibtn_month_prev, ibtn_month_next,
            ibtn_sidebar_memo, ibtn_sidebar_activity,
            ibtn_timesection_ok, ibtn_timesection_cancel, ibtn_timesection_palette;
    DrawerLayout drawerLayout;
    ConstraintLayout layout_timebar, layout_dialog_section,
            layout_sidebar_remain_time,
            layout_time_section;
    LinearLayout
            layout_sidebar_ctrl_layout_memo_start_time_tv, layout_sidebar_ctrl_layout_memo_end_time_tv;
    ImageView iv_google_photo, iv_timesection_ctrl, iv_palette_blue, iv_palette_red, iv_palette_green,
            iv_palette_black, iv_palette_yellow, iv_palette_purple, iv_palette_skyBlue, iv_palette_brown,
            iv_palette_pink, iv_palette_lightGreen;
    Button btn_palette_ok, btn_palette_cancel;
    EditText dialog_section_et_memo;

    int set_year, set_month, set_day;
    Calendar calendar;
    CalendarView calendarView;

    String App_Activity, App_Memo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ui, container, false);


        sideBarViewModel = new ViewModelProvider(this).get(SideBarViewModel.class);
        sideBarLayout = new SideBarLayout(view, sideBarViewModel);

        calendar = Calendar.getInstance();
        view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });

        layout_time_section = (ConstraintLayout) view.findViewById(R.id.fragment_layout_sidebar);
        layout_sidebar_remain_time = (ConstraintLayout) view.findViewById(R.id.sidebar_layout_remain_time);

        tv_toolbar_cur_date = (TextView) view.findViewById(R.id.main_toolbar_tv_cur_date);

        ibtn_calendar = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_calendar);
        ibtn_day_prev = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_day_prev);
        ibtn_day_next = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_day_next);
        ibtn_month_prev = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_month_prev);
        ibtn_month_next = (ImageButton) view.findViewById(R.id.main_toolbar_ibtn_month_next);

        calendarView = (CalendarView) view.findViewById(R.id.fragment_calendarview);

        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sidebarObservers();

        calendarView.setVisibility(View.GONE);

        ViewTreeObserver ibtn_calendar_viewTreeObserver = ibtn_calendar.getViewTreeObserver();
        if (ibtn_calendar_viewTreeObserver.isAlive()) {
            ibtn_calendar_viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ibtn_calendar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    set_date(new Date(calendar.getTimeInMillis()));
                }
            });
        }


    }

    @Override
    public void onStart() {
        Log.d(TAG, "onStart is called");
        sideBarLayout.setContext(getContext());
        sideBarLayout.init();
        tv_toolbar_cur_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                set_date(new Date(calendar.getTimeInMillis()));

                if (sideBarLayout != null) {
                    sideBarLayout.is_timesection_touched = false;
                    sideBarLayout.update_time();
                }

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int cur_visibilty = calendarView.getVisibility();
                calendarView.setVisibility((cur_visibilty == View.VISIBLE) ? View.GONE : View.VISIBLE);
                sideBarLayout.layout_sidebar.setVisibility((cur_visibilty == View.VISIBLE) ? View.VISIBLE : View.GONE);
                layout_time_section.setVisibility(View.GONE);

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
                    setCalendarDay(calendar.get(Calendar.DAY_OF_WEEK));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                calendarView.setVisibility(View.GONE);
                sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
            }
        });
        ibtn_day_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
                }
            }
        });
        ibtn_day_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.DATE, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
                }
            }
        });
        ibtn_month_prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, -1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
                }
            }
        });

        ibtn_month_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.MONTH, +1);
                set_date(new Date(calendar.getTimeInMillis()));

                if (calendarView.getVisibility() == View.VISIBLE) {
                    calendarView.setVisibility(View.GONE);
                    sideBarLayout.layout_sidebar.setVisibility(View.VISIBLE);
                }
            }
        });


        super.onStart();
    }

    private void sidebarObservers() {

        sideBarViewModel.getTimeline().observe(getViewLifecycleOwner(), new Observer<TimeLine>() {
            @Override
            public void onChanged(TimeLine timeLine) {
                Log.d(TAG, "TimeLine is updated !!");

            }
        });


    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UiFragmentListener) {
            uiFragmentListener = (UiFragmentListener) context;

        } else {
            throw new RuntimeException(context.toString() + " must implement FragmentListner");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        uiFragmentListener = null;
    }

    public static Point getLocationOnScreen(View view) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return new Point(location[0], location[1]);
    }

    public void map_cur_location_click_listener() {

        if (sideBarLayout != null) {
            sideBarLayout.is_timesection_touched = false;
            sideBarLayout.update_time();
        }
    }

    public void set_date(Date date) {
        String getCurDate = new SimpleDateFormat("yyyy.MM.dd").format(date);
        calendar.setTime(date);
        setCalendarDay(calendar.get(Calendar.DAY_OF_WEEK));

        if ((getCurDate.split("\\.")).length == 3) {
            set_year = Integer.parseInt(getCurDate.split("\\.")[0]);
            set_month = Integer.parseInt(getCurDate.split("\\.")[1]);
            set_day = Integer.parseInt(getCurDate.split("\\.")[2]);

            tv_toolbar_cur_date.setText(getCurDate);
        } else {
            tv_toolbar_cur_date.setText("Cal Err.");
        }
    }


    private void hideKeyboard() {
        if (getActivity() != null && getActivity().getCurrentFocus() != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void setCalendarDay(int dayWeek) {
        int cal_res_id = 0;
        dayWeek = (dayWeek + 5) % 7;

        switch (dayWeek) {
            case 0:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_0;
                break;
            case 1:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_1;
                break;
            case 2:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_2;
                break;
            case 3:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_3;
                break;
            case 4:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_4;
                break;
            case 5:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_5;
                break;
            case 6:
                cal_res_id = R.drawable.ic_ui_toolbar_cal_6;
                break;
        }

        ibtn_calendar.setImageResource(cal_res_id);
    }
}