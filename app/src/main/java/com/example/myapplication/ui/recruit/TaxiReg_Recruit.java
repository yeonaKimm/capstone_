package com.example.myapplication.ui.recruit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;

import java.util.Calendar;

public class TaxiReg_Recruit extends Fragment {

    private String[] peopleOptions = {"n", "1", "2", "3", "4"};
    private Calendar calendar;
    private EditText startEditText;
    private EditText endEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private Spinner peopleSpinner;
    private Button registerButton;
    private ImageView profileImageView;
    private TextView nicknameTextView;
    private TextView genderAgeTextView;
    private DatabaseHelper databaseHelper;
    private String userId;

    private final ActivityResultLauncher<Intent> startForResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == getActivity().RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    String currentLocation = data.getStringExtra("currentLocation");
                    String destination = data.getStringExtra("destination");
                    if (currentLocation != null && !currentLocation.isEmpty()) {
                        startEditText.setText(currentLocation);
                    }
                    if (destination != null && !destination.isEmpty()) {
                        endEditText.setText(destination);
                    }
                }
            });

    public static TaxiReg_Recruit newInstance() {
        return new TaxiReg_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recruit_taxireg, container, false);

        startEditText = view.findViewById(R.id.start);
        endEditText = view.findViewById(R.id.end);
        dateTextView = view.findViewById(R.id.date);
        timeTextView = view.findViewById(R.id.time);
        peopleSpinner = view.findViewById(R.id.spinner_people);
        registerButton = view.findViewById(R.id.registerButton);
        profileImageView = view.findViewById(R.id.profile_image);
        nicknameTextView = view.findViewById(R.id.profile_nickname);
        genderAgeTextView = view.findViewById(R.id.profile_gender_age);

        databaseHelper = new DatabaseHelper(requireContext());

        // MainActivity에서 userId 가져오기
        userId = ((MainActivity) getActivity()).getUserId();
        if (userId != null) {
            loadUserProfile(userId);
        }

        startEditText.setOnClickListener(v -> openTexiRouteActivity());
        endEditText.setOnClickListener(v -> openTexiRouteActivity());

        ArrayAdapter<CharSequence> peopleAdapter = new ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_spinner_item, peopleOptions) {
            @Override
            public boolean isEnabled(int position) {
                return position != 0;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        peopleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        peopleSpinner.setAdapter(peopleAdapter);

        view.findViewById(R.id.date).setOnClickListener(v -> showDatePickerDialog());
        view.findViewById(R.id.time).setOnClickListener(v -> showTimePickerDialog());

        registerButton.setOnClickListener(v -> {
            String startLocation = startEditText.getText().toString();
            String endLocation = endEditText.getText().toString();
            if (startLocation.isEmpty() || endLocation.isEmpty()) {
                Toast.makeText(getContext(), "출발지와 목적지 모두 설정해주세요", Toast.LENGTH_SHORT).show();
                return;
            }
            String date = dateTextView.getText().toString();
            String time = timeTextView.getText().toString();
            int people = Integer.parseInt(peopleOptions[peopleSpinner.getSelectedItemPosition()]);

            TaxiRecruitDBHelper dbHelper = new TaxiRecruitDBHelper(getContext());
            dbHelper.insertTaxi(date, time, people, startLocation, endLocation);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_recruit_taxireg_to_navigation_recruit_taxilist);
        });

        return view;
    }

    private void openTexiRouteActivity() {
        Intent intent = new Intent(getActivity(), texi_route.class);
        startForResultLauncher.launch(intent);
    }

    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> dateTextView.setText(String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth)), year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> timeTextView.setText(String.format("%02d:%02d", hourOfDay, minute1)), hour, minute, true);
        timePickerDialog.show();
    }

    private void loadUserProfile(String userId) {
        Cursor cursor = databaseHelper.getUserData(userId);

        if (cursor != null && cursor.moveToFirst()) {
            String nickname = getColumnString(cursor, "NICKNAME");
            String gender = getColumnString(cursor, "GENDER");
            String ageRange = getColumnString(cursor, "AGE_RANGE");
            String profilePicture = getColumnString(cursor, "PROFILE_PICTURE");
            int rankId = getColumnInt(cursor, "RANK_ID");

            nicknameTextView.setText(nickname);
            genderAgeTextView.setText((gender.equals("FEMALE") ? "♀ 여, " : "♂ 남, ") + getAgeRangeDisplayText(ageRange));
            setProfileImage(profilePicture, rankId);
        } else {
            nicknameTextView.setText("Unknown");
            genderAgeTextView.setText("");
            profileImageView.setImageResource(R.drawable.grade_babe); // 기본 이미지 리소스 사용
        }

        if (cursor != null) {
            cursor.close();
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
                .into(profileImageView);
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
}
