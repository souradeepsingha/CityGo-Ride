package com.citygo.user.Fragment;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.citygo.user.Activities.Distance;
import com.citygo.user.Activities.DistanceApi;
import com.citygo.user.Activities.Duration;
import com.citygo.user.Activities.Element;
import com.citygo.user.Activities.GetDistance;
import com.citygo.user.model.DistanceCalculatorModel;
import com.citygo.user.model.DriverModel;
import com.citygo.user.model.preBookRoomModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.citygo.user.adapter.BookDriverListAdapter;
import com.citygo.user.databinding.FragmentPreBookDriverListBinding;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class preBookDriverListFragment extends Fragment {

    String passengerLatLang, passengerEmail, passengerFcmToken, passengerName, passengerNumber, passengerProfileImage, passengerUid, passengerFromWhere,
            passengerToWhere;
    Timestamp pickupTimestamp;

    List<DriverModel> BookDriverList;

    FragmentPreBookDriverListBinding binding;
    private BookDriverListAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPreBookDriverListBinding.inflate(getLayoutInflater(), container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            // Extract data using the keys provided while passing the data
            passengerLatLang = bundle.getString("passengerLatLang");
            passengerEmail = bundle.getString("passengerEmail");
            passengerFcmToken = bundle.getString("PassengerFcmToken");
            passengerName = bundle.getString("PassengerName");
            Log.d("anuj",passengerName);
            passengerNumber = bundle.getString("passengerNumber");
            passengerProfileImage = bundle.getString("passengerProfileImage");
            passengerUid = bundle.getString("PassengerUid");
            passengerFromWhere = bundle.getString("passengerFromWhere");
            passengerToWhere = bundle.getString("passengerToWhere");
            pickupTimestamp = (Timestamp) bundle.getSerializable("pickupTimestamp");
        }

        binding.location1.setText(passengerFromWhere);
        binding.destination1.setText(passengerToWhere);
        LatLng fromLatLng = geocodeLocation(passengerFromWhere);
        LatLng toLatLng = geocodeLocation(passengerToWhere);
        Log.d( "onCreateView32",String.valueOf(fromLatLng));
        Log.d( "onCreateView23",String.valueOf(toLatLng));
         getData(fromLatLng,toLatLng);


        BookDriverList = new ArrayList<>();
        adapter = new BookDriverListAdapter(getContext(), BookDriverList, pickupTimestamp, passengerToWhere, passengerFromWhere,passengerUid,passengerLatLang,passengerName);
        binding.PreBookDriverListRV.setAdapter(adapter);

        FirebaseDatabase.getInstance().getReference().child("preBook")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            BookDriverList.clear();
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                preBookRoomModel model = childSnap.getValue(preBookRoomModel.class);

                                Double distance = DistanceCalculatorModel.calculateDistance(model.getLatLang(), passengerLatLang);
                                if (distance <= 5000.0) {
                                    fetchOtherNodeData(childSnap.getKey());
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return binding.getRoot();
    }

    private void fetchOtherNodeData(String uid) {
        DatabaseReference otherNodeRef = FirebaseDatabase.getInstance().getReference().child("Drivers").child(uid);
        otherNodeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DriverModel model = snapshot.getValue(DriverModel.class);
                BookDriverList.add(model);
                adapter.notifyDataSetChanged(); // Notify the adapter of the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




    }
    private LatLng geocodeLocation(String locationName) {
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
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
        Log.d("onResponse2340",String.valueOf(fromLatLng.latitude));
        Log.d("onResponse2341",String.valueOf(fromLatLng.longitude));
        Log.d("onResponse2342",String.valueOf(toLatLng.latitude ));
        Log.d("onResponse2343",String.valueOf(toLatLng.longitude));
        call.enqueue(new Callback<GetDistance>() {
            @Override
            public void onResponse(Call<GetDistance> call, retrofit2.Response<GetDistance> response) {
                GetDistance details = response.body();
                Log.d("onMapRead2124", String.valueOf(details));
                if (details != null) {
                    int rowsSize = details.getRows() != null ? details.getRows().size() : 0;
                    Log.d("RowsSize1", String.valueOf(rowsSize));
                    if (rowsSize > 0) {
                        List<Element> elements = details.getRows().get(0).getElements();
                        Log.d("onMapRead2722", String.valueOf(elements));
                        if (elements.size() > 0) {
                            Duration duration = elements.get(0).getDuration();
                            Distance distance = elements.get(0).getDistance();
                            if (duration != null && distance != null) {
                                String timeHr = duration.getText();
                                Integer timeSec = duration.getValue();
                                Log.d("onMapRead27222", String.valueOf(timeHr));

                                String disKm = distance.getText();

// Extract the numeric part of the string (remove " km" from the end)
                                String numericValue = disKm.replace(" km", "");
                                Log.d("27222", String.valueOf(numericValue));
                                try {
                                    // Parse the extracted numeric value as a double
                                    double disKmDouble = Double.parseDouble(numericValue);

                                    // Convert kilometers to meters and calculate the cost
                                    double disMeters = disKmDouble * 1000; // Convert kilometers to meters
                                    double cost = (disMeters / 1000) * 10; // Calculate the cost for 1000 meters
                                    Log.d("onMapRead272232", String.valueOf(cost));
                                    // Cast the cost to an integer and set it as text
                                    int costAsInt = (int) cost;
                                  binding.moneyinrupee.setText(costAsInt+"");
                                    Log.d("onResponse234",String.valueOf(costAsInt));


                                } catch (NumberFormatException e) {

                                    e.printStackTrace();

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

}
