package com.citygo.user.Activities;

import static android.app.Activity.RESULT_OK;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.citygo.user.databinding.FragmentSecondfragmentBinding;
import com.citygo.user.model.LocationUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class secondfragment extends Fragment {




   FragmentSecondfragmentBinding binding;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String uid,passengerLatLang;
    String driverId;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private CancellationToken cancellationToken;
    FirebaseAuth mAuth;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static final int REQUEST_CODE_AUTOCOMPLETE= 1001;
    AutoCompleteTextView autoCompleteTextView;
  LinearLayout linearLayout;
  TextView textView;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentSecondfragmentBinding.inflate(inflater, container, false);

        mAuth = FirebaseAuth.getInstance();
        Bundle args = getArguments();
        if (args != null) {
            email = args.getString("email");
            fcmToken = args.getString("fcmToken");
            name = args.getString("name");
            number = args.getString("number");
            profileImage = args.getString("profileImage");
            uid = args.getString("Uid");
            driverId = args.getString("driverId");
        }

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

        binding.button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkLocationPermission();
            }
        });

//        binding.autocomplete.setAdapter(new PlaceAutoSuggestAdapter(getContext(), android.R.layout.simple_list_item_1));
//        binding.autocomplete1.setAdapter(new PlaceAutoSuggestAdapter(getContext(), android.R.layout.simple_list_item_1));
//
//        binding.autocomplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                //address 1
//                Log.d("Address : ", binding.autocomplete.getText().toString());
//
//                LatLng latLng = getLatLngFromAddress(binding.autocomplete.getText().toString());
//                if (latLng != null) {
//                    passengerLatLang= latLng.latitude + "," + latLng.longitude;
//                    Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
//                    Address address = getAddressFromLatLng(latLng);
//                    if (address != null) {
////                        Log.d("Address : ", "" + address.getPostalCode());
////                        Log.d("Address Line : ",""+address.getAddressLine(0));
//                        Log.d("Phone : ", "" + address.getPhone());
//                        Log.d("Pin Code : ", "" + address.getPostalCode());
//                        Log.d("Feature : ", "" + address.getFeatureName());
//                        Log.d("More : ", "" + address.getLocality());
//                    } else {
////                        Log.d("Adddress","Address Not Found");
//                    }
//                } else {
//                    Log.d("Lat Lng", "Lat Lng Not Found");
//                }
//
//            }
//        });
//
//        binding.autocomplete1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.d("Address : ", binding.autocomplete1.getText().toString());
//                LatLng latLng = getLatLngFromAddress(binding.autocomplete1.getText().toString());
//                if (latLng != null) {
//                    Log.d("Lat Lng : ", " " + latLng.latitude + " " + latLng.longitude);
//                    Address address = getAddressFromLatLng(latLng);
//                    if (address != null) {
////                        Log.d("Address : ", "" + address.toString());
////                        Log.d("Address Line : ",""+address.getAddressLine(0));
//                        Log.d("Phone : ", "" + address.getPhone());
//                        Log.d("Pin Code : ", "" + address.getPostalCode());
//                        Log.d("Feature : ", "" + address.getFeatureName());
//                        Log.d("More : ", "" + address.getLocality());
//                    } else {
////                        Log.d("Adddress","Address Not Found");
//                    }
//                } else {
//                    Log.d("Lat Lng", "Lat Lng Not Found");
//                }
//            }
//        });

        binding.idEdtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Please select your journey date-")
                        .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
                    @Override
                    public void onPositiveButtonClick(Long selection) {
                        String date = new SimpleDateFormat("dd-MM-yyy", Locale.getDefault()).format(new Date(selection));
                        binding.idEdtDate.setText(date);
                    }
                });
                materialDatePicker.show(getChildFragmentManager(), "tag");
            }
        });

        binding.edittime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                int currentMinute = calendar.get(Calendar.MINUTE);

                MaterialTimePicker timePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(currentHour)
                        .setMinute(currentMinute)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Please Select the Time")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String time = String.format(Locale.getDefault(), "%02d", timePicker.getHour()) + ":" +
                                String.format(Locale.getDefault(), "%02d", timePicker.getMinute());
                        binding.edittime.setText(time);
                    }
                });
                timePicker.show(getChildFragmentManager(),"tag");
            }
        });

        return binding.getRoot();

    }



    private void checkLocationPermission() {
        if (isLocationPermissionGranted()) {

            validateDetails();
        } else {

            requestLocationPermission();
        }
    }

    private void validateDetails() {
        if (binding.autocomplete.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Location Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.autocomplete1.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Destination Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.edittime.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Time Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.idEdtDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Date Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (passengerLatLang.isEmpty()||passengerLatLang==null) {
            Toast.makeText(getContext(), "Please Enter name of the place correctly", Toast.LENGTH_SHORT).show();
        } else {
            startSearching();
            Toast.makeText(getContext(), "Please wait Searching for drivers", Toast.LENGTH_SHORT).show();
        }
    }

    private void getCurrentLocation() {
        if (isLocationPermissionGranted()) {
            if (LocationUtils.isLocationEnabled(getContext())) {
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationToken)
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Handle the location here.
                                if (location != null) {
                                    Log.d("sougata", location.getLatitude() + "," + location.getLongitude());
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                    startSearching();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getContext(), "We are facing trouble getting your location", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                LocationUtils.showTurnOnLocationDialog(getContext());
            }
        } else {
            requestLocationPermission();
        }
    }

    private void startSearching() {
        //startIteration(latLang);
        Intent intent = new Intent(getActivity(), preBookDriverListActivity.class);
// Put the data in the Intent's extras
        intent.putExtra("passengerLatLang", passengerLatLang);
        intent.putExtra("passengerEmail", email);
        intent.putExtra("PassengerFcmToken", fcmToken);
        intent.putExtra("PassengerName", name);
        intent.putExtra("passengerNumber", number);
        intent.putExtra("passengerProfileImage", profileImage);
        intent.putExtra("PassengerUid", uid);
        intent.putExtra("passengerFromWhere", binding.autocomplete.getText().toString());
        intent.putExtra("passengerToWhere", binding.autocomplete1.getText().toString());
        intent.putExtra("pickupTime", binding.edittime.getText().toString());
        intent.putExtra("pickupDate", binding.idEdtDate.getText().toString());

// Start the activity with the intent
        startActivity(intent);
    }

    private boolean isLocationPermissionGranted() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
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
                //    isLocationPermissionGranted = true;
                validateDetails();
            } else {
                // Location permission denied
                Toast.makeText(getContext(), "You have to allow loacation permission to proceed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private LatLng getLatLngFromAddress(String address) {

        Geocoder geocoder = new Geocoder(getContext());
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


    private Address getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if (addresses != null) {
                Address address = addresses.get(0);
                return address;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

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
            textView.setText(selectedAddress);

            // or
            // updateMapMarker(selectedAddress);
        }
    }
}