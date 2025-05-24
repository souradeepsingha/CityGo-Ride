package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.citygo.user.Fragment.welcomeFragment;
import com.citygo.user.R;

public class Introscreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introscreen);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Introscreen.this, welcomeFragment.class));
                finish();
            }
        },1500);

    }
}