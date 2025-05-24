package com.citygo.user.Activities;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.citygo.user.R;
import com.citygo.user.model.LocationUtils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.List;

public class firstfragment extends Fragment {

    private double latitude;
    private double longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private CancellationToken cancellationToken;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private static final int REQUEST_CODE_AUTOCOMPLETE= 1001;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String uid;
    String driverId;

    FirebaseAuth mAuth;
    TextView text;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    LinearLayout copycoupan;

    private void checkLocationPermission() {
        if (isLocationPermissionGranted()) {

            validateDetails();
        } else {

            requestLocationPermission();
        }
    }
    private void validateDetails() {
        if (text.getText().toString().isEmpty()){
            Toast.makeText(getContext(), "The Destination Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (homeDashBoardActivity.selectedAddress.isEmpty()) {
            Toast.makeText(getContext(), "The Location Field is Empty", Toast.LENGTH_SHORT).show();
        }else {
            getUserLocation();
            Toast.makeText(getContext(), "Please wait Searching for drivers", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myview = inflater.inflate(R.layout.fragment_firstfragment, container, false);

        copycoupan=myview.findViewById(R.id.copycopon);



        Intent intent = getActivity().getIntent();
        email = ((Intent) intent).getStringExtra("email");
        fcmToken = intent.getStringExtra("fcmToken");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        profileImage = intent.getStringExtra("profileImage");
        uid = intent.getStringExtra("Uid");
        driverId = intent.getStringExtra("driverId");


        mAuth = FirebaseAuth.getInstance();

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
        cancellationToken = new CancellationToken() {
            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }

            @Override
            public boolean isCancellationRequested() {
                return false;
            }
        };


        sharedPreferences = getContext().getSharedPreferences(getString(R.string.app_name), MODE_PRIVATE);
        editor = sharedPreferences.edit();


        text=myview.findViewById(R.id.text3);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (homeDashBoardActivity.selectedAddress==null||homeDashBoardActivity.selectedAddress.isEmpty()) {
                    Toast.makeText(getActivity(), "Location is empty", Toast.LENGTH_SHORT).show();
                } else {
                    Intent myintent = new Intent(getActivity(), emptyview2.class);
                    startActivityForResult(myintent, REQUEST_CODE_AUTOCOMPLETE);
                }

            }
        });


copycoupan.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent myintent=new Intent(getActivity(),CopyCoupon.class);
        startActivity(myintent);
    }
});









        final Button searchBtn = myview.findViewById(R.id.button1);

//        For Update the layout of simple list item 1









        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });



        // Inflate the layout for this fragment
        return myview;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK) {
            // Get the selected address from the intent data
            String selectedAddress = data.getStringExtra("selected_address2");

            // Now you have the selected address, you can update your UI or take any necessary actions
            // For example, set the selected address in a TextView or use it to update the map marker
            // For example:
             text.setText(selectedAddress);
            SharedPreferences pref = getContext().getSharedPreferences("data",MODE_PRIVATE);
            SharedPreferences.Editor editor = pref.edit();
            editor.putString("toWhere",selectedAddress);
            editor.commit();

        }
    }













    private void startSearching() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Passengers").child(uid);
        HashMap<String, Object> map = new HashMap<>();

        map.put("latLang", homeDashBoardActivity.userLatLang.latitude + "," + homeDashBoardActivity.userLatLang.longitude);
        map.put("fromWhere", homeDashBoardActivity.selectedAddress);
        map.put("toWhere", text.getText().toString());
        SharedPreferences pref = getContext().getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("fromWhere",homeDashBoardActivity.selectedAddress);
        editor.putString("toWhere", text.getText().toString());
        editor.commit();
        ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Intent intent = new Intent(getContext(), ShowMoneyPage.class);
                intent.putExtra("latLang", homeDashBoardActivity.userLatLang.latitude + "," + homeDashBoardActivity.userLatLang.longitude);
                intent.putExtra("fromWhere", homeDashBoardActivity.selectedAddress);
                intent.putExtra("toWhere",  text.getText().toString());
                intent.putExtra("email", email);
                intent.putExtra("fcmToken", fcmToken);
                intent.putExtra("name", name);
                intent.putExtra("number", number);
                intent.putExtra("profileImage", profileImage);
                intent.putExtra("driverId", driverId);
                intent.putExtra("Uid", uid);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    private boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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
                // Location permission granted, proceed with the logic
                //
               validateDetails();

            } else {
                // Location permission denied
                Toast.makeText(getContext(), "You have to allow location permission to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getUserLocation() {
        if (homeDashBoardActivity.selectedAddress!=null&& !homeDashBoardActivity.selectedAddress.isEmpty()){
            startSearching();
        }
    }



    private LatLng getLatLngFromAddress(String address) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList;
        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null && !addressList.isEmpty()) {
                Address singleAddress = addressList.get(0);
                return new LatLng(singleAddress.getLatitude(), singleAddress.getLongitude());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Address getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext());
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
