package com.example.myapplication.ui.recruit;

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
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.ui.map.PlaceAutocompleteAdapter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

public class texi_route extends FragmentActivity implements OnMapReadyCallback, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private static final String TAG = "texi_route";
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlaceAutocompleteAdapter adapter;
    private EditText currentLocation;
    private EditText destination;
    private AutocompleteSessionToken sessionToken;
    private Marker currentMarker;
    private boolean isStartLocation;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texi_route);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        currentLocation = findViewById(R.id.currentLocation);
        destination = findViewById(R.id.destination);
        recyclerView = findViewById(R.id.recycler_view);
        registerButton = findViewById(R.id.registerButton);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAutocompleteAdapter(this, R.layout.item_search, this);
        recyclerView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionToken = AutocompleteSessionToken.newInstance();

        currentLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    isStartLocation = true;
                    getAutocompletePredictions(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    isStartLocation = false;
                    getAutocompletePredictions(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        registerButton.setOnClickListener(v -> {
            if (currentLocation.getText().toString().isEmpty() || destination.getText().toString().isEmpty()) {
                Toast.makeText(this, "출발지와 목적지를 모두 설정하세요.", Toast.LENGTH_SHORT).show();
            } else {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("currentLocation", currentLocation.getText().toString());
                resultIntent.putExtra("destination", destination.getText().toString());
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });

        ImageView backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());
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
            if (predictionList != null && !predictionList.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setPredictionList(predictionList, isStartLocation);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }).addOnFailureListener(exception -> Log.e(TAG, "Prediction fetching unsuccessful: " + exception.getMessage()));
    }

    @Override
    public void onPlaceClick(ArrayList<AutocompletePrediction> resultList, int position, boolean isStartLocation) {
        if (position >= resultList.size()) {
            Log.e(TAG, "Invalid place selected: " + position);
            Toast.makeText(this, "Invalid place selected", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
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
                    if (isStartLocation) {
                        currentLocation.setText(place.getName());
                    } else {
                        destination.setText(place.getName());
                    }
                    recyclerView.setVisibility(View.GONE);
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                }
            } else {
                Log.e(TAG, "Place details not found.");
                Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(exception -> {
            Log.e(TAG, "Place not found: " + exception.getMessage());
            Toast.makeText(this, "Invalid place selected", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
        });
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
