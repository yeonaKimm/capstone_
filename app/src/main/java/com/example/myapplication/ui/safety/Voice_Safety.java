package com.example.myapplication.ui.safety;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SafetyVoiceBinding;


public class Voice_Safety extends Fragment {
    

    private SafetyVoiceBinding binding; // 바인딩변수 선언
    private MediaPlayer mediaPlayer;
    private ImageView playIcon;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = SafetyVoiceBinding.inflate(inflater, container, false); // 바인딩 초기화
        View view = binding.getRoot();

        playIcon = view.findViewById(R.id.play_icon);
        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playSound();
            }
        });

        return view;
    }

    // 이미지뷰 클릭 시 음성 재생
    private void playSound() {
        // 음성 재생
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.nuguseyo); // 여기에 음성 파일 이름을 넣으세요
        mediaPlayer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // MediaPlayer 객체 해제
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
        binding = null;
    }
}

