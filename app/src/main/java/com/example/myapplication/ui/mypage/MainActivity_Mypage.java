package com.example.myapplication.ui.mypage;

import android.os.AsyncTask;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;

public class MainActivity_Mypage extends Fragment {

    private DatabaseHelper databaseHelper;
    private String userId;

    public static MainActivity_Mypage newInstance() {
        return new MainActivity_Mypage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_main, container, false);

        // Initialize DatabaseHelper
        databaseHelper = new DatabaseHelper(requireContext());

        // Get the user ID from the arguments
        if (getArguments() != null) {
            userId = getArguments().getString("USER_ID", "1");
        } else {
            userId = "1";
        }

        // Load user data asynchronously
        new LoadUserDataTask(view).execute(userId);

        return view;
    }

    private class LoadUserDataTask extends AsyncTask<String, Void, UserData> {
        private View view;

        public LoadUserDataTask(View view) {
            this.view = view;
        }

        @Override
        protected UserData doInBackground(String... strings) {
            String userId = strings[0];
            String nickname = databaseHelper.getUserNickname(userId);
            String gender = "여";
            String ageRange = "20";
            String profilePicture = "https://example.com/profile.jpg";

            return new UserData(nickname, gender, ageRange, profilePicture);
        }

        @Override
        protected void onPostExecute(UserData userData) {
            if (userData != null) {
                TextView textViewNickname = view.findViewById(R.id.textViewNickname);
                textViewNickname.setText(userData.nickname != null ? userData.nickname : "닉네임");

                TextView textViewGenderAge = view.findViewById(R.id.textViewGenderAge);
                if (userData.gender != null) {
                    textViewGenderAge.setText(userData.gender.equals("여") ? "♀ 여 · " : "♂ 남 · ");
                }

                TextView textViewAge = view.findViewById(R.id.textViewAge);
                if (userData.ageRange != null) {
                    textViewAge.setText(userData.ageRange + "대");
                }

                ImageView imageViewProfile = view.findViewById(R.id.imageViewProfile);
                Glide.with(MainActivity_Mypage.this)
                        .load(userData.profilePicture)
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageViewProfile);

                int starRating = 4;
                setStarRating(view, starRating);

                TextView textViewAverageRating = view.findViewById(R.id.textViewAverageRating);
                textViewAverageRating.setText("평균 " + (starRating > 0 ? starRating : 0) + "점");

                String recentReview = "말투가 친절하고 약속도 잘 지켜서 좋았어요.";
                String reviewDate = "2024-04-25";

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

    private static class UserData {
        String nickname;
        String gender;
        String ageRange;
        String profilePicture;

        public UserData(String nickname, String gender, String ageRange, String profilePicture) {
            this.nickname = nickname;
            this.gender = gender;
            this.ageRange = ageRange;
            this.profilePicture = profilePicture;
        }
    }
}
