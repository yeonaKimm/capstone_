package com.example.myapplication.ui.safety;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

    // 예상 소요 시간과 비상 연락처의 목록
    private final String[] timeplusOptions = {"--분", "10분", "20분", "30분", "40분", "50분"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safety_homenext);

        startLocationTextView = findViewById(R.id.startLocation);
        endLocationTextView = findViewById(R.id.endLocation);

        // Intent에서 좌표값 가져오기
        if (getIntent() != null) {
            startLatLng = new LatLng(getIntent().getDoubleExtra("startLat", 0), getIntent().getDoubleExtra("startLng", 0));
            endLatLng = new LatLng(getIntent().getDoubleExtra("endLat", 0), getIntent().getDoubleExtra("endLng", 0));
            startLocationTextView.setText(formatText(getIntent().getStringExtra("currentLocation")));
            endLocationTextView.setText(formatText(getIntent().getStringExtra("destination")));
        }

        // Spinner와 Adapter 설정
        Spinner spinnerTime = findViewById(R.id.spinner_timeplus);
        ArrayAdapter<CharSequence> timeAdapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_spinner_item, timeplusOptions) {
            @Override
            public boolean isEnabled(int position) {
                // 첫 번째 항목 선택을 불가능하게 설정
                return position != 0;
            }

            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                // 첫 번째 항목 선택을 불가능하게 설정한 경우, 텍스트 색상을 회색으로 변경
                if (position == 0) {
                    textView.setTextColor(getResources().getColor(android.R.color.darker_gray));
                } else {
                    textView.setTextColor(getResources().getColor(android.R.color.black));
                }
                return view;
            }
        };
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTime.setAdapter(timeAdapter);

        // 지도 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private String formatText(String text) {
        if (text.length() > 5) {
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
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, 15)); // 더 확대된 줌 레벨 설정
            mMap.addPolyline(new PolylineOptions().clickable(true).add(startLatLng, endLatLng));
        }
    }
}
