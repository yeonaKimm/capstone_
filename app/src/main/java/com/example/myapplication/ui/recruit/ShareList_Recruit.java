package com.example.myapplication.ui.recruit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitSharelistBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class ShareList_Recruit extends Fragment {

    private RecruitSharelistBinding binding; // 바인딩변수 선언
    private ShareRecruitDBHelper sharedbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitSharelistBinding.inflate(inflater, container, false); // 바인딩 초기화
        View view = binding.getRoot();

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // UI 요소 초기화
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);

        // 데이터베이스 헬퍼 초기화
        sharedbHelper = new ShareRecruitDBHelper(requireContext()); // requireContext()를 사용하여 Fragment의 Context를 가져옵니다.

        // 모든 게시글을 가져와서 리스트에 추가
        List<ShareList_Item_Recruit> sharesList = sharedbHelper.getAllshares();

        // 어댑터를 사용하여 리스트에 데이터 연결
        ShareAdapter shareadapter = new ShareAdapter(requireContext(), sharesList, new ShareAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ShareList_Item_Recruit item) {
                // 클릭 이벤트 처리
            }
        });

        recyclerView.setAdapter(shareadapter);

        // fab 클릭 시에 이 액션을 트리거하도록 설정
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // FAB를 클릭할 때 네비게이션 액션을 트리거하여 navigation_recruit_sharereg로 이동
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_sharelist_to_navigation_recruit_sharereg);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
