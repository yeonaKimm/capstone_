package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.SafetyMainBinding;

public class MainActivity_Safety extends Fragment {

    private SafetyMainBinding binding; // 바인딩 변수 선언

    public static MainActivity_Safety newInstance() {
        return new MainActivity_Safety();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = SafetyMainBinding.inflate(inflater, container, false); // 바인딩 초기화

        // SearchView 초기화
        SearchView searchView = binding.searchView;

        // SearchView에 대한 이벤트 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 검색 버튼을 누를때 호출됨
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query != null && !query.isEmpty()) {
                    // 검색어를 사용하여 작업 수행
                }
                return true; // 이벤트가 처리되었음을 반환
            }

            // 검색어가 변경될 때 호출됨
            @Override
            public boolean onQueryTextChange(String newText) {
                // newText를 사용하여 필요한 작업을 수행
                return false;
            }
        });

        // 안전귀가 클릭 시에 이 액션을 트리거하도록 설정
        binding.safetyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_postlist로 이동
                navController.navigate(R.id.action_navigation_board_to_navigation_board_postlist);
            }
        });

        // 통화화면 클릭 시에 이 액션을 트리거하도록 설정
        binding.callBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_votelist로 이동
                navController.navigate(R.id.action_navigation_board_to_navigation_board_votelist);
            }
        });

        // 음성지원 클릭 시에 이 액션을 트리거하도록 설정
        binding.voiceBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_votelist로 이동
                navController.navigate(R.id.action_navigation_board_to_navigation_board_votelist);
            }
        });

        // 비상연락망 클릭 시에 이 액션을 트리거하도록 설정
        binding.contactBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_votelist로 이동
                navController.navigate(R.id.action_navigation_satety_to_navigation_safety_contact);
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
