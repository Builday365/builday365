package com.example.builday365.model.timeline

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
class TimeLine {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
        private set
    var curTime: DateTime
    var curMemos: List<Memo>

    constructor() {
        id = System.currentTimeMillis()
        curTime = DateTime(Date())
        curMemos = ArrayList()
    }

    constructor(curTime: DateTime, curMemos: List<Memo>) {
        this.curTime = curTime
        this.curMemos = curMemos
    }

    fun setId(id: Int) {
        this.id = id.toLong()
    }

    override fun toString(): String {
        return "TimeLine{" +
                "id=" + id +
                ", curTime=" + curTime +
                ", curMemos=" + curMemos +
                '}'
    }
}