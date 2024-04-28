package com.example.myapplication.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.example.myapplication.databinding.BoardPostlistBinding;

public class PostList_Board extends Fragment {

    private BoardPostlistBinding binding; // 바인딩 변수 선언

    public static PostList_Board newInstance() {
        return new PostList_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardPostlistBinding.inflate(inflater, container, false); // 바인딩 초기화

        // SearchView 초기화
        SearchView searchView = binding.searchView;

        // SearchView에 대한 이벤트 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            // 검색 버튼을 누를 때 호출됨
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

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // fab 클릭 시에 이 액션을 트리거하도록 설정
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB를 클릭할 때 네비게이션 액션을 트리거하여 navigation_board_regform로 이동
                Navigation.findNavController(v).navigate(R.id.action_navigation_board_postlist_to_navigation_board_regform);
            }
        });
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
