package com.example.myapplication.ui.recruit;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
    private BuyCommentRecruitDBHelper dbHelper;
    private SQLiteDatabase db;

    public static BuyEnter_Recruit newInstance() {
        return new BuyEnter_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitBuyenterBinding.inflate(inflater, container, false);

        dbHelper = new BuyCommentRecruitDBHelper(requireContext());
        db = dbHelper.getWritableDatabase(); // 데이터베이스 쓰기 모드로 열기

        if (getArguments() != null) {
            BuyList_Item_Recruit selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                binding.itemTopic.setText(selectedItem.getTopic());
                binding.itemPrice.setText(String.valueOf(selectedItem.getPrice()));
                binding.itemPeople.setText(String.valueOf(selectedItem.getPeople()));
            }
        }

        commentList = loadCommentsFromDatabase(); // 데이터베이스에서 댓글 로드
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
            insertCommentIntoDatabase(commentContent); // 댓글 데이터베이스에 추가
            commentList.clear(); // 기존 목록 지우기
            commentList.addAll(loadCommentsFromDatabase()); // 새로 로드한 목록으로 업데이트
            buycommentAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
            binding.commentET.setText("");
        }
    }

    private void insertCommentIntoDatabase(String content) {
        // 데이터베이스에 댓글 추가
        String insertQuery = "INSERT INTO " + BuyCommentRecruitDBHelper.TABLE_COMMENTS + " (" +
                BuyCommentRecruitDBHelper.COLUMN_CONTENT + ") VALUES ('" + content + "')";
        db.execSQL(insertQuery);
    }

    private List<BuyCommentList_Item_Recruit> loadCommentsFromDatabase() {
        List<BuyCommentList_Item_Recruit> comments = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + BuyCommentRecruitDBHelper.TABLE_COMMENTS;
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                String content = cursor.getString(cursor.getColumnIndex(BuyCommentRecruitDBHelper.COLUMN_CONTENT));
                BuyCommentList_Item_Recruit comment = new BuyCommentList_Item_Recruit(content);
                comments.add(comment);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return comments;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        db.close(); // 데이터베이스 닫기
    }
}
