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
import com.example.myapplication.databinding.RecruitBuyprintBinding;

public class BuyPrint_Recruit extends Fragment {

    private RecruitBuyprintBinding binding;

    public static BuyPrint_Recruit newInstance() {
        return new BuyPrint_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState
    ) {
        binding = RecruitBuyprintBinding.inflate(inflater, container, false);

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            BuyList_Item_Recruit selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemTopic.setText(selectedItem.getTopic());
                //binding.itemContent.setText(selectedItem.getContent());
                binding.itemPrice.setText(String.valueOf(selectedItem.getPrice()));
                binding.itemPeople.setText(String.valueOf(selectedItem.getPeople()));
            }
        }

        // enter 클릭 시에 이 액션을 트리거하도록 설정함
        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_printform로 이동
                navController.navigate(R.id.action_navigation_recruit_buyprint_to_navigation_recruit_buyenter);
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
