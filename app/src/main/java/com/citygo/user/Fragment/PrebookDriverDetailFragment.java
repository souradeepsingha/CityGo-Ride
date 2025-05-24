package com.citygo.user.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.citygo.user.Activities.Advancebooktrack;
import com.citygo.user.DeviceToDeviceNotification;
import com.citygo.user.databinding.FragmentPrebookDriverDetailBinding;
import com.citygo.user.model.preBookReqModel;

import java.sql.Timestamp;
import java.util.HashMap;


public class PrebookDriverDetailFragment extends Fragment {

    String driverName,driverNumber,isVerified,driverUid,driverEmail,driverImage,driverFcmToken,formattedDate,passengerUid
            ,passengerFromWhere,passengerToWhere,passengerLatLang,passengerName;
    Timestamp pickupTimestamp;
    FragmentPrebookDriverDetailBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentPrebookDriverDetailBinding.inflate(getLayoutInflater(), container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            driverName = bundle.getString("DriverName");
            driverNumber = bundle.getString("DriverNumber");
            isVerified = bundle.getString("isVerified");
            driverUid = bundle.getString("DriverUid");
            driverEmail = bundle.getString("DriverEmail");
            driverImage = bundle.getString("DriverImage");
            driverFcmToken = bundle.getString("DriverFcmToken");
            formattedDate = bundle.getString("formattedDate");
            passengerUid = bundle.getString("passengerUid");
            pickupTimestamp = (Timestamp) bundle.getSerializable("pickupTimestamp");
            passengerFromWhere = bundle.getString("passengerFromWhere");
            passengerToWhere = bundle.getString("passengerToWhere");
            passengerLatLang = bundle.getString("passengerLatLang");
            passengerName = bundle.getString("passengerName");
            Log.d("anuj",passengerName);

        }
        //set the data
        binding.NameTv.setText(driverName);
        binding.NumberTv.setText(driverNumber);
        binding.isVerifiedTTv.setText(isVerified);
        binding.EmailTv.setText(driverEmail);
        binding.dateTv.setText(formattedDate);

        //when call button clicked
        binding.callUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", driverNumber, null));
                startActivity(intent);
            }
        });

        //when user wants to book the driver
        binding.bookUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preBookReqModel req = new preBookReqModel(passengerLatLang,passengerFromWhere,passengerToWhere,pickupTimestamp);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("preBook").child(driverUid).child("requests")
                        .child(passengerUid);
                ref.setValue(req).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        //If success show a toast

                        HashMap<String,Object> map = new HashMap<String,Object>();
                        map.put(driverUid,"notDone");
                        FirebaseDatabase.getInstance().getReference().child("approved").child(passengerUid).updateChildren(map);

                        try {
                            DeviceToDeviceNotification.sendNotification(driverFcmToken,"new prebook request",passengerName+" sent you a prebook request");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        Toast.makeText(getContext(), "Req sent successfully", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //If Failure show toast that req send failed
                        Toast.makeText(getContext(), "Req sending failed", Toast.LENGTH_SHORT).show();
                    }
                });

                Intent myintent=new Intent(getActivity(), Advancebooktrack.class);
                myintent.putExtra("driverUid",driverUid);
                myintent.putExtra("pickupTimestamp",pickupTimestamp);
                startActivity(myintent);
            }
        });

        return binding.getRoot();
    }
}