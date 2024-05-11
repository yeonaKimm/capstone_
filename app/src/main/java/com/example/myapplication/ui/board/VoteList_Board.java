package com.example.myapplication.ui.board;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.BoardVotelistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VoteList_Board extends Fragment {

    private BoardVotelistBinding binding; // 바인딩변수 선언
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private VoteDBHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardVotelistBinding.inflate(inflater, container, false); // 바인딩 초기화
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI 요소 초기화
        listView = view.findViewById(R.id.listView);

        // 데이터베이스 헬퍼 초기화
        dbHelper = new VoteDBHelper(requireContext()); // requireContext()를 사용하여 Fragment의 Context를 가져옵니다.

        // 모든 게시글을 가져와서 리스트에 추가
        List<String> votesList = dbHelper.getAllVotes();

        // 어댑터를 사용하여 리스트에 데이터 연결
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, votesList);
        listView.setAdapter(adapter);

        // fab 클릭 시에 이 액션을 트리거하도록 설정
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB를 클릭할 때 네비게이션 액션을 트리거하여 navigation_board_regform로 이동
                Navigation.findNavController(v).navigate(R.id.action_navigation_board_votelist_to_navigation_board_regvote);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}