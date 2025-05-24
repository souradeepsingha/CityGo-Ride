package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.citygo.user.R;

public class Support extends AppCompatActivity {
LinearLayout securitylt;
LinearLayout layoutride;
LinearLayout layoutaccount;
LinearLayout layoutservices;
LinearLayout layouttechnical;
LinearLayout layoutpayment;

    ImageView imageView;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        imageView=findViewById(R.id.arrowback4);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(Support.this,MainActivity.class);
                startActivity(myintent);
            }
        });
        securitylt=findViewById(R.id.security);
       securitylt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
            Intent intent=new Intent(Support.this,Security.class);
            startActivity(intent);
           }
       });
       layoutride=findViewById(R.id.layoutride);
       layoutride.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(Support.this,SupportRide.class);
               startActivity(intent);
           }
       });
        layoutaccount=findViewById(R.id.LayoutAccount);
        layoutaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Support.this,SupportAccount.class);
                startActivity(intent);
            }
        });
        layoutservices=findViewById(R.id.layoutservices1);
        layoutservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Support.this,Supportservice1.class);
                startActivity(intent);
            }
        });
        layouttechnical=findViewById(R.id.layouttechnical);
        layouttechnical.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Support.this,SupportTechnical.class);
                startActivity(intent);
            }
        });
        layoutpayment=findViewById(R.id.layoutpayment);
        layoutpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Support.this,SupportPayment.class);
                startActivity(intent);
            }
        });

    }
}