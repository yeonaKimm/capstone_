package com.example.myapplication.ui.board;

import androidx.appcompat.widget.SearchView;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Parcelable;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList; // ArrayList 추가
import java.util.List;

public class MainActivity_Board extends Fragment {

    private BoardMainBinding binding; // 바인딩 변수 선언
    private RecyclerView recyclerViewFree;
    private RecyclerView recyclerViewVote;
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

        recyclerViewFree = view.findViewById(R.id.recycler_view_free);
        recyclerViewVote = view.findViewById(R.id.recycler_view_vote);

        recyclerViewFree.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewVote.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 데이터베이스 헬퍼 초기화
        boardDBHelper = new BoardDBHelper(requireContext());
        voteDBHelper = new VoteDBHelper(requireContext());

        // 자유게시판 데이터 가져오기 및 설정
        List<PostList_Item_Board> postsList = boardDBHelper.getAllPosts();
        BoardAdapter freeAdapter = new BoardAdapter(requireContext(), postsList, new BoardAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PostList_Item_Board item) {
                // 클릭 이벤트 처리
            }
        });
        recyclerViewFree.setAdapter(freeAdapter);

        // 투표게시판 데이터 가져오기 및 설정
        List<VoteList_Item_Board> votesList = voteDBHelper.getAllVotes();
        VoteAdapter voteAdapter = new VoteAdapter(requireContext(), votesList, new VoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(VoteList_Item_Board item) {
                // 클릭 이벤트 처리
            }
        });
        recyclerViewVote.setAdapter(voteAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 필요하다면 여기에서 리소스를 해제할 수 있습니다.
    }
}