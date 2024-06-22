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

import com.bumptech.glide.Glide;
import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitShareenterBinding;

import java.util.ArrayList;
import java.util.List;

public class ShareEnter_Recruit extends Fragment {

    private RecruitShareenterBinding binding;
    private List<BuyCommentList_Item_Recruit> commentList;
    private BuyCommentAdapter buycommentAdapter;
    private ShareList_Item_Recruit selectedItem; // 선택된 아이템을 멤버 변수로 선언

    public static ShareEnter_Recruit newInstance() {
        return new ShareEnter_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitShareenterBinding.inflate(inflater, container, false);

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemContent.setText(selectedItem.getContent());
                binding.itemPlace.setText(String.valueOf(selectedItem.getPlace()));
                binding.itemQuantity.setText(String.valueOf(selectedItem.getQuantity()));

                // Glide를 사용하여 이미지 로드
                Glide.with(requireContext())
                        .load(selectedItem.getImageUri())
                        .placeholder(R.drawable.ic_image) // 로딩 중 이미지
                        .error(R.drawable.ic_error) // 오류 시 이미지
                        .into(binding.itemImage);
            }
        }

        // 추가적인 코드 (예: 댓글 기능 등)
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
                // NavController를 사용하여 이전 프래그먼트로 이동하고 데이터 전달
                NavController navController = Navigation.findNavController(v);
                Bundle bundle = new Bundle();
                bundle.putParcelable("selectedItem", selectedItem);
                navController.navigate(R.id.action_navigation_recruit_shareenter_to_navigation_recruit_shareprint, bundle);
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