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

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitShareprintBinding;

public class SharePrint_Recruit extends Fragment {

    private RecruitShareprintBinding binding; // 바인딩 변수 선언
    private ShareList_Item_Recruit selectedItem; // 선택된 아이템을 멤버 변수로 선언

    public static SharePrint_Recruit newInstance() {
        return new SharePrint_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitShareprintBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemContent.setText(selectedItem.getContent());
                binding.itemPlace.setText(String.valueOf(selectedItem.getPlace()));
                binding.itemQuantity.setText(String.valueOf(selectedItem.getQuantity()));

                // Glide를 사용하여 이미지 로드
                Glide.with(requireContext())
                        .load(selectedItem.getImageUri())
                        .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                        .error(R.drawable.ic_error) // 오류 시 이미지
                        .into(binding.itemImage);
            }
        }

        // share 클릭 시에 이 액션을 트리거하도록 설정함
        binding.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 번들 생성 및 데이터 추가
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem);

                // 액션을 트리거하여 navigation_recruit_shareenter로 이동하면서 번들 전달
                navController.navigate(R.id.action_navigation_recruit_shareprint_to_navigation_recruit_shareenter, bundle);
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