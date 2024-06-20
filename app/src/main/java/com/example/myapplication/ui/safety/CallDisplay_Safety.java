package com.example.myapplication.ui.safety;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

import java.util.Locale;

public class CallDisplay_Safety extends Fragment {

    private int voteCountOption1 = 0;
    private int voteCountOption2 = 0;

    private View barFillOption1;
    private View barFillOption2;

    private TextView tvBarLabel1;
    private TextView tvBarLabel2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.safety_calldisplay, container, false);

        // XML에서 뷰 참조
        barFillOption1 = rootView.findViewById(R.id.barFillOption1);
        barFillOption2 = rootView.findViewById(R.id.barFillOption2);
        tvBarLabel1 = rootView.findViewById(R.id.tvBarLabel1);
        tvBarLabel2 = rootView.findViewById(R.id.tvBarLabel2);

        // 초기 막대 그래프 설정
        updateGraph();

        // 막대 그래프 클릭 이벤트 설정
        rootView.findViewById(R.id.barContainer1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteCountOption1++;
                updateGraph();
            }
        });

        rootView.findViewById(R.id.barContainer2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                voteCountOption2++;
                updateGraph();
            }
        });

        return rootView;
    }

    private void updateGraph() {
        // 전체 투표 수 계산
        int totalVotes = voteCountOption1 + voteCountOption2;

        // 비율 계산
        float ratioOption1 = totalVotes > 0 ? (float) voteCountOption1 / totalVotes : 0;
        float ratioOption2 = totalVotes > 0 ? (float) voteCountOption2 / totalVotes : 0;

        // 막대 그래프 비율 조정
        LinearLayout.LayoutParams paramsOption1 = (LinearLayout.LayoutParams) barFillOption1.getLayoutParams();
        paramsOption1.weight = ratioOption1;
        barFillOption1.setLayoutParams(paramsOption1);

        LinearLayout.LayoutParams paramsOption2 = (LinearLayout.LayoutParams) barFillOption2.getLayoutParams();
        paramsOption2.weight = ratioOption2;
        barFillOption2.setLayoutParams(paramsOption2);

        // 막대 그래프 색상 조정
        int option1Color = ratioOption1 > 0 ? Color.parseColor("#B6DDFF") : Color.TRANSPARENT;
        int option2Color = ratioOption2 > 0 ? Color.parseColor("#B6DDFF") : Color.TRANSPARENT;

        barFillOption1.setBackgroundColor(option1Color);
        barFillOption2.setBackgroundColor(option2Color);

        // 막대 레이블 업데이트
        tvBarLabel1.setText(String.format(Locale.getDefault(), "%.0f%%", ratioOption1 * 100));
        tvBarLabel2.setText(String.format(Locale.getDefault(), "%.0f%%", ratioOption2 * 100));
    }
}
