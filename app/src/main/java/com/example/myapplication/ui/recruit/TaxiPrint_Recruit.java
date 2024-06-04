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

    private RecruitTaxiprintBinding binding; // 바인딩 변수 선언

    public static TaxiPrint_Recruit newInstance() {
        return new TaxiPrint_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitTaxiprintBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            TaxiList_Item_Recruit selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemDate.setText(selectedItem.getDate());
                binding.itemTime.setText(selectedItem.getTime());
                binding.itemPeople.setText(String.valueOf(selectedItem.getPeople()));
            }
        }

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
}
