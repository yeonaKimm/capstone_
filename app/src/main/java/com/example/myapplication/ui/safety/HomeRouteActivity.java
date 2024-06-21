package com.example.myapplication.ui.safety;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.myapplication.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
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

public class HomeRouteActivity extends AppCompatActivity implements OnMapReadyCallback, PlaceAutocompleteAdapterForHomeRoute.PlaceAutoCompleteInterface {

    private GoogleMap mMap;
    private PlacesClient placesClient;
    private RecyclerView recyclerView;
    private PlaceAutocompleteAdapterForHomeRoute adapter;
    private EditText currentLocationEditText;
    private EditText destinationEditText;
    private AutocompleteSessionToken sessionToken;
    private LatLng currentLocationLatLng;
    private LatLng destinationLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_route);

        Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this);

        recyclerView = findViewById(R.id.recycler_view);
        currentLocationEditText = findViewById(R.id.currentLocation);
        destinationEditText = findViewById(R.id.destination);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PlaceAutocompleteAdapterForHomeRoute(this, this);
        recyclerView.setAdapter(adapter);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        sessionToken = AutocompleteSessionToken.newInstance();

        // 소요 시간과 비상 연락처 값을 인텐트에서 가져오기
        String selectedTime = getIntent().getStringExtra("selectedTime");
        String selectedContact = getIntent().getStringExtra("selectedContact");

        currentLocationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getAutocompletePredictions(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        destinationEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {
                    getAutocompletePredictions(s.toString());
                } else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        findViewById(R.id.register).setOnClickListener(v -> {
            String currentLocation = currentLocationEditText.getText().toString();
            String destination = destinationEditText.getText().toString();
            if (currentLocation.isEmpty() || destination.isEmpty()) {
                Toast.makeText(this, "출발지와 목적지를 모두 선택해주세요", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(HomeRouteActivity.this, HomeNext_Safety.class);
                intent.putExtra("currentLocation", currentLocation);
                intent.putExtra("destination", destination);
                intent.putExtra("selectedTime", selectedTime); // 소요 시간 추가
                intent.putExtra("selectedContact", selectedContact); // 비상 연락처 추가
                if (currentLocationLatLng != null) {
                    intent.putExtra("startLat", currentLocationLatLng.latitude);
                    intent.putExtra("startLng", currentLocationLatLng.longitude);
                }
                if (destinationLatLng != null) {
                    intent.putExtra("endLat", destinationLatLng.latitude);
                    intent.putExtra("endLng", destinationLatLng.longitude);
                }
                startActivity(intent);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng defaultLocation = new LatLng(34.7604, 127.6622); // 여수시의 위도와 경도
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 12));
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
        }).addOnFailureListener(exception -> Log.e("HomeRouteActivity", "Prediction fetching unsuccessful: " + exception.getMessage()));
    }

    @Override
    public void onPlaceClick(List<AutocompletePrediction> resultList, int position) {
        if (position >= resultList.size()) {
            Toast.makeText(this, "Invalid place selected", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
            return;
        }

        AutocompletePrediction item = resultList.get(position);
        String placeId = item.getPlaceId();

        FetchPlaceRequest request = FetchPlaceRequest.builder(placeId, Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME)).build();
        placesClient.fetchPlace(request).addOnSuccessListener(response -> {
            Place place = response.getPlace();
            LatLng latLng = place.getLatLng();
            if (latLng != null) {
                if (currentLocationEditText.hasFocus()) {
                    currentLocationEditText.setText(place.getName());
                    currentLocationLatLng = latLng;
                } else if (destinationEditText.hasFocus()) {
                    destinationEditText.setText(place.getName());
                    destinationLatLng = latLng;
                }
                recyclerView.setVisibility(View.GONE);

                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            }
        }).addOnFailureListener(exception -> {
            Toast.makeText(this, "Place not found", Toast.LENGTH_SHORT).show();
            recyclerView.setVisibility(View.GONE);
        });
    }
}
