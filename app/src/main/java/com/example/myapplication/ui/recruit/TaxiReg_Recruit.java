package com.example.myapplication.ui.recruit;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.myapplication.R;
import com.example.myapplication.databinding.RecruitTaxiregBinding;
import com.example.myapplication.ui.recruit.texi_route;

import java.util.Calendar;

public class TaxiReg_Recruit extends Fragment {

    private RecruitTaxiregBinding binding;
    private static final int REQUEST_CODE_START = 1;
    private static final int REQUEST_CODE_END = 2;
    private String[] peopleOptions = {"n", "1", "2", "3", "4"};
    private Calendar calendar;

    public static TaxiReg_Recruit newInstance() {
        return new TaxiReg_Recruit();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = RecruitTaxiregBinding.inflate(inflater, container, false);

        Spinner spinnerPeople = binding.getRoot().findViewById(R.id.spinner_people);
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
        spinnerPeople.setAdapter(peopleAdapter);

        binding.date.setOnClickListener(v -> showDatePickerDialog());
        binding.time.setOnClickListener(v -> showTimePickerDialog());

        binding.currentLocation.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), texi_route.class);
            startActivityForResult(intent, REQUEST_CODE_START);
        });

        binding.destination.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), texi_route.class);
            startActivityForResult(intent, REQUEST_CODE_END);
        });

        binding.registerButton.setOnClickListener(v -> {
            String date = binding.date.getText().toString();
            String time = binding.time.getText().toString();
            int people = Integer.parseInt(peopleOptions[spinnerPeople.getSelectedItemPosition()]);

            TaxiRecruitDBHelper dbHelper = new TaxiRecruitDBHelper(getContext());
            dbHelper.insertTaxi(date, time, people);

            Navigation.findNavController(v).navigate(R.id.action_navigation_recruit_taxireg_to_navigation_recruit_taxilist);
        });

        return binding.getRoot();
    }

    private void showDatePickerDialog() {
        calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(requireContext(),
                (view, year1, month1, dayOfMonth) -> binding.date.setText(String.format("%04d-%02d-%02d", year1, month1 + 1, dayOfMonth)), year, month, day);
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(requireContext(),
                (view, hourOfDay, minute1) -> binding.time.setText(String.format("%02d:%02d", hourOfDay, minute1)), hour, minute, true);
        timePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK && data != null) {
            String selectedLocation = data.getStringExtra("selectedLocation");
            if (requestCode == REQUEST_CODE_START) {
                binding.currentLocation.setText(selectedLocation);
            } else if (requestCode == REQUEST_CODE_END) {
                binding.destination.setText(selectedLocation);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
