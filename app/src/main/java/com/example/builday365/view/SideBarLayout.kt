package com.example.builday365.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Handler
import android.os.Message
import android.util.Log
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.builday365.R
import com.example.builday365.viewmodel.SideBarViewModel
import java.text.SimpleDateFormat
import java.util.Date

class SideBarLayout internal constructor(view: View, sideBarViewModel: SideBarViewModel) {
    private var context: Context? = null
    private val sidebarVm: SideBarViewModel
    var is_timesection_touched = false
    var total_time_len = 0
    var cur_time_len = 0
    var sidebar_touch_time = 0
    var App_Section_Color: String? = null

    //memo
    var layout_diaglog_box: ConstraintLayout
    var m_dialog_section_btn_ok: Button
    var m_dialog_section_btn_cancel: Button
    var m_startTime_hr_up_btn: ImageButton
    var m_startTime_hr_down_btn: ImageButton
    var m_startTime_min_up_btn: ImageButton
    var m_startTime_min_down_btn: ImageButton
    var m_endTime_hr_up_btn: ImageButton
    var m_endTime_hr_down_btn: ImageButton
    var m_endTime_min_up_btn: ImageButton
    var m_endTime_min_down_btn: ImageButton

    //sidebar
    var layout_sidebar: ConstraintLayout
    var layout_sidebar_ctrl_bar: ConstraintLayout
    var layout_sidebar_blank: ConstraintLayout
    var layout_sidebar_total_time: ConstraintLayout
    var layout_sidebar_ctrl: ConstraintLayout
    var layout_sidebar_cur_time: ConstraintLayout
    var layout_sidebar_startTime_ctrl: ConstraintLayout
    var layout_sidebar_endTime_ctrl: ConstraintLayout
    var layout_sidebar_ctrl_cur_time_tv: LinearLayout
    var layout_sidebar_time_list: LinearLayout
    var layout_sidebar_ctrl_ui_btn: LinearLayout
    var tv_sidebar_ctrl_cur_time_hr: TextView
    var tv_sidebar_ctrl_cur_time_min: TextView
    var et_dialog_section_et_memo: EditText
    var ibtn_sidebar_ctrl_activity: ImageButton
    var ibtn_sidebar_ctrl_memo: ImageButton

    init {
        Log.d(TAG, "onCreateView called")
        layout_sidebar =
            view.findViewById<View>(R.id.fragment_layout_sidebar_ctrl) as ConstraintLayout
        layout_sidebar_ctrl = view.findViewById<View>(R.id.sidebar_ctrl_layout) as ConstraintLayout
        layout_sidebar_ctrl_bar =
            view.findViewById<View>(R.id.sidebar_ctrl_layout_bar) as ConstraintLayout
        layout_sidebar_blank =
            view.findViewById<View>(R.id.sidebar_layout_blank) as ConstraintLayout
        layout_sidebar_total_time =
            view.findViewById<View>(R.id.sidebar_layout_total_time) as ConstraintLayout
        layout_sidebar_cur_time =
            view.findViewById<View>(R.id.sidebar_layout_cur_time) as ConstraintLayout
        layout_sidebar_startTime_ctrl =
            view.findViewById<View>(R.id.sidebar_startTime_ctrl_layout) as ConstraintLayout
        layout_sidebar_startTime_ctrl.visibility = View.GONE
        layout_sidebar_endTime_ctrl =
            view.findViewById<View>(R.id.sidebar_endTime_ctrl_layout) as ConstraintLayout
        layout_sidebar_endTime_ctrl.visibility = View.GONE
        layout_sidebar_ctrl_ui_btn =
            view.findViewById<View>(R.id.sidebar_ctrl_layout_ui_btn) as LinearLayout
        layout_sidebar_ctrl_cur_time_tv =
            view.findViewById<View>(R.id.sidebar_ctrl_layout_cur_time_tv) as LinearLayout
        layout_sidebar_time_list =
            view.findViewById<View>(R.id.sidebar_layout_time_list) as LinearLayout
        tv_sidebar_ctrl_cur_time_hr =
            view.findViewById<View>(R.id.sidebar_ctrl_tv_cur_time_hr) as TextView
        tv_sidebar_ctrl_cur_time_min =
            view.findViewById<View>(R.id.sidebar_ctrl_tv_cur_time_min) as TextView
        et_dialog_section_et_memo = view.findViewById<View>(R.id.dialog_section_et_memo) as EditText
        ibtn_sidebar_ctrl_activity =
            view.findViewById<View>(R.id.sidebar_ctrl_ibtn_activity) as ImageButton
        ibtn_sidebar_ctrl_memo = view.findViewById<View>(R.id.sidebar_ctrl_ibtn_memo) as ImageButton
        layout_diaglog_box = view.findViewById<View>(R.id.fragment_dialog_box) as ConstraintLayout
        layout_diaglog_box.visibility = View.INVISIBLE
        m_startTime_hr_up_btn =
            view.findViewById<View>(R.id.memo_startTime_hr_up_btn) as ImageButton
        m_startTime_hr_down_btn =
            view.findViewById<View>(R.id.memo_startTime_hr_down_btn) as ImageButton
        m_startTime_min_up_btn =
            view.findViewById<View>(R.id.memo_startTime_min_up_btn) as ImageButton
        m_startTime_min_down_btn =
            view.findViewById<View>(R.id.memo_startTime_min_down_btn) as ImageButton
        m_endTime_hr_up_btn = view.findViewById<View>(R.id.memo_endTime_hr_up_btn) as ImageButton
        m_endTime_hr_down_btn =
            view.findViewById<View>(R.id.memo_endTime_hr_down_btn) as ImageButton
        m_endTime_min_up_btn = view.findViewById<View>(R.id.memo_endTime_min_up_btn) as ImageButton
        m_endTime_min_down_btn =
            view.findViewById<View>(R.id.memo_endTime_min_down_btn) as ImageButton
        m_dialog_section_btn_ok = view.findViewById<View>(R.id.dialog_section_btn_ok) as Button
        m_dialog_section_btn_cancel =
            view.findViewById<View>(R.id.dialog_section_btn_cancel) as Button
        sidebarVm = sideBarViewModel
    }

