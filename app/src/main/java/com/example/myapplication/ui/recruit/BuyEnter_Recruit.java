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
import com.example.myapplication.databinding.RecruitBuyenterBinding;

import java.util.ArrayList;
import java.util.List;

public class BuyEnter_Recruit extends Fragment {

    private RecruitBuyenterBinding binding;
    private List<BuyCommentList_Item_Recruit> commentList;
    private BuyCommentAdapter buycommentAdapter;
    private BuyList_Item_Recruit selectedItem; // 추가: 선택된 아이템을 멤버 변수로 선언

    public static BuyEnter_Recruit newInstance() {
        return new BuyEnter_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitBuyenterBinding.inflate(inflater, container, false);

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemPrice.setText(String.valueOf(selectedItem.getPrice()));
                binding.itemPeople.setText(String.valueOf(selectedItem.getPeople()));
                binding.itemContent.setText(selectedItem.getContent());
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

        binding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.navigate(R.id.action_navigation_recruit_buyenter_to_navigation_recruit_buyprint);
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
