package com.citygo.user.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.citygo.user.databinding.ActivityReviewBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {

    String latLang;
    String fromWhere;
    String toWhere;
    String email;
    String fcmToken;
    String name,ratingId;
    String number;
    String profileImage;
    String driverId;
    String uid;
    float rating;
    ActivityReviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        latLang = intent.getStringExtra("latLang");
        fromWhere = intent.getStringExtra("fromWhere");
        toWhere = intent.getStringExtra("toWhere");
        email = intent.getStringExtra("email");
        fcmToken = intent.getStringExtra("fcmToken");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        profileImage = intent.getStringExtra("profileImage");
        driverId = intent.getStringExtra("driverId");
        ratingId = intent.getStringExtra("ratingId");
        uid = intent.getStringExtra("Uid");


        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String driverName = pref.getString("driverName","null");

        binding.driverNameTV.setText(driverName);

        binding.rateText.setText("Rate Your Last Ride with "+driverName);


    }

    public void GicereviewClicked(View view) {
        Intent intent1 = new Intent(getApplicationContext(), homeDashBoardActivity.class);
        intent1.putExtra("latLang",latLang);
        intent1.putExtra("fromWhere",fromWhere);
        intent1.putExtra("toWhere",toWhere);
        intent1.putExtra("email",email);
        intent1.putExtra("fcmToken",fcmToken);
        intent1.putExtra("name",name);
        intent1.putExtra("number",number);
        intent1.putExtra("profileImage",profileImage);
        intent1.putExtra("driverId",driverId);
        intent1.putExtra("Uid",uid);
        rating = binding.ratingBar.getRating();

        if (rating==0){
            Toast.makeText(this, "please give rating", Toast.LENGTH_SHORT).show();
        }else {
            FirebaseDatabase.getInstance().getReference("Drivers").child(ratingId).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child("rating").exists()){

                        float temp = snapshot.child("rating").getValue(float.class);
                        temp = (temp+rating)/2;

                        Map map = new HashMap<String,Object>();
                        map.put("rating",temp);

                        FirebaseDatabase.getInstance().getReference("Drivers").child(ratingId).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                finish();
                                finishAffinity();
                                startActivity(intent1);
                            }
                        });
                    }else {
                        Map map = new HashMap<String,Object>();
                        map.put("rating",rating);
                        FirebaseDatabase.getInstance().getReference("Drivers").child(ratingId).updateChildren(map).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                finish();
                                finishAffinity();
                                startActivity(intent1);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }
}