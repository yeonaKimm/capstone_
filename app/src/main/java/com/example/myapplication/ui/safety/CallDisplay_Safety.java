package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R; // 이 부분은 실제 프로젝트에서 사용하는 리소스 이름으로 변경해야 합니다.

public class CallDisplay_Safety extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // XML 레이아웃을 인플레이트합니다.
        View rootView = inflater.inflate(R.layout.safety_calldisplay, container, false);

        // 인플레이트된 이미지뷰를 참조합니다.
        ImageView imageView = rootView.findViewById(R.id.imageView);

        // 이미지뷰에 이미지를 설정합니다.
        imageView.setImageResource(R.drawable.call);

        return rootView;
    }
}
