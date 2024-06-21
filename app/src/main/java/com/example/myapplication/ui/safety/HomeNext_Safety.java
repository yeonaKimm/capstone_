package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class HomeNext_Safety extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng startLatLng;
    private LatLng endLatLng;
    private TextView startLocationTextView;
    private TextView endLocationTextView;
    private TextView remainingTimeTextView;
    private Spinner spinnerTimePlus;
    private TextView emergencyContactTextView;
    private Button share2Button;

    private long remainingTimeInMillis;
    private CountDownTimer countDownTimer;

    // 예상 소요 시간과 비상 연락처의 목록
    private final String[] timeplusOptions = {"--분", "10분", "20분", "30분", "40분", "50분"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safety_homenext);

        startLocationTextView = findViewById(R.id.startLocation);
        endLocationTextView = findViewById(R.id.endLocation);
        remainingTimeTextView = findViewById(R.id.remainingTime);
        spinnerTimePlus = findViewById(R.id.spinner_timeplus);
        emergencyContactTextView = findViewById(R.id.emergencyContact);
        share2Button = findViewById(R.id.share2);

        // Intent에서 좌표값과 기타 데이터 가져오기
        if (getIntent() != null) {
            startLatLng = new LatLng(getIntent().getDoubleExtra("startLat", 0), getIntent().getDoubleExtra("startLng", 0));
            endLatLng = new LatLng(getIntent().getDoubleExtra("endLat", 0), getIntent().getDoubleExtra("endLng", 0));
            startLocationTextView.setText(formatText(getIntent().getStringExtra("currentLocation")));
            endLocationTextView.setText(formatText(getIntent().getStringExtra("destination")));

            // 소요 시간과 비상 연락처 값 설정
            String remainingTime = getIntent().getStringExtra("selectedTime");
            if (remainingTime != null) {
                remainingTimeTextView.setText(remainingTime);
                remainingTimeInMillis = Integer.parseInt(remainingTime.replace("분", "")) * 60 * 1000;
                startCountDownTimer(remainingTimeInMillis);
            }

            String emergencyContact = getIntent().getStringExtra("selectedContact");
            if (emergencyContact != null) {
                emergencyContactTextView.setText(emergencyContact);
            }
        }

        // Spinner와 Adapter 설정
        ArrayAdapter<CharSequence> timeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, timeplusOptions) {
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
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTimePlus.setAdapter(timeAdapter);

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        share2Button.setOnClickListener(v -> {
            String selectedTime = (String) spinnerTimePlus.getSelectedItem();
            if (!"--분".equals(selectedTime)) {
                int extendMinutes = Integer.parseInt(selectedTime.replace("분", ""));
                remainingTimeInMillis += extendMinutes * 60 * 1000;
                startCountDownTimer(remainingTimeInMillis);
            }
        });
    }

    private void startCountDownTimer(long durationInMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(durationInMillis, 60000) { // 1분 단위로 업데이트
            @Override
            public void onTick(long millisUntilFinished) {
                remainingTimeInMillis = millisUntilFinished;
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                remainingTimeTextView.setText(String.format("%d분", minutes));
            }

            @Override
            public void onFinish() {
                remainingTimeTextView.setText("0분");
            }
        }.start();
    }

    private String formatText(String text) {
        if (text != null && text.length() > 5) {
            return text.substring(0, 5) + "\n" + text.substring(5);
        }
        return text;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (startLatLng != null && endLatLng != null) {
            mMap.addMarker(new MarkerOptions().position(startLatLng).title("출발지"));
            mMap.addMarker(new MarkerOptions().position(endLatLng).title("도착지"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15));
            mMap.addPolyline(new PolylineOptions().clickable(true).add(startLatLng, endLatLng));
        }
    }
}
