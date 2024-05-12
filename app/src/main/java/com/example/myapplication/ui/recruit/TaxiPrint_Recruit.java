package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitTaxiprintBinding;

public class TaxiPrint_Recruit extends Fragment {

    private RecruitTaxiprintBinding binding; // 바인딩변수 선언

    public static TaxiPrint_Recruit newInstance() {
        return new TaxiPrint_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitTaxiprintBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 데이터베이스에서 값 가져오기 (가정)
        String date = getDateFromDatabase(); // 탑승일
        String time = getTimeFromDatabase(); // 탑승시간
        String people = getPeopleFromDatabase(); // 탑승인원

        // 각 TextView에 데이터베이스에서 가져온 값 설정
        binding.date.setText(date);
        binding.time.setText(time);
        binding.people.setText(people);

        // enter 클릭 시에 이 액션을 트리거하도록 설정함
        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_taxilist로 이동
                navController.navigate(R.id.action_navigation_recruit_taxiprint_to_navigation_recruit_taxilist);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 가정한 데이터베이스에서 값 가져오는 메소드들
    private String getDateFromDatabase() {
        // 여기에 데이터베이스에서 탑승일 값을 가져오는 로직 작성
        return "20XX-XX-XX";
    }

    private String getTimeFromDatabase() {
        // 여기에 데이터베이스에서 탑승시간 값을 가져오는 로직 작성
        return "00:00";
    }

    private String getPeopleFromDatabase() {
        // 여기에 데이터베이스에서 탑승인원 값을 가져오는 로직 작성
        return "N 명";
    }

}
