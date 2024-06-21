package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitMainBinding;
import com.example.myapplication.ui.board.BoardAdapter;
import com.example.myapplication.ui.board.PostList_Item_Board;
import com.example.myapplication.ui.board.VoteAdapter;
import com.example.myapplication.ui.board.VoteList_Item_Board;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Recruit extends Fragment {

    private RecruitMainBinding binding; // 바인딩 변수 선언
    private RecyclerView recyclerViewBuy;
    private RecyclerView recyclerViewShare;
    private BuyRecruitDBHelper buydbHelper;
    private ShareRecruitDBHelper sharedbHelper;

    public static MainActivity_Recruit newInstance() {
        return new MainActivity_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitMainBinding.inflate(inflater, container, false); // 바인딩 초기화

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
        // 공동구매 클릭 시에 이 액션을 트리거하도록 설정
        binding.buyBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_buylist로 이동
                navController.navigate(R.id.action_navigation_recruit_to_navigation_recruit_buylist);
            }
        });

        // 무료나눔 클릭 시에 이 액션을 트리거하도록 설정
        binding.shareBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_sharelist로 이동
                navController.navigate(R.id.action_navigation_recruit_to_navigation_recruit_sharelist);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewBuy = view.findViewById(R.id.recycler_view_buy);
        recyclerViewShare = view.findViewById(R.id.recycler_view_share);

        recyclerViewBuy.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerViewShare.setLayoutManager(new LinearLayoutManager(requireContext()));

        // 데이터베이스 헬퍼 초기화
        buydbHelper = new BuyRecruitDBHelper(requireContext());
        sharedbHelper = new ShareRecruitDBHelper(requireContext());

        // 함께구매 목록 설정
        List<BuyList_Item_Recruit> buysList = buydbHelper.getAllBuys();
        BuyAdapter buyadapter = new BuyAdapter(requireContext(), buysList, new BuyAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BuyList_Item_Recruit item) {
                // 클릭 이벤트 처리
            }
        });
        recyclerViewBuy.setAdapter(buyadapter);

        // 투표게시판 데이터 가져오기 및 설정
        List<ShareList_Item_Recruit> sharesList = sharedbHelper.getAllshares();
        ShareAdapter shareadapter = new ShareAdapter(requireContext(), sharesList, new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShareList_Item_Recruit item) {
                // 클릭 이벤트 처리
            }
        });
        recyclerViewShare.setAdapter(shareadapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
