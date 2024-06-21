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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitShareenterBinding;

import java.util.ArrayList;
import java.util.List;

public class ShareEnter_Recruit extends Fragment {

    private RecruitShareenterBinding binding; // 바인딩변수 선언
    private List<BuyCommentList_Item_Recruit> commentList;
    private BuyCommentAdapter buycommentAdapter;
    private ShareList_Item_Recruit selectedItem;

    public static ShareEnter_Recruit newInstance() {
        return new ShareEnter_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitShareenterBinding.inflate(inflater, container, false); // 바인딩 초기화

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemContent.setText(selectedItem.getContent());
                binding.itemPlace.setText(String.valueOf(selectedItem.getPlace()));
                binding.itemQuantity.setText(String.valueOf(selectedItem.getQuantity()));
            }
        }

        commentList = new ArrayList<>();
        buycommentAdapter = new BuyCommentAdapter(commentList);
        binding.commentRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.commentRecyclerView.setAdapter(buycommentAdapter);

        binding.sendBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });

        // cancel 클릭 시에 이 액션을 트리거하도록 설정함
        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_shareprint로 이동
                navController.navigate(R.id.action_navigation_recruit_shareenter_to_navigation_recruit_shareprint);
            }
        });

        return binding.getRoot();
    }
    private void sendComment() {
        String commentContent = binding.commentET.getText().toString().trim();
        if (!commentContent.isEmpty()) {
            // 새로운 댓글을 생성하고 어댑터에 추가
            BuyCommentList_Item_Recruit newComment = new BuyCommentList_Item_Recruit(commentContent);
            commentList.add(newComment);
            buycommentAdapter.notifyDataSetChanged();
            binding.commentET.setText("");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}