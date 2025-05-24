package com.citygo.user.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import com.citygo.user.databinding.ActivityPreBookDriverListBinding;
import com.citygo.user.Fragment.preBookDriverListFragment;
import com.citygo.user.R;

import java.sql.Timestamp;

public class preBookDriverListActivity extends AppCompatActivity {

    String passengerLatLang, passengerEmail, passengerFcmToken, passengerName, passengerNumber, passengerProfileImage, passengerUid, passengerFromWhere,
            passengerToWhere;
    Timestamp pickupTimestamp;

    ActivityPreBookDriverListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityPreBookDriverListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Get the data from the Intent's extras
        Intent intent = getIntent();
         passengerLatLang = intent.getStringExtra("passengerLatLang");
         passengerEmail = intent.getStringExtra("passengerEmail");
         passengerFcmToken = intent.getStringExtra("PassengerFcmToken");
         passengerName = intent.getStringExtra("passengerName");

         passengerNumber = intent.getStringExtra("passengerNumber");
         passengerProfileImage = intent.getStringExtra("passengerProfileImage");
         passengerUid = intent.getStringExtra("PassengerUid");
         passengerFromWhere = intent.getStringExtra("passengerFromWhere");
         passengerToWhere = intent.getStringExtra("passengerToWhere");
          pickupTimestamp = (Timestamp) intent.getSerializableExtra("pickupTimestamp");

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment view = new preBookDriverListFragment();
        ft.replace(R.id.ActivityPreBookDriverListFL,view);
        Bundle bundle = new Bundle();
        view.setArguments(bundle);
        bundle.putString("passengerLatLang",passengerLatLang);
        bundle.putString("passengerEmail", passengerEmail);
        bundle.putString("PassengerFcmToken", passengerFcmToken);
        bundle.putString("PassengerName", passengerName);
        bundle.putString("passengerNumber", passengerNumber);
        bundle.putString("passengerProfileImage", passengerProfileImage);
        bundle.putString("PassengerUid", passengerUid);
        bundle.putString("passengerFromWhere", passengerFromWhere);
        bundle.putString("passengerToWhere", passengerToWhere);
        bundle.putSerializable("pickupTimestamp", pickupTimestamp);
        ft.commit();

    }
}