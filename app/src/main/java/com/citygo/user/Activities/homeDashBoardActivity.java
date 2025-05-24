package com.citygo.user.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citygo.user.DataClass.nodeDataClass;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.Priority;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.citygo.user.Fragment.prebookingFragment;
import com.citygo.user.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class homeDashBoardActivity extends AppCompatActivity implements OnMapReadyCallback {

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    FrameLayout frameLayout, frameLayout2;

    TextView text1;
    TabLayout tabLayout;

    private Marker userMarker;

    private static final int REQUEST_CODE_AUTOCOMPLETE = 1001;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    MaterialToolbar materialToolbar;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String uid;
    String driverId;

    LocationRequest locationRequest;
    LocationCallback locationCallback;

    public static String selectedAddress;
    public static LatLng userLatLang;


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set the FLAG_LAYOUT_NO_LIMITS flag to extend the layout behind the status bar
        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        );

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        // Set the status bar color to transparent with a specific color


        setContentView(R.layout.activity_home_dash_board);



        Intent intent = getIntent();
        email = intent.getStringExtra("email");
        fcmToken = intent.getStringExtra("fcmToken");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        profileImage = intent.getStringExtra("profileImage");
        uid = intent.getStringExtra("Uid");
        driverId = intent.getStringExtra("driverId");
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        Bundle bundle = new Bundle();

        bundle.putString("email", email);
        bundle.putString("fcmToken", fcmToken);
        bundle.putString("name", name);
        bundle.putString("number", number);
        bundle.putString("profileImage", profileImage);
        bundle.putString("Uid", uid);
        bundle.putString("driverId", driverId);

        materialToolbar = findViewById(R.id.materialtoolbar);
        text1 = findViewById(R.id.actionbar_text);
        materialToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we have to remove the location updates because user is choosing it manually

                fusedLocationProviderClient.removeLocationUpdates(locationCallback);

                Intent intent = new Intent(homeDashBoardActivity.this, emptyview.class);
                startActivityForResult(intent, REQUEST_CODE_AUTOCOMPLETE);
            }
        });


        drawerLayout = findViewById(R.id.drawerlayout);
        materialToolbar = findViewById(R.id.materialtoolbar);

        navigationView = findViewById(R.id.navigationview);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(homeDashBoardActivity.this, drawerLayout, materialToolbar, R.string.close, R.string.open);
        drawerLayout.addDrawerListener(toggle);
        NavigationView navigationView = findViewById(R.id.navigationview);

// Clear existing menu items
        navigationView.getMenu().clear();

