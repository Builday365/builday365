package com.example.builday365.model.test

import java.util.Date

class Memo(var startTime: Date, var endTime: Date, var memoContent: String) {

    override fun toString(): String {
        return "Memo{" +
                "startTime=" + startTime +
                ", endTime=" + endTime +
                ", memoContent='" + memoContent + '\'' +
                '}'
    }
}