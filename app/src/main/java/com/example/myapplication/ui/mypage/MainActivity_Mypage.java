package com.example.myapplication.ui.mypage;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;

public class MainActivity_Mypage extends Fragment {

    private DatabaseHelper databaseHelper;
    private TextView textViewNickname, textViewGenderAge, textViewAge, textViewAverageRating, textViewRecentReview, textViewReviewDate;
    private ImageView imageViewProfile, star1, star2, star3, star4, star5;

    public static MainActivity_Mypage newInstance() {
        return new MainActivity_Mypage();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage_main, container, false);

        // 툴바 숨기기
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        databaseHelper = new DatabaseHelper(getContext());

        textViewNickname = view.findViewById(R.id.textViewNickname);
        textViewGenderAge = view.findViewById(R.id.textViewGenderAge);
        textViewAge = view.findViewById(R.id.textViewAge);
        textViewAverageRating = view.findViewById(R.id.textViewAverageRating);
        textViewRecentReview = view.findViewById(R.id.textViewRecentReview);
        textViewReviewDate = view.findViewById(R.id.textViewReviewDate);
        imageViewProfile = view.findViewById(R.id.imageViewProfile);
        star1 = view.findViewById(R.id.star1);
        star2 = view.findViewById(R.id.star2);
        star3 = view.findViewById(R.id.star3);
        star4 = view.findViewById(R.id.star4);
        star5 = view.findViewById(R.id.star5);

        String userId = ((MainActivity) getActivity()).getUserId();
        if (userId != null) {
            loadData(userId);
        }

        view.findViewById(R.id.settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_navigation_mypage_to_navigation_settings);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 툴바 보이기
        if (getActivity() != null && getActivity() instanceof AppCompatActivity) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        }
    }

    private void loadData(String userId) {
        Cursor cursor = databaseHelper.getUserData(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String nickname = getColumnString(cursor, "NICKNAME");
            String gender = getColumnString(cursor, "GENDER");
            String ageRange = getColumnString(cursor, "AGE_RANGE");
            int rating = getColumnInt(cursor, "RATING");
            String recentReview = getColumnString(cursor, "RECENT_REVIEW");
            String reviewDate = getColumnString(cursor, "REVIEW_DATE");
            String profilePicture = getColumnString(cursor, "PROFILE_PICTURE");
            int rankId = getColumnInt(cursor, "RANK_ID");

            textViewNickname.setText(nickname);
            textViewGenderAge.setText(gender.equals("FEMALE") ? "♀ 여 · " : "♂ 남 · ");
            textViewAge.setText(getAgeRangeDisplayText(ageRange));
            textViewAverageRating.setText("평균 " + rating + "점");

            if (recentReview != null && !recentReview.isEmpty()) {
                textViewRecentReview.setText(recentReview);
            } else {
                textViewRecentReview.setText("최근 평가 내역이 없습니다.");
            }

            if (reviewDate != null && !reviewDate.isEmpty()) {
                textViewReviewDate.setText("평가받은 날짜 : " + reviewDate);
            } else {
                textViewReviewDate.setText("평가받은 날짜 : 없음");
            }

            setStarRating(rating);
            setProfileImage(profilePicture, rankId);
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private String getAgeRangeDisplayText(String ageRange) {
        switch (ageRange) {
            case "AGE_10_19":
                return "10대";
            case "AGE_20_29":
                return "20대";
            case "AGE_30_39":
                return "30대";
            case "AGE_40_49":
                return "40대";
            case "AGE_50_59":
                return "50대";
            case "AGE_60_69":
                return "60대";
            case "AGE_70_79":
                return "70대";
            case "AGE_80_89":
                return "80대";
            case "AGE_90_99":
                return "90대";
            default:
                return "연령대 정보 없음";
        }
    }

    private void setProfileImage(String profilePicture, int rankId) {
        int defaultImageResId;
        switch (rankId) {
            case 2:
                defaultImageResId = R.drawable.grade_black;
                break;
            case 3:
                defaultImageResId = R.drawable.grade_nobility;
                break;
            case 4:
                defaultImageResId = R.drawable.grade_king;
                break;
            case 1:
            default:
                defaultImageResId = R.drawable.grade_babe;
                break;
        }

        RequestOptions requestOptions = new RequestOptions()
                .error(defaultImageResId)
                .fallback(defaultImageResId);

        Glide.with(this)
                .load(profilePicture)
                .apply(requestOptions)
                .into(imageViewProfile);
    }

    private String getColumnString(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getString(columnIndex);
        }
        return "";
    }

    private int getColumnInt(Cursor cursor, String columnName) {
        int columnIndex = cursor.getColumnIndex(columnName);
        if (columnIndex != -1) {
            return cursor.getInt(columnIndex);
        }
        return 0;
    }

    private void setStarRating(int rating) {
        ImageView[] stars = {star1, star2, star3, star4, star5};
        for (int i = 0; i < stars.length; i++) {
            if (i < rating) {
                stars[i].setImageResource(R.drawable.ic_star);
            } else {
                stars[i].setImageResource(R.drawable.ic_star_none);
            }
        }
    }
}
