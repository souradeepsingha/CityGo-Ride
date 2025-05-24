package com.citygo.user.Fragment;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.citygo.user.Activities.homeDashBoardActivity;
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
import com.citygo.user.Activities.emptyview2;
import com.citygo.user.Activities.preBookDriverListActivity;
import com.citygo.user.adapter.PlaceAutoSuggestAdapter;
import com.citygo.user.databinding.FragmentPrebookingBinding;
import com.citygo.user.model.LocationUtils;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class prebookingFragment extends Fragment {

    FragmentPrebookingBinding binding;
    String email;
    String fcmToken;

    String number;
    String profileImage;
    String uid,passengerLatLang,dbTime,DbDate,name;
    String driverId;
    private double latitude, longitude;
    private FusedLocationProviderClient fusedLocationClient;
    private CancellationToken cancellationToken;
    FirebaseAuth mAuth;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;


    private static final int REQUEST_CODE_AUTOCOMPLETE= 1001;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPrebookingBinding.inflate(inflater, container, false);
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




        binding.text3.setOnClickListener(new View.OnClickListener() {
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
                        DbDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(selection));
                        Log.d("sougata",DbDate);
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
                        .setTimeFormat(TimeFormat.CLOCK_12H)
                        .setHour(currentHour)
                        .setMinute(currentMinute)
                        .setInputMode(MaterialTimePicker.INPUT_MODE_CLOCK)
                        .setTitleText("Please Select the Time")
                        .build();
                timePicker.addOnPositiveButtonClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dbTime = String.format(Locale.getDefault(), "%02d", timePicker.getHour()) + ":" +
                                String.format(Locale.getDefault(), "%02d", timePicker.getMinute())+":"+"000";

                        Log.d("sougata",dbTime);

                        int hour = timePicker.getHour();
                        String amPm = (hour < 12) ? "AM" : "PM";
                        hour = (hour % 12 == 0) ? 12 : hour % 12; // Handle 12-hour format
                        String time = String.format(Locale.getDefault(), "%02d:%02d %s", hour, timePicker.getMinute(), amPm);

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
            getData();

        } else {

            requestLocationPermission();
        }
    }

    private void getData() {
        if (homeDashBoardActivity.selectedAddress!=null&&!homeDashBoardActivity.selectedAddress.isEmpty()) {
            LatLng temp = getLatLngFromAddress(homeDashBoardActivity.selectedAddress);
            passengerLatLang = temp.latitude + "," + temp.longitude;
        }
        if (passengerLatLang!=null){
            validateDetails();
        }
        else {
            Toast.makeText(getContext(), "Please try again", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_AUTOCOMPLETE && resultCode == RESULT_OK) {
            // Get the selected address from the intent data
            String destinationAddress = data.getStringExtra("selected_address2");

            // Now you have the selected address, you can update your UI or take any necessary actions
            // For example, set the selected address in a TextView or use it to update the map marker
            // For example:

             binding.text3.setText(destinationAddress);

        }
    }





    private void validateDetails() {
        if (homeDashBoardActivity.selectedAddress.isEmpty()) {
            Toast.makeText(getContext(), "The Location Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.text3.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Destination Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.edittime.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Time Field is Empty", Toast.LENGTH_SHORT).show();
        } else if (binding.idEdtDate.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), "The Date Field is Empty", Toast.LENGTH_SHORT).show();
        }if (passengerLatLang == null || passengerLatLang.isEmpty()) {
            Toast.makeText(getContext(), "Please Enter name of the place correctly", Toast.LENGTH_SHORT).show();
        }
        else {
            formatToTimeStamp(DbDate,dbTime);
            Toast.makeText(getContext(), "Please wait Searching for drivers", Toast.LENGTH_SHORT).show();
        }
    }

    private void formatToTimeStamp(String dateStr, String timeStr) {
        String timestampString = dateStr+" "+timeStr;
        Timestamp parsedTimestamp = Timestamp.valueOf(timestampString);
        startSearching(parsedTimestamp);
        Log.d("sougata",parsedTimestamp+"");
    }



    private void startSearching(Timestamp parsedTimestamp) {
        //startIteration(latLang);
        Intent intent = new Intent(getActivity(), preBookDriverListActivity.class);
// Put the data in the Intent's extras
        intent.putExtra("passengerLatLang", passengerLatLang);
        intent.putExtra("passengerEmail", email);
        intent.putExtra("PassengerFcmToken", fcmToken);
        intent.putExtra("passengerNumber", number);
        intent.putExtra("passengerProfileImage", profileImage);
        intent.putExtra("PassengerUid", uid);
        intent.putExtra("passengerFromWhere", homeDashBoardActivity.selectedAddress);
        intent.putExtra("passengerToWhere", binding.text3.getText().toString());
        intent.putExtra("pickupTimestamp", parsedTimestamp);
        intent.putExtra("passengerName",name);

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


}