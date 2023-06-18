package com.example.builday365.View;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.builday365.R;

public class MemoLayout {
    private static final String TAG = "MemoFragment";

    ConstraintLayout layout_diaglog_box;
    Button btn_dialog_section_ok, btn_dialog_section_cancel;


    MemoLayout(View view)
    {
        layout_diaglog_box = (ConstraintLayout) view.findViewById(R.id.fragment_dialog_box);
        btn_dialog_section_ok = (Button) view.findViewById((R.id.dialog_section_btn_ok));
        btn_dialog_section_cancel = (Button) view.findViewById((R.id.dialog_section_btn_cancel));
        layout_diaglog_box.setVisibility(View.INVISIBLE);
    }

}