    fun init() {
        Log.d(TAG, "SideBarLayout Init")
        sideBarLayoutListeners()
        memoElementListeners()
    }

    private fun memoElementListeners() {
        m_startTime_hr_up_btn.setOnClickListener { Log.d(TAG, "m_startTime_hr_up_btn is called") }
        m_startTime_hr_down_btn.setOnClickListener {
            Log.d(
                TAG,
                "m_startTime_hr_down_btn is called"
            )
        }
        m_startTime_min_up_btn.setOnClickListener { Log.d(TAG, "m_startTime_min_up_btn is called") }
        m_startTime_min_down_btn.setOnClickListener {
            Log.d(
                TAG,
                "m_startTime_min_down_btn is called"
            )
        }
        m_endTime_hr_up_btn.setOnClickListener { Log.d(TAG, "m_endTime_hr_up_btn is called") }
        m_endTime_hr_down_btn.setOnClickListener { Log.d(TAG, "m_endTime_hr_down_btn is called") }
        m_endTime_min_up_btn.setOnClickListener { Log.d(TAG, "m_endTime_min_up_btn is called") }
        m_endTime_min_down_btn.setOnClickListener { Log.d(TAG, "m_endTime_min_down_btn is called") }
        m_dialog_section_btn_ok.setOnClickListener {
            Log.d(TAG, "m_dialog_section_btn_ok clicked")
            layout_sidebar_ctrl.visibility = View.VISIBLE
            layout_sidebar_startTime_ctrl.visibility = View.GONE
            layout_sidebar_endTime_ctrl.visibility = View.GONE
            layout_diaglog_box.visibility = View.GONE
            sidebarVm.addMemo(et_dialog_section_et_memo.text.toString(), Date(), Date())
        }
        m_dialog_section_btn_cancel.setOnClickListener {
            layout_sidebar_ctrl.visibility = View.VISIBLE
            layout_sidebar_startTime_ctrl.visibility = View.GONE
            layout_sidebar_endTime_ctrl.visibility = View.GONE
            layout_diaglog_box.visibility = View.GONE
        }
    }

