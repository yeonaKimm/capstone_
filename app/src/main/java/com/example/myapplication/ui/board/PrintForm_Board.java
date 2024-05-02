package com.example.myapplication.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardPrintformBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PrintForm_Board extends Fragment {

    private BoardPrintformBinding binding; // 바인딩변수 선언

    public static PrintForm_Board newInstance() {
        return new PrintForm_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardPrintformBinding.inflate(inflater, container, false); // 바인딩 초기화

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}