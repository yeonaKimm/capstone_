package com.example.myapplication.ui.recruit;

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
import androidx.appcompat.app.AppCompatActivity;
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

public class texi_route extends AppCompatActivity implements OnMapReadyCallback, PlaceAutocompleteAdapter.PlaceAutoCompleteInterface {

    private static final String TAG = "texi_route";
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlaceAutocompleteAdapter adapter;
    private EditText currentLocation;
    private EditText destination;
    private AutocompleteSessionToken sessionToken;
    private Marker currentMarker;
    private Marker destinationMarker;
    private Button registerButton;
    private boolean isCurrentLocationSelected = false;
    private boolean isDestinationSelected = false;
    private LatLng selectedStartLocation;
    private LatLng selectedEndLocation;

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
        adapter = new PlaceAutocompleteAdapter(this, this);
        recyclerView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionToken = AutocompleteSessionToken.newInstance();

        currentLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getAutocompletePredictions(s.toString(), true);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        destination.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getAutocompletePredictions(s.toString(), false);
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });

        findViewById(R.id.backButton).setOnClickListener(v -> finish());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void getAutocompletePredictions(String query, boolean isCurrentLocation) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .setSessionToken(sessionToken)
                .build();

        placesClient.findAutocompletePredictions(request).addOnSuccessListener(response -> {
            List<AutocompletePrediction> predictionList = response.getAutocompletePredictions();
            if (predictionList != null && !predictionList.isEmpty()) {
                recyclerView.setVisibility(View.VISIBLE);
                adapter.setPredictionList(predictionList, isCurrentLocation);
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
                    if (isCurrentLocation) {
                        currentLocation.setText(place.getName());
                        if (currentMarker != null) {
                            currentMarker.remove();
                        }
                        currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("출발지"));
                        selectedStartLocation = latLng;
                        isCurrentLocationSelected = true;
                    } else {
                        destination.setText(place.getName());
                        if (destinationMarker != null) {
                            destinationMarker.remove();
                        }
                        destinationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("목적지"));
                        selectedEndLocation = latLng;
                        isDestinationSelected = true;
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    recyclerView.setVisibility(View.GONE);
                    checkRegisterButtonState();
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

    private void checkRegisterButtonState() {
        registerButton.setEnabled(isCurrentLocationSelected && isDestinationSelected);
    }
}
