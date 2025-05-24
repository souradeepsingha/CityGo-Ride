package com.citygo.user.Activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.citygo.user.R;

public class Coupon extends AppCompatActivity {

    EditText editText;

    Button button;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        editText=findViewById(R.id.editTextCouponCode);

        button=findViewById(R.id.buttonapply);

// Find the TextView in your coupon activity's layout
        TextView rupeeTextView = findViewById(R.id.rupeeTextView);

        // Get the intent that started this activity
        Intent intent = getIntent();

        String code=editText.getText().toString();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (intent != null ) {
                    // Get the rupee value from the extras
                    if(editText.getText().toString().equals("citygo")) {
                        String rupeeValueString = intent.getStringExtra("rupee");

                        // Convert the rupee value from String to int
                        int rupeeValue = Integer.parseInt(rupeeValueString);

                        // Calculate 20% of the rupee value
                        int twentyPercent = (int) (rupeeValue * 0.2);

                        // Calculate 80% of the rupee value
                        int eightyPercent = rupeeValue - twentyPercent;

                       //  Set the remaining 80% value in the TextView

                        Intent mapsIntent = new Intent(Coupon.this, MapsActivity.class);
                        mapsIntent.putExtra("eightyPercent", String.valueOf(eightyPercent));
                        startActivity(mapsIntent);
                    }

                    else{
                        rupeeTextView.setText("Couponcode is not Valid");
                    }

                } else {
                    // Handle the case when the "rupee" extra is not found
                    rupeeTextView.setText("Couponcode is not Valid");
                }

            }
        });





    }
}