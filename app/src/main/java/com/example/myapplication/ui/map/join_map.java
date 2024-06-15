package com.example.myapplication.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class join_map extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationClient;
    private DatabaseHelper databaseHelper;
    private Geocoder geocoder;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LatLng selectedLocation;
    private int selectedRadius = 0;
    private Button confirmButton;
    private Button button400m, button700m, button1km;
    private EditText searchAddress;
    private Button searchButton;
    private static final String TAG = "join_map";

    private final ActivityResultLauncher<Intent> startAutocomplete = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent intent = result.getData();
                    if (intent != null) {
                        Place place = Autocomplete.getPlaceFromIntent(intent);
                        Log.i(TAG, "Place: " + place.getName() + ", " + place.getId());
                        selectedLocation = place.getLatLng();
                        if (selectedLocation != null) {
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(selectedLocation).title(place.getName()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(selectedLocation, 15));
                        }
                    }
                } else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR) {
                    Log.i(TAG, "An error occurred");
                } else if (result.getResultCode() == RESULT_CANCELED) {
                    Log.i(TAG, "User canceled autocomplete");
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_map);

        // Initialize the Places API with an API key
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }
        PlacesClient placesClient = Places.createClient(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        databaseHelper = new DatabaseHelper(this);
        geocoder = new Geocoder(this);

        confirmButton = findViewById(R.id.confirmButton);
        button400m = findViewById(R.id.button400m);
        button700m = findViewById(R.id.button700m);
        button1km = findViewById(R.id.button1km);
        searchAddress = findViewById(R.id.searchAddress);
        searchButton = findViewById(R.id.searchButton);

        confirmButton.setEnabled(false); // 초기에는 비활성화

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

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
                Log.e(TAG, "현재 위치나 반경이 설정되지 않았습니다.");
            }
        });

        searchButton.setOnClickListener(view -> openAutocompleteActivity());
    }

    private void openAutocompleteActivity() {
        List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG);
        Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY, fields)
                .build(this);
        startAutocomplete.launch(intent);
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
            Log.e(TAG, "현재 위치를 가져오지 못했습니다.");
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

    private void saveUserLocation(double latitude, double longitude) {
        String userId = getIntent().getStringExtra("USER_ID");
        databaseHelper.updateUserLocation(userId, latitude, longitude);
    }

    private void saveRadiusAndCompleteRegistration(int radius) {
        String userId = getIntent().getStringExtra("USER_ID");
        double maxDistance = radius;
        Log.d(TAG, "Saving radius for user: " + userId + ", radius: " + radius + ", maxDistance: " + maxDistance);
        boolean isUpdated = databaseHelper.updateUserRadius(userId, radius, maxDistance);
        if (isUpdated) {
            Log.d(TAG, "반경 정보가 저장되었습니다.");
            // 회원가입 완료 후 MainActivity로 이동
            Intent intent = new Intent(join_map.this, MainActivity.class);
            intent.putExtra("USER_ID", userId);
            startActivity(intent);
            finish();
        } else {
            Log.e(TAG, "반경 정보를 저장하지 못했습니다.");
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
