package com.example.builday365.Model.Timeline;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TimeLine {
    private HashMap<Date, Memo> memoMap;

    public TimeLine() {
        memoMap = new HashMap<>();
    }

    public void addMemo(Date timeStamp, String memoContent) {
        Memo memo = new Memo(timeStamp, memoContent);
        memoMap.put(timeStamp, memo);
    }

    public Memo getMemoForTimeStamp(Date timeStamp) {
        return memoMap.get(timeStamp);
    }

    public void removeMemo(Date timeStamp) {
        memoMap.remove(timeStamp);
    }

    public Map<Date, Memo> getAllMemos() {
        return new HashMap<>(memoMap);
    }

}
