package com.example.myapplication.ui.safety;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class CallDisplay_Safety extends Fragment {

    private Button button;
    private MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.safety_calldisplay, container, false);

        button = root.findViewById(R.id.button);

        // MediaPlayer 초기화
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.play_police); // 여기서 R.raw.play_police는 재생할 오디오 파일의 리소스 ID입니다.

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 버튼 클릭 시 음성 재생
                if (mediaPlayer != null) {
                    mediaPlayer.start();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 프래그먼트 종료 시 MediaPlayer 해제
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