    fun sideBarLayoutListeners() {
        val layout_sidebar_viewTreeObserver = layout_sidebar_total_time.viewTreeObserver
        layout_sidebar_blank.setOnTouchListener { view, motionEvent ->
            is_timesection_touched = false
            update_time()
            false
        }
        layout_sidebar_time_list.setOnTouchListener { view, motionEvent ->
            Log.d("sideBar", "layout_sidebar_time_list touch event !!")
            val touch_location = IntArray(2)
            layout_sidebar_total_time.getLocationOnScreen(touch_location)
            var touch_calced_y = (motionEvent.rawY - touch_location[1]).toInt()
            total_time_len = layout_sidebar_total_time.height
            val time_rate =
                Math.max(0.0, Math.min(1.0, touch_calced_y.toDouble() / total_time_len.toDouble()))
            sidebar_touch_time = (time_rate * 24 * 60).toInt()
            val total_len_buffer = 40
            if (touch_calced_y > 0 && touch_calced_y < total_time_len + total_len_buffer) {
                is_timesection_touched = true
                sidebar_touch_time = (sidebar_touch_time + 30) / 30 * 30
                touch_calced_y = Math.max(0, Math.min(touch_calced_y, total_time_len))
                tv_sidebar_ctrl_cur_time_hr.text = String.format("%02d", sidebar_touch_time / 60)
                tv_sidebar_ctrl_cur_time_min.text = String.format("%02d", sidebar_touch_time % 60)
                var layoutParams = layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = touch_calced_y - layout_sidebar_ctrl.height / 2
                layout_sidebar_ctrl.layoutParams = layoutParams
                layoutParams = layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = touch_calced_y
                layout_sidebar_cur_time.layoutParams = layoutParams
                layoutParams = layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = touch_calced_y
                layout_sidebar_cur_time.layoutParams = layoutParams
                App_Section_Color = context!!.resources.getString(R.color.blue)
                layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue)
                ibtn_sidebar_ctrl_activity.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                ibtn_sidebar_ctrl_memo.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue)
                layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue)
            } else if (touch_calced_y <= 0) {
                is_timesection_touched = true
                touch_calced_y = 1
                tv_sidebar_ctrl_cur_time_hr.text = "00"
                tv_sidebar_ctrl_cur_time_min.text = "00"
                val layoutParams = layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = touch_calced_y - layout_sidebar_ctrl.height / 2
                layout_sidebar_ctrl.layoutParams = layoutParams
                App_Section_Color = context!!.resources.getString(R.color.blue)
                layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue)
                ibtn_sidebar_ctrl_activity.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                ibtn_sidebar_ctrl_memo.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue)
                layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue)
            } else if (touch_calced_y >= total_time_len + total_len_buffer) {
                is_timesection_touched = true
                touch_calced_y = total_time_len
                tv_sidebar_ctrl_cur_time_hr.text = "24"
                tv_sidebar_ctrl_cur_time_min.text = "00"
                var layoutParams = layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = touch_calced_y - layout_sidebar_ctrl.height / 2
                layout_sidebar_ctrl.layoutParams = layoutParams
                layoutParams = layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = touch_calced_y
                layout_sidebar_cur_time.layoutParams = layoutParams
                App_Section_Color = context!!.resources.getString(R.color.blue)
                layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_blue)
                ibtn_sidebar_ctrl_activity.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                ibtn_sidebar_ctrl_memo.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                layout_sidebar_ctrl_bar.setBackgroundResource(R.color.blue)
                layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_blue)
            }
            true
        }
        layout_sidebar_ctrl_cur_time_tv.setOnTouchListener { view, motionEvent ->
            Log.d("sideBar", "layout_sidebar_ctrl_cur_time_tv touch event !!")
            val touch_location = IntArray(2)
            layout_sidebar_total_time.getLocationOnScreen(touch_location)
            var touch_calced_y = (motionEvent.rawY - touch_location[1]).toInt()
            total_time_len = layout_sidebar_total_time.height
            val time_rate =
                Math.max(0.0, Math.min(1.0, touch_calced_y.toDouble() / total_time_len.toDouble()))
            sidebar_touch_time = (time_rate * 24 * 60).toInt()
            val total_len_buffer = 40
            if (touch_calced_y > 0 && touch_calced_y < total_time_len + total_len_buffer) {
                is_timesection_touched = true
                touch_calced_y = Math.max(0, Math.min(touch_calced_y, total_time_len))
                tv_sidebar_ctrl_cur_time_hr.text = String.format("%02d", sidebar_touch_time / 60)
                tv_sidebar_ctrl_cur_time_min.text = String.format("%02d", sidebar_touch_time % 60)
                var layoutParams = layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = touch_calced_y - layout_sidebar_ctrl.height / 2
                layout_sidebar_ctrl.layoutParams = layoutParams
                layoutParams = layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = touch_calced_y
                layout_sidebar_cur_time.layoutParams = layoutParams
                App_Section_Color = context!!.resources.getString(R.color.gray)
                layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray)
                ibtn_sidebar_ctrl_activity.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                ibtn_sidebar_ctrl_memo.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray)
                layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray)
            } else if (touch_calced_y <= 0) {
                touch_calced_y = 1
                tv_sidebar_ctrl_cur_time_hr.text = "00"
                tv_sidebar_ctrl_cur_time_min.text = "00"
                var layoutParams = layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.topMargin = touch_calced_y - layout_sidebar_ctrl.height / 2
                layout_sidebar_ctrl.layoutParams = layoutParams
                layoutParams = layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                layoutParams.height = touch_calced_y
                layout_sidebar_cur_time.layoutParams = layoutParams
                App_Section_Color = context!!.resources.getString(R.color.gray)
                layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray)
                ibtn_sidebar_ctrl_activity.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                ibtn_sidebar_ctrl_memo.imageTintList =
                    ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray)
                layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray)
            } else if (touch_calced_y >= total_time_len + total_len_buffer) {
                is_timesection_touched = false
                update_time()
            }
            true
        }
        ibtn_sidebar_ctrl_memo.setOnClickListener {
            layout_sidebar_ctrl.visibility = View.INVISIBLE
            layout_diaglog_box.visibility = View.VISIBLE
        }
        if (layout_sidebar_viewTreeObserver.isAlive) {
            layout_sidebar_viewTreeObserver.addOnGlobalLayoutListener(object :
                OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    layout_sidebar_total_time.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    update_time()
                }
            })
        }
    }

    fun setContext(context: Context?) {
        this.context = context
    }

    fun get_time(): String {
        val date = Date(System.currentTimeMillis())
        val simpleDateFormat = SimpleDateFormat("HH:mm")
        return simpleDateFormat.format(date)
    }

    fun update_time() {
        val handler: Handler = object : Handler() {
            @SuppressLint("ResourceType")
            override fun handleMessage(message: Message) {
                if (!is_timesection_touched) {
                    val cur_time = get_time()
                    total_time_len = layout_sidebar_total_time.height
                    val cur_hour = cur_time.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[0].toInt()
                    val cur_min = cur_time.split(":".toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()[1].toInt()
                    val time_rate = (cur_hour * 60 + cur_min) / (24 * 60.0)
                    cur_time_len = (time_rate * total_time_len).toInt()
                    tv_sidebar_ctrl_cur_time_hr.text = String.format("%02d", cur_hour)
                    tv_sidebar_ctrl_cur_time_min.text = String.format("%02d", cur_min)
                    var layoutParams =
                        layout_sidebar_ctrl.layoutParams as ConstraintLayout.LayoutParams
                    val layout_sidebar_ctrl_half_height = layout_sidebar_ctrl.height / 2
                    val layout_sidebar_ctrl_top_margin =
                        cur_time_len - layout_sidebar_ctrl_half_height
                    layoutParams.topMargin = Math.max(
                        -layout_sidebar_ctrl_half_height,
                        Math.min(total_time_len, layout_sidebar_ctrl_top_margin)
                    )
                    layout_sidebar_ctrl.layoutParams = layoutParams
                    layoutParams =
                        layout_sidebar_cur_time.layoutParams as ConstraintLayout.LayoutParams
                    layoutParams.height = cur_time_len
                    layout_sidebar_cur_time.layoutParams = layoutParams
                    App_Section_Color = context!!.resources.getString(R.color.gray)
                    layout_sidebar_ctrl_ui_btn.setBackgroundResource(R.drawable.border_button_gray)
                    ibtn_sidebar_ctrl_activity.imageTintList =
                        ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                    ibtn_sidebar_ctrl_memo.imageTintList =
                        ColorStateList.valueOf(Color.parseColor(App_Section_Color))
                    layout_sidebar_ctrl_bar.setBackgroundResource(R.color.gray)
                    layout_sidebar_ctrl_cur_time_tv.setBackgroundResource(R.drawable.border_button_fill_gray)
                }
            }
        }
        val task = Runnable {
            while (true) {
                try {
                    Thread.sleep(500)
                } catch (e: InterruptedException) {
                    Log.e(TAG, "thread error $e")
                }
                handler.sendEmptyMessage(1)
            }
        }
        val thread = Thread(task)
        thread.start()
    }

    companion object {
        private const val TAG = "SideBarFragment"
    }
}