package com.citygo.user.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.citygo.user.model.DistanceCalculatorModel;
import com.citygo.user.Activities.MainActivity;
import com.citygo.user.DeviceToDeviceNotification;
import com.citygo.user.databinding.FragmentWaitingBinding;
import com.citygo.user.model.DriverModel;
import com.citygo.user.model.RoomModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;

public class WaitingFragment extends Fragment {

    String fcmToken;
    String name;
    String number;
    String profileImage;
    String uid;
    String driverId;
    String latLang;
    String fromWhere;
    String toWhere;
    FragmentWaitingBinding binding;
    FirebaseDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWaitingBinding.inflate(inflater, container, false);
        db = FirebaseDatabase.getInstance();

        Bundle bundle = getArguments();
        if (bundle != null) {
            driverId = bundle.getString("driverId");
            number = bundle.getString("number");
            uid = bundle.getString("uid");
            fcmToken = bundle.getString("fcmToken");
            name = bundle.getString("name");
            profileImage = bundle.getString("profileImage");
            latLang = bundle.getString("latLang");
            fromWhere = bundle.getString("fromWhere");
            toWhere = bundle.getString("toWhere");
        }

        binding.cancelRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = db.getReference("Passengers").child(uid);
                HashMap<String, Object> map = new HashMap<>();
                map.put("driverId", "notMade");

                SharedPreferences pref = requireContext().getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("driverId","notMade");
                editor.commit();

                ref.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        deleteReq();

                    }
                });
            }
        });

        if (driverId.equals("notMade")) {
            startIteration();
        }

        FirebaseMessaging.getInstance().subscribeToTopic("my_topic");


        return binding.getRoot();
    }

    private void deleteReq() {
        FirebaseDatabase.getInstance().getReference("Room").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getChildrenCount()>0){
                    for(DataSnapshot ds : snapshot.getChildren()){


                        FirebaseDatabase.getInstance().getReference("Room")
                                .child(ds.getKey()).child("requests").child(uid).removeValue();

                    }
                }

                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
                Toast.makeText(getContext(), "Ride Cancelled", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void startIteration() {

        db.getReference().child("Room")
                .orderByChild("isAvailable")
                .equalTo(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        if (snapshot.getChildrenCount() > 0) {
                            for (DataSnapshot childSnap : snapshot.getChildren()) {
                                RoomModel model = childSnap.getValue(RoomModel.class);
                                // check for distance if less than 3 km send request and notification
                                Double distance = DistanceCalculatorModel.calculateDistance(latLang, model.getLatLang());
                                if (distance <= 3.0) {
                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put("PassengerName", name);
                                    map.put("PassengerUid", uid);
                                    map.put("passengerLatLang", latLang);
                                    map.put("passengerNumber", number);
                                    map.put("passengerFromWhere", fromWhere);
                                    map.put("passengerToWhere", toWhere);
                                    map.put("PassengerFcmToken", fcmToken);
                                    map.put("passengerProfileImage", profileImage);

                                    FirebaseDatabase.getInstance().getReference("Room")
                                            .child(childSnap.getKey()).child("requests").child(uid).setValue(map);



                                    sendNotification(childSnap.getKey());

                                }
                            }

                            HashMap<String, Object> map = new HashMap<>();
                            map.put("driverId", "made");
                            FirebaseDatabase.getInstance().getReference("Passengers").child(uid).updateChildren(map);
                            SharedPreferences pref = requireContext().getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("driverId","made");
                            editor.commit();



                        } else {
                            Toast.makeText(getContext(), "No driver available at this moment ,please try after sometime", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError error) {
                        // Handle onCancelled event if needed
                    }
                });
    }

    private void sendNotification(String key) {
        DatabaseReference driversRef = FirebaseDatabase.getInstance().getReference("Drivers").child(key);

        driversRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    DriverModel model = snapshot.getValue(DriverModel.class);

                    if (model != null) {
                        try {
                            String fcmToken = model.getFcmToken();
                            if (fcmToken != null && !fcmToken.isEmpty()) {
                                DeviceToDeviceNotification.sendNotification(fcmToken, "New Ride Available", name + " sent you a ride request");
                            } else {
                                // Handle the case where FCM token is null or empty
                            }
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        // Handle the case where model is null
                    }
                } else {
                    // Handle the case where the snapshot does not exist
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle database read error
            }
        });
    }

}