package com.example.builday365.view

import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.inputmethod.InputMethodManager
import android.widget.CalendarView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.builday365.R
import com.example.builday365.viewmodel.SideBarViewModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class UiFragment : Fragment() {
    private var uiFragmentListener: UiFragmentListener? = null
    private var sideBarLayout: SideBarLayout? = null
    var sideBarViewModel: SideBarViewModel? = null

    interface UiFragmentListener {
        fun onInputBSent(input: CharSequence?)
    }

    var tv_toolbar_cur_date: TextView? = null
    var menu_main_tv_user_name: TextView? = null
    var ibtn_side_menu: ImageButton? = null
    var ibtn_calendar: ImageButton? = null
    var ibtn_day_prev: ImageButton? = null
    var ibtn_day_next: ImageButton? = null
    var ibtn_month_prev: ImageButton? = null
    var ibtn_month_next: ImageButton? = null
    var menu_main_iv_user_pic: ImageView? = null
    var layout_sidebar_remain_time: ConstraintLayout? = null
    var layout_time_section: ConstraintLayout? = null
    var fragment_main_menu: ConstraintLayout? = null
    var menu_main_layout_general: LinearLayout? = null
    var menu_main_layout_data: LinearLayout? = null
    var menu_main_layout_analysis: LinearLayout? = null
    var menu_main_layout_gps: LinearLayout? = null
    var menu_main_layout_sensor: LinearLayout? = null
    var menu_main_layout_account: LinearLayout? = null
    var menu_main_layout_about: LinearLayout? = null
    var set_year = 0
    var set_month = 0
    var set_day = 0
    var calendar: Calendar? = null
    var calendarView: CalendarView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ui, container, false)
        sideBarViewModel = ViewModelProvider(this).get(SideBarViewModel::class.java)
        sideBarLayout = SideBarLayout(view, sideBarViewModel!!)
        ibtn_side_menu = view.findViewById<View>(R.id.main_toolbar_ibtn_side_menu) as ImageButton
        fragment_main_menu = view.findViewById<View>(R.id.fragment_main_menu) as ConstraintLayout
        menu_main_layout_general =
            view.findViewById<View>(R.id.menu_main_layout_general) as LinearLayout
        menu_main_layout_data = view.findViewById<View>(R.id.menu_main_layout_data) as LinearLayout
        menu_main_layout_analysis =
            view.findViewById<View>(R.id.menu_main_layout_analysis) as LinearLayout
        menu_main_layout_gps = view.findViewById<View>(R.id.menu_main_layout_gps) as LinearLayout
        menu_main_layout_sensor =
            view.findViewById<View>(R.id.menu_main_layout_sensor) as LinearLayout
        menu_main_layout_account =
            view.findViewById<View>(R.id.menu_main_layout_account) as LinearLayout
        menu_main_layout_about =
            view.findViewById<View>(R.id.menu_main_layout_about) as LinearLayout
        menu_main_tv_user_name = view.findViewById<View>(R.id.menu_main_tv_user_name) as TextView
        menu_main_iv_user_pic = view.findViewById<View>(R.id.menu_main_iv_user_pic) as ImageView
        fragment_main_menu!!.visibility = View.GONE
        val google_name = requireActivity().intent.extras!!.getString("google_name")
        val google_photo_url = requireActivity().intent.extras!!.getString("google_photo")

        // FIX when DB Level is Done
        val user_level = 1
        menu_main_tv_user_name!!.text = "Lv." + Integer.toString(user_level) + " " + google_name
        Glide.with(this).load(google_photo_url).circleCrop().into(menu_main_iv_user_pic!!)
        ibtn_side_menu!!.setOnClickListener {
            if (fragment_main_menu!!.visibility == View.VISIBLE) {
                fragment_main_menu!!.visibility = View.GONE
            } else {
                fragment_main_menu!!.visibility = View.VISIBLE
            }
        }
        calendar = Calendar.getInstance()
        view.setOnTouchListener { v, event ->
            hideKeyboard()
            false
        }
        layout_time_section =
            view.findViewById<View>(R.id.fragment_layout_sidebar) as ConstraintLayout
        layout_sidebar_remain_time =
            view.findViewById<View>(R.id.sidebar_layout_remain_time) as ConstraintLayout
        tv_toolbar_cur_date = view.findViewById<View>(R.id.main_toolbar_tv_cur_date) as TextView
        ibtn_calendar = view.findViewById<View>(R.id.main_toolbar_ibtn_calendar) as ImageButton
        ibtn_day_prev = view.findViewById<View>(R.id.main_toolbar_ibtn_day_prev) as ImageButton
        ibtn_day_next = view.findViewById<View>(R.id.main_toolbar_ibtn_day_next) as ImageButton
        ibtn_month_prev = view.findViewById<View>(R.id.main_toolbar_ibtn_month_prev) as ImageButton
        ibtn_month_next = view.findViewById<View>(R.id.main_toolbar_ibtn_month_next) as ImageButton
        calendarView = view.findViewById<View>(R.id.fragment_calendarview) as CalendarView
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sidebarObservers()
        calendarView!!.visibility = View.GONE
        val ibtn_calendar_viewTreeObserver = ibtn_calendar!!.viewTreeObserver
        if (ibtn_calendar_viewTreeObserver.isAlive) {
            ibtn_calendar_viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    ibtn_calendar!!.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    set_date(Date(calendar!!.timeInMillis))
                }
            })
        }
    }

    override fun onStart() {
        Log.d(TAG, "onStart is called")
        sideBarLayout!!.setContext(context)
        sideBarLayout!!.init()
        tv_toolbar_cur_date!!.setOnClickListener {
            calendar = Calendar.getInstance()
            set_date(Date(calendar.getTimeInMillis()))
            if (sideBarLayout != null) {
                sideBarLayout!!.is_timesection_touched = false
                sideBarLayout!!.update_time()
            }
            if (calendarView!!.visibility == View.VISIBLE) {
                calendarView!!.visibility = View.GONE
                sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
            }
        }
        ibtn_calendar!!.setOnClickListener {
            val cur_visibilty = calendarView!!.visibility
            calendarView!!.visibility =
                if (cur_visibilty == View.VISIBLE) View.GONE else View.VISIBLE
            sideBarLayout!!.layout_sidebar.visibility =
                if (cur_visibilty == View.VISIBLE) View.VISIBLE else View.GONE
            layout_time_section!!.visibility = View.GONE
            val date_str = String.format("%d-%d-%d", set_year, set_month, set_day)
            val transFormat = SimpleDateFormat("yyyy-MM-dd")
            try {
                val date = transFormat.parse(date_str)
                calendarView!!.date = date.time
            } catch (e: ParseException) {
                e.printStackTrace()
            }
        }
        calendarView!!.setOnDateChangeListener { calendarView, year, month, day ->
            tv_toolbar_cur_date!!.text = String.format("%d.%02d.%02d", year, month + 1, day)
            set_year = year
            set_month = month + 1
            set_day = day
            val date_str = String.format("%d-%d-%d", set_year, set_month, set_day)
            val transFormat = SimpleDateFormat("yyyy-MM-dd")
            try {
                val date = transFormat.parse(date_str)
                calendar!!.time = date
                setCalendarDay(calendar!![Calendar.DAY_OF_WEEK])
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            calendarView.visibility = View.GONE
            sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
        }
        ibtn_day_prev!!.setOnClickListener {
            calendar!!.add(Calendar.DATE, -1)
            set_date(Date(calendar!!.timeInMillis))
            if (calendarView!!.visibility == View.VISIBLE) {
                calendarView!!.visibility = View.GONE
                sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
            }
        }
        ibtn_day_next!!.setOnClickListener {
            calendar!!.add(Calendar.DATE, +1)
            set_date(Date(calendar!!.timeInMillis))
            if (calendarView!!.visibility == View.VISIBLE) {
                calendarView!!.visibility = View.GONE
                sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
            }
        }
        ibtn_month_prev!!.setOnClickListener {
            calendar!!.add(Calendar.MONTH, -1)
            set_date(Date(calendar!!.timeInMillis))
            if (calendarView!!.visibility == View.VISIBLE) {
                calendarView!!.visibility = View.GONE
                sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
            }
        }
        ibtn_month_next!!.setOnClickListener {
            calendar!!.add(Calendar.MONTH, +1)
            set_date(Date(calendar!!.timeInMillis))
            if (calendarView!!.visibility == View.VISIBLE) {
                calendarView!!.visibility = View.GONE
                sideBarLayout!!.layout_sidebar.visibility = View.VISIBLE
            }
        }
        super.onStart()
    }

    private fun sidebarObservers() {
        sideBarViewModel!!.timeline.observe(viewLifecycleOwner) {
            Log.d(
                TAG,
                "TimeLine is updated !!"
            )
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        uiFragmentListener = if (context is UiFragmentListener) {
            context
        } else {
            throw RuntimeException("$context must implement FragmentListner")
        }
    }

    override fun onDetach() {
        super.onDetach()
        uiFragmentListener = null
    }

    fun map_cur_location_click_listener() {
        if (sideBarLayout != null) {
            sideBarLayout!!.is_timesection_touched = false
            sideBarLayout!!.update_time()
        }
    }

    fun set_date(date: Date?) {
        val getCurDate = SimpleDateFormat("yyyy.MM.dd").format(date)
        calendar!!.time = date
        setCalendarDay(calendar!![Calendar.DAY_OF_WEEK])
        if (getCurDate.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray().size == 3) {
            set_year = getCurDate.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[0].toInt()
            set_month = getCurDate.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[1].toInt()
            set_day = getCurDate.split("\\.".toRegex()).dropLastWhile { it.isEmpty() }
                .toTypedArray()[2].toInt()
            tv_toolbar_cur_date!!.text = getCurDate
        } else {
            tv_toolbar_cur_date!!.text = "Cal Err."
        }
    }

    private fun hideKeyboard() {
        if (activity != null && requireActivity().currentFocus != null) {
            val inputManager =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(
                requireActivity().currentFocus!!.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }

    private fun setCalendarDay(dayWeek: Int) {
        var dayWeek = dayWeek
        var cal_res_id = 0
        dayWeek = (dayWeek + 5) % 7
        when (dayWeek) {
            0 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_0
            1 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_1
            2 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_2
            3 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_3
            4 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_4
            5 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_5
            6 -> cal_res_id = R.drawable.ic_ui_toolbar_cal_6
        }
        ibtn_calendar!!.setImageResource(cal_res_id)
    }

    companion object {
        private const val TAG = "UiFragment"
        fun getLocationOnScreen(view: View): Point {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            return Point(location[0], location[1])
        }
    }
}