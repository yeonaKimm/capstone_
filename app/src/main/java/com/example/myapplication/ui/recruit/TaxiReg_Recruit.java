package com.example.myapplication.ui.recruit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.myapplication.R;

import java.util.Calendar;

public class TaxiReg_Recruit extends Fragment {

    private static final int REQUEST_CODE_START = 1;
    private static final int REQUEST_CODE_END = 2;
    private String[] peopleOptions = {"n", "1", "2", "3", "4"};
    private Calendar calendar;
    private EditText startEditText;
    private EditText endEditText;
    private TextView dateTextView;
    private TextView timeTextView;
    private Spinner peopleSpinner;
    private Button registerButton;

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

        startEditText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), texi_route.class);
            startActivityForResult(intent, REQUEST_CODE_START);
        });

        endEditText.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), texi_route.class);
            startActivityForResult(intent, REQUEST_CODE_END);
        });

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
            dbHelper.insertTaxi(date, time, people);

            NavController navController = Navigation.findNavController(v);
            navController.navigate(R.id.action_navigation_recruit_taxireg_to_navigation_recruit_taxilist);
        });

        return view;
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_START) {
                String startLocation = data.getStringExtra("currentLocation");
                startEditText.setText(startLocation);
            } else if (requestCode == REQUEST_CODE_END) {
                String endLocation = data.getStringExtra("destination");
                endEditText.setText(endLocation);
            }
        }
    }
}
