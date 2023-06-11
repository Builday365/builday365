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

public class MemoFragment extends Fragment {

    ConstraintLayout layout_diaglog_box;
    Button btn_dialog_section_ok, btn_dialog_section_cancel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_memo, container, false);

        layout_diaglog_box = (ConstraintLayout) view.findViewById(R.id.fragment_dialog_box);
        layout_diaglog_box.setVisibility(View.GONE);
        btn_dialog_section_ok = (Button) view.findViewById((R.id.dialog_section_btn_ok));
        btn_dialog_section_cancel = (Button) view.findViewById((R.id.dialog_section_btn_cancel));


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
