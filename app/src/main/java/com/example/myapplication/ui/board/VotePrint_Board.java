package com.example.myapplication.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.databinding.BoardVoteprintBinding;

public class VotePrint_Board extends Fragment {

    private BoardVoteprintBinding binding; // 바인딩변수 선언

    public static VotePrint_Board newInstance() {
        return new VotePrint_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardVoteprintBinding.inflate(inflater, container, false); // 바인딩 초기화

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}