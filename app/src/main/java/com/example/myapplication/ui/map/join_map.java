package com.example.myapplication.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.ui.Login.DatabaseHelper;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class join_map extends FragmentActivity implements OnMapReadyCallback, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper databaseHelper;
    private Geocoder geocoder;
    private PlacesClient placesClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LatLng selectedLocation;
    private int selectedRadius = 0;
    private Button confirmButton;
    private Button button400m, button700m, button1km;
    private EditText searchAddress;
    private RecyclerView rvAutocompleteKeyword;
    private PlaceAutocompleteAdapter placeAutocompleteAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_map);

        // Initialize Places
        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseHelper = new DatabaseHelper(this);
        geocoder = new Geocoder(this);

        confirmButton = findViewById(R.id.confirmButton);
        button400m = findViewById(R.id.button400m);
        button700m = findViewById(R.id.button700m);
        button1km = findViewById(R.id.button1km);
        searchAddress = findViewById(R.id.searchAddress);
        rvAutocompleteKeyword = findViewById(R.id.list_search);

        confirmButton.setEnabled(false); // 초기에는 비활성화

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // Setup RecyclerView and adapter for autocomplete suggestions
        rvAutocompleteKeyword.setHasFixedSize(true);
        rvAutocompleteKeyword.setLayoutManager(new LinearLayoutManager(this));
        placeAutocompleteAdapter = new PlaceAutocompleteAdapter(this, R.layout.item_search, this);
        rvAutocompleteKeyword.setAdapter(placeAutocompleteAdapter);

        searchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().isEmpty()) {
                    findAutocompletePredictions(s.toString());
                } else {
                    placeAutocompleteAdapter.clearList();
                    rvAutocompleteKeyword.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        button400m.setOnClickListener(view -> {
            selectedRadius = 400;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });
        button700m.setOnClickListener(view -> {
            selectedRadius = 700;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });
        button1km.setOnClickListener(view -> {
            selectedRadius = 1000;
            drawCircle(selectedRadius);
            confirmButton.setEnabled(true); // 반경이 선택되면 활성화
        });

        confirmButton.setOnClickListener(view -> {
            if (selectedLocation != null && selectedRadius > 0) {
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
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void findAutocompletePredictions(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    placeAutocompleteAdapter.setResults(predictions);
                    rvAutocompleteKeyword.setVisibility(View.VISIBLE);
                })
                .addOnFailureListener(exception -> {
                    Log.e("join_map", "Place not found: " + exception.getMessage());
                });
    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> resultList, int position) {
        PlaceAutocompleteAdapter.PlaceAutocomplete place = resultList.get(position);
        String placeId = place.placeId.toString();
        // Fetch additional details about the selected place if necessary
        Log.d("join_map", "Place selected: " + place.description);
    }

    private void drawCircle(int radius) {
        if (selectedLocation != null) {
            mMap.clear();
            mMap.addMarker(new MarkerOptions().position(selectedLocation).title("Selected Location"));
            mMap.addCircle(new CircleOptions()
                    .center(selectedLocation)
                    .radius(radius)
                    .strokeWidth(0f)
                    .fillColor(0x550000FF));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, getZoomLevel(radius)));
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
        double maxDistance = radius;
        Log.d("join_map", "Saving radius for user: " + userId + ", radius: " + radius + ", maxDistance: " + maxDistance);
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
            Toast.makeText(this, "반경 정보를 저장하지 못했습니다.", Toast.LENGTH_SHORT).show();
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
                }
            }
        }
    }
}
