package com.citygo.user.Activities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.citygo.user.DataClass.nodeDataClass;
import com.citygo.user.MyApplication;
import com.citygo.user.adapter.userCurrentBookHistoryAdapter;
import com.citygo.user.databinding.FragmentCurrenbookingsDataBinding;
import com.citygo.user.model.DriverModel;
import com.citygo.user.model.userCurrentBookHistoryModel;
import com.citygo.user.model.userCurrentBookHistoryRVModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Currenbookings_data extends Fragment {

    FragmentCurrenbookingsDataBinding binding;
    FirebaseDatabase db;
    FirebaseAuth mAuth;
    ArrayList<userCurrentBookHistoryRVModel> list;
    userCurrentBookHistoryAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         binding=FragmentCurrenbookingsDataBinding.inflate(getLayoutInflater(), container, false);
         db = FirebaseDatabase.getInstance();
         mAuth = FirebaseAuth.getInstance();
         list = new ArrayList<>();
         adapter = new userCurrentBookHistoryAdapter(getContext(),list);
         binding.userCurrentBookHistoryRV.setAdapter(adapter);

        DatabaseReference postsRef=db.getReference(nodeDataClass.HISTORY).child(nodeDataClass.PASSENGERS).child(mAuth.getUid())
                 .child(nodeDataClass.CURRENT_BOOK);
        Query query = postsRef.orderByChild(nodeDataClass.TIME_STAMP);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot ds :snapshot.getChildren()){
                    userCurrentBookHistoryModel model = ds.getValue(userCurrentBookHistoryModel.class);
                    Long TimeStamp = model.getTimeStamp();
                    String FromWhere = model.getFromWhere();
                    String ToWhere = model.getToWhere();
                    String DriverUid = model.getDriverUid();
                    fetchDriverData(TimeStamp,FromWhere,ToWhere,DriverUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }

    private void fetchDriverData(Long timeStamp, String fromWhere, String toWhere, String DriverUid) {

        db.getReference(nodeDataClass.DRIVER_NODE).child(DriverUid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        DriverModel driverModel = snapshot.getValue(DriverModel.class);
                        Double rating = driverModel.getRating();
                        String formattedNumber = String.format("%.1f", rating);
                        userCurrentBookHistoryRVModel model1 = new userCurrentBookHistoryRVModel(driverModel.getTimeStamp(),fromWhere
                        ,toWhere, driverModel.getUid(), driverModel.getName(),driverModel.getEmail(),
                                driverModel.getNumber(),formattedNumber,
                                MyApplication.formatTimestamp(driverModel.getTimeStamp()));

                        list.add(model1);

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }
}