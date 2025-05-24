package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.citygo.user.R;

public class AboutPage extends AppCompatActivity {
LinearLayout privacypolicy;
LinearLayout termsandcondition;
    ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);
        imageView=findViewById(R.id.arrowback1);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(AboutPage.this,MainActivity.class);
                startActivity(myintent);
            }
        });

        privacypolicy=findViewById(R.id.privacypolicy);
        privacypolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(AboutPage.this,PrivacyPolicy.class);
                startActivity(myintent);
            }
        });
        termsandcondition=findViewById(R.id.termsandcondition);
        termsandcondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(AboutPage.this,TermsAndCondition.class);
                startActivity(myintent);
            }
        });



    }
}