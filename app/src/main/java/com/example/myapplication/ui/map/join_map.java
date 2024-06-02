package com.example.myapplication.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;
import com.example.myapplication.MainActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class join_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper databaseHelper;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LatLng currentLocation;
    private int selectedRadius = 0;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_map);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseHelper = new DatabaseHelper(this);

        confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setEnabled(false); // 초기에는 비활성화

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        findViewById(R.id.button1km).setOnClickListener(view -> {
            selectedRadius = 1000;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });
        findViewById(R.id.button5km).setOnClickListener(view -> {
            selectedRadius = 5000;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });
        findViewById(R.id.button8km).setOnClickListener(view -> {
            selectedRadius = 8000;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });

        confirmButton.setOnClickListener(view -> {
            if (currentLocation != null && selectedRadius > 0) {
                saveRadiusAndCompleteRegistration(selectedRadius);
            } else {
                Log.e("join_map", "현재 위치나 반경이 설정되지 않았습니다.");
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            try {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, location -> {
                            if (location != null) {
                                currentLocation = new LatLng(location.getLatitude(), location.getLongitude());
                                mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15));
                                saveUserLocation(location.getLatitude(), location.getLongitude());
                            }
                        });
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
    }

    private void saveUserLocation(double latitude, double longitude) {
        String userId = getIntent().getStringExtra("USER_ID");
        databaseHelper.updateUserLocation(userId, latitude, longitude);
    }

    private void drawCircle(int radius) {
        if (currentLocation != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(currentLocation).title("Current Location"));
            mMap.addCircle(new CircleOptions()
                    .center(currentLocation)
                    .radius(radius)
                    .strokeWidth(0f)
                    .fillColor(0x550000FF));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, getZoomLevel(radius)));
        } else {
            Log.e("join_map", "현재 위치를 가져오지 못했습니다.");
        }
    }

    private int getZoomLevel(int radius) {
        int zoomLevel = 15;
        if (radius > 5000) {
            zoomLevel = 12;
        } else if (radius > 2000) {
            zoomLevel = 13;
        } else if (radius > 1000) {
            zoomLevel = 14;
        }
        return zoomLevel;
    }

    private void saveRadiusAndCompleteRegistration(int radius) {
        String userId = getIntent().getStringExtra("USER_ID");
        // 반경과 가장 먼 거리값을 저장하는 로직
        double maxDistance = radius;
        boolean isUpdated = databaseHelper.updateUserRadius(userId, radius, maxDistance);
        if (isUpdated) {
            Log.d("join_map", "반경 정보가 저장되었습니다.");
            // 회원가입 완료 후 MainActivity로 이동
            Intent intent = new Intent(join_map.this, MainActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            Log.e("join_map", "반경 정보를 저장하지 못했습니다.");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                    getCurrentLocation();
                }
            }
        }
    }
}
