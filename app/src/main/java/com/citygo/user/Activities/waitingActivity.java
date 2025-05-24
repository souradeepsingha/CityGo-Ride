package com.citygo.user.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.citygo.user.Fragment.WaitingFragment;
import com.citygo.user.R;
import com.citygo.user.databinding.ActivityWaitingBinding;
import com.citygo.user.model.FirebaseModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class waitingActivity extends AppCompatActivity {

    ActivityWaitingBinding binding;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String uid;
    String driverId;
    String latLang;
    String fromWhere;
    String toWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWaitingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String fcmToken = intent.getStringExtra("fcmToken");
        String name = intent.getStringExtra("name");
        String number = intent.getStringExtra("number");
        String profileImage = intent.getStringExtra("profileImage");
        String uid = intent.getStringExtra("Uid");
        String driverId = intent.getStringExtra("driverId");
        String latLang = intent.getStringExtra("latLang");
        String fromWhere = intent.getStringExtra("fromWhere");
        String toWhere = intent.getStringExtra("toWhere");
        DatabaseReference temp = FirebaseDatabase.getInstance().getReference("Passengers").child(uid);
        temp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseModel model = snapshot.getValue(FirebaseModel.class);
                Log.d("chutiyappa",model.getDriverId());
                if (!model.getDriverId().equals("notMade")&&!model.getDriverId().equals("made")){
                    editor.putString("driverId",model.getDriverId());
                    editor.commit();
                    Intent intent1 = new Intent(getApplicationContext(), MapsActivity.class);
                    intent1.putExtra("latLang",model.getLatLang());
                    intent1.putExtra("fromWhere",model.getFromWhere());
                    intent1.putExtra("toWhere",model.getToWhere());
//                    intent1.putExtra("fromwherelatlang", model.fromwherelatlang());
//                    intent1.putExtra("towherelatlang", towherelatlang);
                    intent1.putExtra("email",model.getEmail());
                    intent1.putExtra("fcmToken",model.getFcmToken());
                    intent1.putExtra("name",model.getName());
                    intent1.putExtra("number",model.getNumber());
                    intent1.putExtra("profileImage",model.getProfileImage());
                    intent1.putExtra("driverId",model.getDriverId());
                    intent1.putExtra("Uid",model.getUid());
                    startActivity(intent1);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        if (driverId.equals("notMade") ||driverId.equals("made")){
            Bundle bundle = new Bundle();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment view = new WaitingFragment();
            ft.add(R.id.frameLayout,view);
            ft.commit();
            view.setArguments(bundle);
            bundle.putString("driverId",driverId);
            bundle.putString("number",number);
            bundle.putString("uid",uid);
            bundle.putString("fcmToken",fcmToken);
            bundle.putString("name",name);
            bundle.putString("profileImage",profileImage);
            bundle.putString("latLang",latLang);
            bundle.putString("fromWhere",fromWhere);
            bundle.putString("toWhere",toWhere);
            Log.d("cannotgettheeroor", "reaching that fragment");
        }else {


        }

    }
}