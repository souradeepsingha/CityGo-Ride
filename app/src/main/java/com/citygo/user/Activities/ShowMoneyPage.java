package com.citygo.user.Activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.citygo.user.R;
import com.citygo.user.databinding.ActivityMapsBinding;
import com.citygo.user.model.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class ShowMoneyPage extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private Button searchbtn;
    private TextView location1TextView;
    private TextView destination1TextView;
    String fromWhere;
    String toWhere;
    private String uid;
    private String email;
    private String fcmToken;
    private String name;
    private String number;
    private String profileImage,userLatLang;
    private String driverId;
    private FirebaseAuth mAuth;
    private GoogleMap mMap;


    private Marker fromMarker;
    private Marker toMarker;
    private Polyline routePolyline;

    private LinearLayout linearLayoutcoupan;
    EditText editcoupon;
    Button buttoncoupon;
    TextView Rupevalue;
    TextView textprice;


    ImageView imageView2, imageView3;
    TextView time;
    LinearLayout linearcoupon, linearLayout2;
    ImageView imageinfo;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_money_page);
        imageView2 = findViewById(R.id.arrowright);
        imageView3 = findViewById(R.id.arrowdown);
        linearcoupon = findViewById(R.id.linearcoupon);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout2.setVisibility(View.GONE);

        buttoncoupon = findViewById(R.id.buttoncoupon);
        editcoupon = findViewById(R.id.edittextcoupon);
        time = findViewById(R.id.time);
        imageinfo = findViewById(R.id.imageinfo);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // Find the SupportMapFragment and obtain a reference to the GoogleMap
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);


        textprice = findViewById(R.id.textprice);
        mapFragment.getMapAsync(this);
        // Retrieve data from the intent
        Intent intent = getIntent();
        if (intent != null) {
            uid = mAuth.getCurrentUser().getUid();
            email = intent.getStringExtra("email");
            fcmToken = intent.getStringExtra("fcmToken");
            name = intent.getStringExtra("name");
            number = intent.getStringExtra("number");
            profileImage = intent.getStringExtra("profileImage");
            driverId = intent.getStringExtra("driverId");
            fromWhere = intent.getStringExtra("fromWhere");
            toWhere = intent.getStringExtra("toWhere");
            userLatLang = intent.getStringExtra("latLang");
        }


        searchbtn = findViewById(R.id.button1);
        location1TextView = findViewById(R.id.location1);
        destination1TextView = findViewById(R.id.destination1);
        // Set these values in your TextViews
        location1TextView.setText(fromWhere);
        destination1TextView.setText(toWhere);
        searchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });


        linearLayoutcoupan = findViewById(R.id.linearcoupon);
        linearLayoutcoupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLinearLayoutVisibility();
            }
        });


        buttoncoupon.setOnClickListener(new View.OnClickListener() {
            private boolean hasCalculated = false; // Add a boolean flag

            @Override
            public void onClick(View view) {
                if (!hasCalculated && editcoupon.getText().toString().equals("citygo20")) {
                    // Get the text content of the Rupevalue TextView
                    String rupeeText = textprice.getText().toString();
                    Log.d("hello124", rupeeText);
                    // Convert the rupee text to an integer
                    int rupeeValue1 = Integer.parseInt(rupeeText);

                    // Calculate 20% of the rupee value
                    int twentyPercent = (int) (rupeeValue1 * 0.2);

                    // Calculate 80% of the rupee value
                    int eightyPercent = rupeeValue1 - twentyPercent;

                    // Set the remaining 80% value in the TextView
                    textprice.setText(String.valueOf(eightyPercent));

                    hasCalculated = true; // Set the flag to true after calculation
                }
            }
        });

        imageinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ShowMoneyPage.this)
                        .setTitle("Price Distribution")
                        .setMessage("1 KM=1km x 10 Rupee \n 1Km=10 Rupee")
                        .setCancelable(false)
                        .setNegativeButton("Okey", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })


                        .show();
            }
        });


    }

    private void toggleLinearLayoutVisibility() {
        if (linearLayout2.getVisibility() == View.VISIBLE) {
            linearLayout2.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);
        } else {
            linearLayout2.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.GONE);
        }
    }

    private void checkLocationPermission() {
        if (isLocationPermissionGranted()) {
            validateDetails();
        } else {
            requestLocationPermission();
        }
    }

    private boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    private void validateDetails() {
        if (destination1TextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "The Destination Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (location1TextView.getText().toString().isEmpty()) {
            Toast.makeText(this, "The Location Field is Empty", Toast.LENGTH_SHORT).show();
        } else {
            startSearching();
            Toast.makeText(this, "Please wait, searching for drivers", Toast.LENGTH_SHORT).show();
        }
    }

    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }



    private void startSearching() {

        Intent intent = new Intent(ShowMoneyPage.this, waitingActivity.class);
        intent.putExtra("latLang", userLatLang);
        intent.putExtra("fromWhere", location1TextView.getText().toString());
        intent.putExtra("toWhere", destination1TextView.getText().toString());
        intent.putExtra("email", email);
        intent.putExtra("fcmToken", fcmToken);
        intent.putExtra("name", name);
        intent.putExtra("number", number);
        intent.putExtra("profileImage", profileImage);
        intent.putExtra("driverId", driverId);
        intent.putExtra("Uid", uid);
        startActivity(intent);
        finish();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

        // Geocode the fromWhere and toWhere locations to get their LatLng coordinates
        LatLng fromLatLng = geocodeLocation(fromWhere);
        LatLng toLatLng = geocodeLocation(toWhere);

        if (fromLatLng != null && toLatLng != null) {
            // Add markers for fromWhere and toWhere
            fromMarker = mMap.addMarker(new MarkerOptions().position(fromLatLng).title("From"));
            toMarker = mMap.addMarker(new MarkerOptions().position(toLatLng).title("To"));
            getData(fromLatLng, toLatLng);
            Log.d("onMapRead43", String.valueOf(toLatLng));
            // Add an OnGlobalLayoutListener to wait until the map layout is ready
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    // Calculate the bounds that include both markers
                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    builder.include(fromMarker.getPosition());
                    builder.include(toMarker.getPosition());
                    LatLngBounds bounds = builder.build();

                    // Set padding for the camera animation
                    int padding = 100; // Adjust this value as needed

                    // Animate the camera to fit both markers within the specified padding
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                    mMap.animateCamera(cameraUpdate);

                    // Draw a polyline between fromWhere and toWhere
                    PolylineOptions polylineOptions = new PolylineOptions()
                            .add(fromLatLng, toLatLng)
                            .color(ContextCompat.getColor(getApplicationContext(), R.color.blue));
                    routePolyline = mMap.addPolyline(polylineOptions);
                }
            });
        }
    }


    // Geocode a location name to get its LatLng coordinates
    private LatLng geocodeLocation(String locationName) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void getData(LatLng fromLatLng, LatLng toLatLng) {
        Call<GetDistance> call = DistanceApi.getDistanceService()
                .getDistance(fromLatLng.latitude + "," + fromLatLng.longitude,
                        toLatLng.latitude + "," + toLatLng.longitude,
                        "driving",
                        "metric",
                        "AIzaSyAcrhT6GDVqfHQLKFmoMcFBWa0W-wFUCEg"); // Replace with your API key

        call.enqueue(new Callback<GetDistance>() {
            @Override
            public void onResponse(Call<GetDistance> call, retrofit2.Response<GetDistance> response) {
                GetDistance details = response.body();
                Log.d("onMapRead212", String.valueOf(details));
                if (details != null) {
                    int rowsSize = details.getRows() != null ? details.getRows().size() : 0;
                    Log.d("RowsSize", String.valueOf(rowsSize));
                    if (rowsSize > 0) {
                        List<Element> elements = details.getRows().get(0).getElements();
                        Log.d("onMapRead272", String.valueOf(elements));
                        if (elements.size() > 0) {
                            Duration duration = elements.get(0).getDuration();
                            Distance distance = elements.get(0).getDistance();
                            if (duration != null && distance != null) {
                                String timeHr = duration.getText();
                                Integer timeSec = duration.getValue();
                                time.setText(timeHr + "");

                                String disKm = distance.getText();

// Extract the numeric part of the string (remove " km" from the end)
                                String numericValue = disKm.replace(" km", "");

                                try {
                                    // Parse the extracted numeric value as a double
                                    double disKmDouble = Double.parseDouble(numericValue);

                                    // Convert kilometers to meters and calculate the cost
                                    double disMeters = disKmDouble * 1000; // Convert kilometers to meters
                                    double cost = (disMeters / 1000) * 10; // Calculate the cost for 1000 meters

                                    // Cast the cost to an integer and set it as text
                                    int costAsInt = (int) cost;
                                    textprice.setText(costAsInt + "");
                                } catch (NumberFormatException e) {
                                    // Handle the case where the string cannot be parsed as a double
                                    e.printStackTrace();
                                    // You might want to show an error message to the user here
                                }


//                                int disM = distance.getValue();
//                                if (disM >= 1000) {
//                                    int disKmInt = Integer.parseInt(disKm);
//                                    int result = disKmInt * 10;
//                                   textdistance.setText(String.valueOf(result));
//                                } else {
//                                    if (disM < 500) {
//                                        int result2 = 10;
//                                        binding.textdistance.setText(String.valueOf(result2));
//                                    } else {
//                                        int result3 = 10;
//                                        binding.textdistance.setText(String.valueOf(result3));
//                                    }
                            }

                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<GetDistance> call, Throwable t) {
                // Handle the failure case here
            }
        });
    }


    // Example function to calculate the price (replace with your logic)
    private double calculatePrice(double distanceInKm, double durationInHours) {
        // Replace this with your pricing logic
        double pricePerKm = 5.0; // Example price per kilometer
        double pricePerHour = 10.0; // Example price per hour

        // Calculate the total price
        double totalPrice = (distanceInKm * pricePerKm) + (durationInHours * pricePerHour);

        return totalPrice;
    }

}
