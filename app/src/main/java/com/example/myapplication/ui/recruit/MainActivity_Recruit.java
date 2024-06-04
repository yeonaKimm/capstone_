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

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Recruit extends Fragment {

    private RecruitMainBinding binding; // 바인딩 변수 선언

    private ListView recruitlistView;
    private ArrayAdapter<String> adapter; // 하나의 어댑터로 변경
    private BuyRecruitDBHelper buydbHelper;
    private ShareRecruitDBHelper sharedbHelper;
    private TaxiRecruitDBHelper taxidbHelper;

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

        // 함께택시 클릭 시에 이 액션을 트리거하도록 설정
        binding.taxiBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigationrecruit_buylist로 이동
                navController.navigate(R.id.action_navigation_recruit_to_navigation_recruit_taxilist);
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

        // UI 요소 초기화
        recruitlistView = view.findViewById(R.id.recruit_listView);

        // 데이터베이스 헬퍼 초기화
        buydbHelper = new BuyRecruitDBHelper(requireContext());
        sharedbHelper = new ShareRecruitDBHelper(requireContext());
        taxidbHelper = new TaxiRecruitDBHelper(requireContext());// requireContext()를 사용하여 Fragment의 Context를 가져옵니다.

        // 모든 게시글을 가져와서 리스트에 추가
        List<String> buysList = buydbHelper.getAllBuys();
        List<String> SharesList = sharedbHelper.getAllshares();
        //List<String> taxisList = taxidbHelper.getAllTaxis();

        /// 두 리스트를 합치기 위해 하나의 리스트로 결합
        List<String> combinedList = new ArrayList<>();
        combinedList.addAll(buysList);
        combinedList.addAll(SharesList);
        //combinedList.addAll(taxisList);

        // 어댑터를 사용하여 리스트에 데이터 연결
        adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, combinedList);
        recruitlistView.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
