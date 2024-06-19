package com.example.myapplication.ui.recruit;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import com.example.myapplication.R;
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

import java.util.Arrays;
import java.util.List;

public class texi_route extends FragmentActivity implements OnMapReadyCallback, PlaceAutocompleteAdapterForTexiRoute.PlaceAutoCompleteInterface {

    private static final String TAG = "texi_route";
    private GoogleMap mMap;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlaceAutocompleteAdapterForTexiRoute adapter;
    private EditText currentLocationEditText;
    private EditText destinationEditText;
    private Button registerButton;
    private AutocompleteSessionToken sessionToken;
    private Marker currentMarker;
    private LatLng selectedLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.texi_route);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        recyclerView = findViewById(R.id.recycler_view);
        registerButton = findViewById(R.id.register);
        currentLocationEditText = findViewById(R.id.currentLocation);
        destinationEditText = findViewById(R.id.destination);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new PlaceAutocompleteAdapterForTexiRoute(this, this);
        recyclerView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionToken = AutocompleteSessionToken.newInstance();

        currentLocationEditText.addTextChangedListener(new TextWatcher() {
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        destinationEditText.addTextChangedListener(new TextWatcher() {
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
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registerButton.setOnClickListener(v -> {
            String currentLocation = currentLocationEditText.getText().toString();
            String destination = destinationEditText.getText().toString();
            if (currentLocation.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "출발지와 목적지를 모두 선택해주세요", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("currentLocation", currentLocation);
                intent.putExtra("destination", destination);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // 여수시의 위도와 경도
        LatLng yeosu = new LatLng(34.7604, 127.6622);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(yeosu, 12)); // 줌 레벨 조정 가능

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
                adapter.setPredictionList(predictionList);
            } else {
                recyclerView.setVisibility(View.GONE);
            }
        }).addOnFailureListener(exception -> Log.e(TAG, "Prediction fetching unsuccessful: " + exception.getMessage()));
    }

    @Override
    public void onPlaceClick(List<AutocompletePrediction> resultList, int position) {
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
                    if (currentLocationEditText.hasFocus()) {
                        currentLocationEditText.setText(place.getName());
                    } else if (destinationEditText.hasFocus()) {
                        destinationEditText.setText(place.getName());
                    }
                    recyclerView.setVisibility(View.GONE);

                    if (currentMarker != null) {
                        currentMarker.remove();
                    }

                    currentMarker = mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                    selectedLocation = latLng;
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
