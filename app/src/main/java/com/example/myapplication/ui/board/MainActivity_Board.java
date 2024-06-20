package com.example.myapplication.ui.board;

import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.myapplication.databinding.BoardMainBinding;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import androidx.navigation.fragment.NavHostFragment;

import java.util.ArrayList; // ArrayList 추가
import java.util.List;

public class MainActivity_Board extends Fragment {

    private BoardMainBinding binding; // 바인딩 변수 선언
    private ListView boardlistView;
    private ArrayAdapter<String> adapter; // 하나의 어댑터로 변경
    private BoardDBHelper boardDBHelper;
    private VoteDBHelper voteDBHelper;

    public static MainActivity_Board newInstance() {
        return new MainActivity_Board();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = BoardMainBinding.inflate(inflater, container, false); // 바인딩 초기화

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

        // 자유게시판 클릭 시에 이 액션을 트리거하도록 설정
        binding.freeBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_postlist로 이동
                navController.navigate(R.id.action_navigation_board_to_navigation_board_postlist);
            }
        });

        // 투표게시판 클릭 시에 이 액션을 트리거하도록 설정
        binding.voteBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_board_votelist로 이동
                navController.navigate(R.id.action_navigation_board_to_navigation_board_votelist);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 전체 게시글 //
        // UI 요소 초기화
        boardlistView = view.findViewById(R.id.board_listView);

        // 데이터베이스 헬퍼 초기화
        boardDBHelper = new BoardDBHelper(requireContext());
        voteDBHelper = new VoteDBHelper(requireContext()); // requireContext()를 사용하여 Fragment의 Context를 가져옵니다.

        // 모든 게시글을 가져와서 리스트에 추가
        //List<String> postsList = boardDBHelper.getAllPosts();
        //List<String> votesList = voteDBHelper.getAllVotes();

        // 두 리스트를 합치기 위해 하나의 리스트로 결합
        List<String> combinedList = new ArrayList<>();
        //combinedList.addAll(postsList);
        //combinedList.addAll(votesList);

        // 어댑터를 사용하여 리스트에 데이터 연결
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, combinedList);
        boardlistView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
