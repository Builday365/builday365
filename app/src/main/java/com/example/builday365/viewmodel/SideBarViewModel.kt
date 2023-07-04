package com.example.builday365.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.builday365.model.test.Memo
import com.example.builday365.model.test.TimeLine
import java.util.Date

class SideBarViewModel : ViewModel() {
    private val memo = MutableLiveData<String>()
    private val mtimeline = MutableLiveData<TimeLine>()
    var m_startTime_hr = 0
    val timeline: LiveData<TimeLine>
        get() = mtimeline

    // Method to add a memo
    fun addMemo(memoContent: String?, stime: Date?, etime: Date?) {
        var currentTimeLine = mtimeline.value
        if (currentTimeLine == null) {
            currentTimeLine = TimeLine()
        }
        val newMemo = Memo(stime, etime, memoContent)
        currentTimeLine.curMemos.add(newMemo)
        mtimeline.value = currentTimeLine
    }
}