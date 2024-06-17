package com.example.myapplication.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class join_map extends FragmentActivity implements OnMapReadyCallback, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private static final String TAG = "join_map";
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlaceAutocompleteAdapter adapter;
    private EditText searchAddress;
    private ImageButton clearButton;
    private AutocompleteSessionToken sessionToken;
    private Marker currentMarker;
    private Circle currentCircle;
    private boolean radiusSet = false;
    private Button confirmButton;
    private LatLng selectedLocation;
    private int selectedRadius;
    private DatabaseHelper databaseHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_map);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        searchAddress = findViewById(R.id.searchAddress);
        recyclerView = findViewById(R.id.recycler_view);
        clearButton = findViewById(R.id.clearButton);
        confirmButton = findViewById(R.id.confirmButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlaceAutocompleteAdapter(this, this);
        recyclerView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionToken = AutocompleteSessionToken.newInstance();

        searchAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getAutocompletePredictions(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
                clearButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        clearButton.setOnClickListener(v -> {
            searchAddress.setText("");
            recyclerView.setVisibility(View.GONE);
            clearButton.setVisibility(View.GONE);
            if (currentMarker != null) {
                currentMarker.remove();
                currentMarker = null;
            }
            if (currentCircle != null) {
                currentCircle.remove();
                currentCircle = null;
            }
            radiusSet = false;
            confirmButton.setEnabled(false);
        });

        findViewById(R.id.button400m).setOnClickListener(v -> setRadius(400));
        findViewById(R.id.button700m).setOnClickListener(v -> setRadius(700));
        findViewById(R.id.button1km).setOnClickListener(v -> setRadius(1000));

        confirmButton.setOnClickListener(v -> {
            if (radiusSet) {
                double maxDistance = selectedRadius;
                if (databaseHelper.updateUserLocationAndRadius(userId, selectedLocation.latitude, selectedLocation.longitude, selectedRadius, maxDistance)) {
                    Toast.makeText(this, "반경이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(join_map.this, MainActivity.class);
                    intent.putExtra("USER_ID", userId);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "반경 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Please select a radius first.", Toast.LENGTH_SHORT).show();
            }
        });

        databaseHelper = new DatabaseHelper(this);
        userId = getIntent().getStringExtra("USER_ID");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    private void getAutocompletePredictions(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(sessionToken)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            List<AutocompletePrediction> predictionList = response.getAutocompletePredictions();
            if (predictionList != null && predictionList.size() > 0) {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setPredictionList(predictionList, false); // Updated method call
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }).addOnFailureListener(exception -> Log.e(TAG, "Prediction fetching unsuccessful: " + exception.getMessage()));
    }

    @Override
    public void onPlaceClick(ArrayList<AutocompletePrediction> resultList, int position, boolean isCurrentLocation) {
        if (position >= resultList.size()) {
            Log.e(TAG, "Invalid place selected: " + position);
            Toast.makeText(this, "Invalid place selected", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE); // Hide autocomplete list
            return;
        }

        AutocompletePrediction item = resultList.get(position);
        String placeId = item.getPlaceId();

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME)).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place = response.getPlace();
            if (place != null) {
                LatLng latLng = place.getLatLng();
                if (latLng != null) {
                    searchAddress.setText(place.getName());
                    recyclerView.setVisibility(View.GONE);

                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    if (currentCircle != null) {
                        currentCircle.remove();
                    }

                    currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    selectedLocation = latLng;
                    radiusSet = false;
                    confirmButton.setEnabled(false);
                }
            } else {
                Log.e(TAG, "Place details not found.");
                Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
            Toast.makeText(this, "Invalid place selected", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE); // Hide autocomplete list
        });
    }

    private void setRadius(int radius) {
        if (currentMarker != null && currentMarker.getPosition() != null) {
            LatLng latLng = currentMarker.getPosition();
            if (currentCircle != null) {
                currentCircle.remove();
            }
            currentCircle = mMap.addCircle(new CircleOptions()
                    .center(latLng)
                    .radius(radius)
                    .strokeColor(Color.RED)
                    .fillColor(Color.argb(50, 255, 0, 0)));
            selectedRadius = radius;
            radiusSet = true;
            confirmButton.setEnabled(true);
        } else {
            Toast.makeText(this, "Please select a place first.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
            }
        }
    }
}
