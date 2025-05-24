package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.citygo.user.R;
import com.citygo.user.databinding.ActivityMainBinding;
import com.citygo.user.Fragment.welcomeFragment;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;

    FirebaseAuth mAuth;
    SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();


        pref = getSharedPreferences("data",MODE_PRIVATE);
        String driverId = pref.getString("driverId","notMade");
        Log.d("sharedName",driverId+"  "+pref.getString("passengerLatLang","cannot get")+" "+" "+pref.getString("fromWhere","cannot get")+" "+pref.getString("fromWhere","cannot get") +pref.getString("toWhere","cannot get")
        +" "+pref.getString("userEmail","cannot get")+" "+ pref.getString("userFcmToken","cannot get") +" "+pref.getString("userName","cannot get")
        +" "+pref.getString("userNumber","cannot get")+" "+pref.getString("userProfileImage","cannot get")+" "+pref.getString("driverId",driverId) + " "+ pref.getString("userId","cannot get"));


        if (mAuth.getUid() != null){

                    if (driverId.equals("notMade")){
                        Intent intent = new Intent(getApplicationContext(), homeDashBoardActivity.class);
                        intent.putExtra("latLang",pref.getString("passengerLatLang","cannot get"));
                        intent.putExtra("fromWhere",pref.getString("fromWhere","cannot get"));
                        intent.putExtra("toWhere",pref.getString("toWhere","cannot get"));
                        intent.putExtra("email",pref.getString("userEmail","cannot get"));
                        intent.putExtra("fcmToken",pref.getString("userFcmToken","cannot get"));
                        intent.putExtra("name",pref.getString("userName","cannot get"));
                        intent.putExtra("number",pref.getString("userNumber","cannot get"));
                        intent.putExtra("profileImage",pref.getString("userProfileImage","cannot get"));
                        intent.putExtra("driverId",pref.getString("driverId",driverId));
                        intent.putExtra("Uid",pref.getString("userId","cannot get"));
                        startActivity(intent);
                        finish();
                    } else  if (driverId.equals("made")){
                        Intent intent = new Intent(getApplicationContext(), waitingActivity.class);
                        intent.putExtra("latLang",pref.getString("passengerLatLang","cannot get"));
                        intent.putExtra("fromWhere",pref.getString("fromWhere","cannot get"));
                        intent.putExtra("toWhere",pref.getString("toWhere","cannot get"));
                        intent.putExtra("email",pref.getString("userEmail","cannot get"));
                        intent.putExtra("fcmToken",pref.getString("userFcmToken","cannot get"));
                        intent.putExtra("name",pref.getString("userName","cannot get"));
                        intent.putExtra("number",pref.getString("userNumber","cannot get"));
                        intent.putExtra("profileImage",pref.getString("userProfileImage","cannot get"));
                        intent.putExtra("driverId",pref.getString("driverId",driverId));
                        intent.putExtra("Uid",pref.getString("userId","cannot get"));
                        startActivity(intent);
                        finish();
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                        intent.putExtra("latLang",pref.getString("passengerLatLang","cannot get"));
                        intent.putExtra("fromWhere",pref.getString("fromWhere","cannot get"));
                        intent.putExtra("toWhere",pref.getString("toWhere","cannot get"));
                        intent.putExtra("email",pref.getString("userEmail","cannot get"));
                        intent.putExtra("fcmToken",pref.getString("userFcmToken","cannot get"));
                        intent.putExtra("name",pref.getString("userName","cannot get"));
                        intent.putExtra("number",pref.getString("userNumber","cannot get"));
                        intent.putExtra("profileImage",pref.getString("userProfileImage","cannot get"));
                        intent.putExtra("driverId",pref.getString("driverId",driverId));
                        intent.putExtra("Uid",pref.getString("userId","cannot get"));
                        finish();
                    }
                }




        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.frameLayout, new welcomeFragment());
            ft.commit();
        }

    }
}