// Create a custom view for each item
        // Replace '5' with the number of items you want
            View customItemView = getLayoutInflater().inflate(R.layout.navigationlayout, navigationView, false);

            // Find the view with the ID "about" and set a click listener for it
            View aboutItemView = customItemView.findViewById(R.id.about);
            if (aboutItemView != null) {
                aboutItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, AboutPage.class);
                        startActivity(intent);
                    }
                });
            }
            View settingsItemView = customItemView.findViewById(R.id.settings);
            if (settingsItemView != null) {
                settingsItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, settings.class);
                        startActivity(intent);
                    }
                });
            }
            View safetyItemView = customItemView.findViewById(R.id.Safety);
            if (safetyItemView != null) {
                safetyItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, Safety.class);
                        startActivity(intent);
                    }
                });
            }
            View supportItemView = customItemView.findViewById(R.id.support);
            if (supportItemView != null) {
                supportItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, Support.class);
                        startActivity(intent);
                    }
                });
            }

            View notificationItemView = customItemView.findViewById(R.id.notification);
            if (notificationItemView != null) {
                notificationItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, Notification.class);
                        startActivity(intent);
                    }
                });
            }
            View historyItemView = customItemView.findViewById(R.id.userhistory);
            if (historyItemView != null) {
                historyItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, Userbooking.class);
                        startActivity(intent);
                    }
                });
            }

            View bookingItemView = customItemView.findViewById(R.id.bookings);
            if (bookingItemView != null) {
                bookingItemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        // Handle the click event for the "About" item
                        Intent intent = new Intent(homeDashBoardActivity.this, Bookings.class);
                        startActivity(intent);
                    }
                });
            }

            navigationView.addView(customItemView);


        View headerView = navigationView.getHeaderView(0);
        TextView nameTv = headerView.findViewById(R.id.textname);
        TextView numberTv = headerView.findViewById(R.id.numberTv);
        nameTv.setText(name);
        numberTv.setText(number);




        frameLayout = findViewById(R.id.framelayout);


        tabLayout = findViewById(R.id.tablayout1);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.framelayout, new firstfragment());
        fragmentTransaction.commit();
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int tabposition = tab.getPosition();
                if (tabposition == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.framelayout, new firstfragment());
                    fragmentTransaction.commit();

                } else if (tabposition == 1) {

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    Fragment view = new prebookingFragment();
                    ft.replace(R.id.framelayout, view);
                    ft.commit();
                    view.setArguments(bundle);
                    bundle.putString("email", email);
                    bundle.putString("fcmToken", fcmToken);
                    bundle.putString("name", name);
                    bundle.putString("number", number);
                    bundle.putString("profileImage", profileImage);
                    bundle.putString("Uid", uid);
                    bundle.putString("driverId", driverId);

                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        locationRequest = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, TimeUnit.SECONDS.toMillis(5))
                .setMinUpdateDistanceMeters(2).build();

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                Location location = locationResult.getLastLocation();
                if (location != null) {
                    //here you get the location
                    Double latitude = location.getLatitude();
                    Double longitude = location.getLongitude();
                    userLatLang = new LatLng(location.getLatitude(),location.getLongitude());
                    SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putString("passengerLatLang",latitude+","+longitude);
                    editor.commit();
                    setLocationtoTV(location);
                    updateMarkerPosition(location);
                }
            }
        };

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK) {
            // Get the selected address from the intent data and set it in the AutoCompleteTextView
            selectedAddress = data.getStringExtra("selected_address");
            userLatLang = data.getParcelableExtra("userLatLang");
            Location userLocation = new Location("userLocation");
            userLocation.setLatitude(userLatLang.latitude);
            userLocation.setLongitude(userLatLang.longitude);

            updateMarkerPosition(userLocation);


            SharedPreferences pref = getSharedPreferences("data", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("fromWhere", selectedAddress);
            editor.putString("passengerLatLang",userLatLang.latitude+","+userLatLang.longitude);
            editor.commit();
            text1.setText(selectedAddress);
        }
    }


    private boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }


    private void requestLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
            } else {
                // Location permission denied
                Toast.makeText(this, "You have to allow loacation permission to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        try {
            // Load the custom map style from the JSON file
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));

            if (!success) {
                Log.e("MapActivity", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapActivity", "Can't find style. Error: ", e);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            //mMap.setMyLocationEnabled(true);

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }

        FirebaseDatabase.getInstance().getReference(nodeDataClass.ROOM).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String latlang = ds.child("latLang").getValue(String.class);
                        Log.d("latlang",latlang);
                        LatLng driverLocation = new LatLng(divideLocation(latlang)[0],divideLocation(latlang)[1]);
                        BitmapDescriptor customMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.driver_upperview);
                         mMap.addMarker(new MarkerOptions().position(driverLocation).title("You")
                                .icon(customMarkerIcon));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private Double[] divideLocation(String passengerLatLang){
        String[] parts1 = passengerLatLang.split(",");
        Double Platitude =Double.parseDouble(parts1[0]);
        Double PLongitude =Double.parseDouble(parts1[1]);
        return new Double[]{Platitude,PLongitude};
    }


    private void updateMarkerPosition(Location location) {

        LatLng currentLocation = new LatLng(location.getLatitude(), location.getLongitude());

        if (userMarker == null) {
            // Create a new marker if it doesn't exist
            BitmapDescriptor customMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.img_marker1);
            userMarker = mMap.addMarker(new MarkerOptions().position(currentLocation).title("You")
                    .icon(customMarkerIcon));
        } else {
            // Update the existing marker's position
            userMarker.setPosition(currentLocation);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 15f));
    }

    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                Address singleaddress = addressList.get(0);
                LatLng latLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return latLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setTheme(String colour) {
//          buttomBar.setBarBackgroundColor(Color.parseColor(colour));
        getWindow().setStatusBarColor(Color.parseColor(colour));
        getWindow().setNavigationBarColor(Color.parseColor(colour));

    }

    private void setLocationtoTV(Location location) {
        Address address = getAddressFromLatLng( new LatLng(location.getLatitude(), location.getLongitude()));
        String addressString = address.getAddressLine(0);
        if (addressString!=null&&!addressString.isEmpty()){
            selectedAddress = addressString;
            Log.d("xxxx",selectedAddress);
            text1.setText(selectedAddress);
        }
    }

    private Address getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}