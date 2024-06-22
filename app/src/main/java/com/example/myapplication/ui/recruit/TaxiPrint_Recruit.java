package com.example.myapplication.ui.recruit;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitTaxiprintBinding;
import com.example.myapplication.ui.Login.DatabaseHelper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TaxiPrint_Recruit extends Fragment {

    private RecruitTaxiprintBinding binding; // 바인딩 변수 선언
    private DatabaseHelper databaseHelper;
    private String userId;
    private Handler handler;
    private Runnable updateRemainingTimeRunnable;

    public static TaxiPrint_Recruit newInstance() {
        return new TaxiPrint_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitTaxiprintBinding.inflate(inflater, container, false); // 바인딩 초기화

        databaseHelper = new DatabaseHelper(requireContext());

        // MainActivity에서 userId 가져오기
        userId = ((MainActivity) getActivity()).getUserId();
        if (userId != null) {
            loadUserProfile(userId);
        }

        // 번들에서 전달된 데이터 가져오기
        if (getArguments() != null) {
            TaxiList_Item_Recruit selectedItem = getArguments().getParcelable("selectedItem");
            if (selectedItem != null) {
                // UI 요소에 데이터 설정
                binding.itemDate.setText(selectedItem.getDate());
                binding.itemTime.setText(selectedItem.getTime());
                binding.itemPeople.setText(String.valueOf(selectedItem.getPeople()));
                binding.start.setText(selectedItem.getStartLocation());
                binding.end.setText(selectedItem.getEndLocation());

                // 남은 시간을 업데이트하는 핸들러와 Runnable 설정
                handler = new Handler();
                updateRemainingTimeRunnable = new Runnable() {
                    @Override
                    public void run() {
                        updateRemainingTime(selectedItem.getDate(), selectedItem.getTime());
                        handler.postDelayed(this, 60000); // 1분마다 업데이트
                    }
                };
                handler.post(updateRemainingTimeRunnable);
            }
        }

        // enter 클릭 시에 이 액션을 트리거하도록 설정함
        binding.enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // NavController를 가져옴
                NavController navController = Navigation.findNavController(v);

                // 액션을 트리거하여 navigation_recruit_taxilist로 이동
                navController.navigate(R.id.action_navigation_recruit_taxiprint_to_navigation_recruit_taxilist);
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (handler != null && updateRemainingTimeRunnable != null) {
            handler.removeCallbacks(updateRemainingTimeRunnable);
        }
    }

    private void loadUserProfile(String userId) {
        Cursor cursor = databaseHelper.getUserData(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String nickname = getColumnString(cursor, "NICKNAME");
            String gender = getColumnString(cursor, "GENDER");
            String ageRange = getColumnString(cursor, "AGE_RANGE");
            String profilePicture = getColumnString(cursor, "PROFILE_PICTURE");
            int rankId = getColumnInt(cursor, "RANK_ID");

            binding.profileNickname.setText(nickname);
            binding.profileGenderAge.setText((gender.equals("FEMALE") ? "♀ 여, " : "♂ 남, ") + getAgeRangeDisplayText(ageRange));
            setProfileImage(profilePicture, rankId, binding.profileImage);
        } else {
            binding.profileNickname.setText("Unknown");
            binding.profileGenderAge.setText("");
            binding.profileImage.setImageResource(R.drawable.grade_babe); // 기본 이미지 리소스 사용
        }

        if (cursor != null) {
            cursor.close();
        }
    }

    private void setProfileImage(String profilePicture, int rankId, ImageView imageView) {
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
                .into(imageView);
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

    private void updateRemainingTime(String date, String time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        try {
            Date now = new Date();
            Date tripTime = dateFormat.parse(date + " " + time);
            if (tripTime != null) {
                long diff = tripTime.getTime() - now.getTime();
                long minutes = diff / (1000 * 60);
                long hours = minutes / 60;
                minutes = minutes % 60;
                long days = hours / 24;
                hours = hours % 24;

                String remainingTime;
                if (days > 0) {
                    remainingTime = String.format(Locale.getDefault(), "%d일 %d시간 %d분 남음", days, hours, minutes);
                } else if (hours > 0) {
                    remainingTime = String.format(Locale.getDefault(), "%d시간 %d분 남음", hours, minutes);
                } else {
                    remainingTime = String.format(Locale.getDefault(), "%d분 남음", minutes);
                }

                binding.retime.setText(remainingTime);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
