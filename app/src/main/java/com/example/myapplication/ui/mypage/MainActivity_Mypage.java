package com.example.myapplication.ui.mypage;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.navigation.Navigation;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;

public class MainActivity_Mypage extends Fragment {

    private DatabaseHelper databaseHelper;

    public static MainActivity_Mypage newInstance() {
        return new MainActivity_Mypage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_main, container, false);

        view.findViewById(R.id.imageViewSettings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_to_navigation_mypage);
            }
        });

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(requireContext());

        // Load user data
        loadUserData(view);

        return view;
    }

    private void loadUserData(View view) {
        // Replace with the actual user ID
        String userId = "1";

        // Load nickname
        String nickname = databaseHelper.getUserNickname(userId);
        TextView textViewNickname = view.findViewById(R.id.textViewNickname);
        textViewNickname.setText(nickname != null ? nickname : "닉네임");

        // Load gender and age range
        // Replace with actual methods to get these values from the database
        String gender = "여"; // example value
        String ageRange = "20"; // example value

        TextView textViewGenderAge = view.findViewById(R.id.textViewGenderAge);
        if (gender != null) {
            textViewGenderAge.setText(gender.equals("여") ? "♀ 여 · " : "♂ 남 · ");
        }

        TextView textViewAge = view.findViewById(R.id.textViewAge);
        if (ageRange != null) {
            textViewAge.setText(ageRange + "대");
        }

        // Load star ratings and average rating
        // Replace with actual methods to get these values from the database
        int starRating = 4; // example value
        setStarRating(view, starRating);

        TextView textViewAverageRating = view.findViewById(R.id.textViewAverageRating);
        textViewAverageRating.setText("평균 " + (starRating > 0 ? starRating : 0) + "점");

        // Load recent review
        // Replace with actual methods to get these values from the database
        String recentReview = "말투가 친절하고 약속도 잘 지켜서 좋았어요."; // example value
        String reviewDate = "2024-04-25"; // example value

        TextView textViewRecentReview = view.findViewById(R.id.textViewRecentReview);
        textViewRecentReview.setText(recentReview != null ? recentReview : "아직 받은 평가 내역이 없습니다.");

        TextView textViewReviewDate = view.findViewById(R.id.textViewReviewDate);
        if (reviewDate != null) {
            textViewReviewDate.setText("평가받은 날짜 : " + reviewDate);
            textViewReviewDate.setVisibility(View.VISIBLE);
        } else {
            textViewReviewDate.setVisibility(View.GONE);
        }
    }

    private void setStarRating(View view, int starRating) {
        ImageView star1 = view.findViewById(R.id.star1);
        ImageView star2 = view.findViewById(R.id.star2);
        ImageView star3 = view.findViewById(R.id.star3);
        ImageView star4 = view.findViewById(R.id.star4);
        ImageView star5 = view.findViewById(R.id.star5);

        star1.setImageResource(starRating >= 1 ? R.drawable.star_filled : R.drawable.star_outline);
        star2.setImageResource(starRating >= 2 ? R.drawable.star_filled : R.drawable.star_outline);
        star3.setImageResource(starRating >= 3 ? R.drawable.star_filled : R.drawable.star_outline);
        star4.setImageResource(starRating >= 4 ? R.drawable.star_filled : R.drawable.star_outline);
        star5.setImageResource(starRating >= 5 ? R.drawable.star_filled : R.drawable.star_outline);
    }
}
