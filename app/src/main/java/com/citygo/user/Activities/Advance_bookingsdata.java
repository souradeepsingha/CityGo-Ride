package com.citygo.user.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citygo.user.DataClass.nodeDataClass;
import com.citygo.user.adapter.advanceBookAdapter;
import com.citygo.user.databinding.FragmentAdvanceBookingsdataBinding;
import com.citygo.user.model.DriverModel;
import com.citygo.user.model.advanceBookRVModel;
import com.citygo.user.model.preBookReqModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Advance_bookingsdata extends Fragment {

    FragmentAdvanceBookingsdataBinding binding;
    FirebaseAuth mAuth;
    String passengerUid;
    FirebaseDatabase db;
    ArrayList<advanceBookRVModel> list;
    advanceBookAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         binding=FragmentAdvanceBookingsdataBinding.inflate(getLayoutInflater(), container, false);
         mAuth = FirebaseAuth.getInstance();
         passengerUid = mAuth.getUid();
         db = FirebaseDatabase.getInstance();

         list = new ArrayList<>();
         adapter = new advanceBookAdapter(getContext(),list);

         binding.advanceBookingDataRV.setLayoutManager(new LinearLayoutManager(getActivity()));

         binding.advanceBookingDataRV.setAdapter(adapter);

         db.getReference(nodeDataClass.APPROVED).child(passengerUid)
                 .addValueEventListener(new ValueEventListener() {
                     @Override
                     public void onDataChange(@NonNull DataSnapshot snapshot) {
                         list.clear();
                         for (DataSnapshot ds : snapshot.getChildren()){
                             String driverUid = ds.getKey();
                             String status = ds.getValue(String.class);

                             if (status.equals("Done")){

                                 String node = nodeDataClass.APPROVED;

                                 fetchDistance(driverUid,node);

                             } else if (status.equals("notDone")) {

                                 String node = nodeDataClass.REQUESTS;
                                 fetchDistance(driverUid,node);

                             }


                         }
                     }

                     @Override
                     public void onCancelled(@NonNull DatabaseError error) {

                     }
                 });


        return binding.getRoot();
    }

    private void fetchDistance(String driverUid, String node) {
        db.getReference(nodeDataClass.PRE_BOOK).child(driverUid).child(node)
                .child(passengerUid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        preBookReqModel model = snapshot.getValue(preBookReqModel.class);
                        String passengerFromWhere = model.getPassengerFromWhere();
                        String passengerToWhere = model.getPassengerToWhere();
                        String passengerLatLang = model.getPassengerLatLang();
                        Long pickupTimestamp = model.getPickupTimestamp();

                        fetchDriverDetails(passengerFromWhere,passengerToWhere,passengerLatLang,pickupTimestamp,driverUid,node);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void fetchDriverDetails(String passengerFromWhere, String passengerToWhere,
                                    String passengerLatLang, Long pickupTimestamp, String driverUid, String node) {
        db.getReference(nodeDataClass.DRIVER_NODE).child(driverUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DriverModel model = snapshot.getValue(DriverModel.class);

                        advanceBookRVModel model2 = new advanceBookRVModel(model.getNumber(), model.getTimeStamp(), model.getName(), model.getEmail()
                        ,model.getUid(),model.getIsVerified(),model.getProfileImage(),model.getFcmToken(),model.getAddress(),model.getLicenceImage()
                        ,model.getCarImage(),model.getRcNo(),model.getInsuranceImage(),model.getCarType(),passengerFromWhere,passengerToWhere,passengerLatLang,driverUid,
                                pickupTimestamp,model.getRating(),node);
                        list.add(model2);

                        adapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
}