package com.citygo.user.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.citygo.user.MyApplication;
import com.citygo.user.model.preBookReqModel;
import com.google.android.gms.maps.model.Marker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.citygo.user.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Advancebooktrack extends AppCompatActivity implements PaymentResultListener {


    String latLang;
    String fromWhere;
    String toWhere;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String driverUid;
    String uid;
    private Marker otherUserMarker;
    Button button;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayoutcoupan;
    ImageView imageview;
    DatabaseReference approved;
    TextView waitingTimeTV, waitingTv;
    FirebaseAuth mAuth;
    LinearLayout linearLayout2;
    ImageView imageView2, imageView3;
    EditText editcoupon;
    Button buttoncoupon;
    TextView Rupevalue;
    Timestamp pickupTimestamp;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancebooktrack);
        imageview = findViewById(R.id.backhome);
        waitingTimeTV = findViewById(R.id.waitingTimeTV);
        waitingTv = findViewById(R.id.waitingTv);
        mAuth = FirebaseAuth.getInstance();
        linearLayout2 = findViewById(R.id.linear2);
        imageView2 = findViewById(R.id.arrowdown);
        imageView3 = findViewById(R.id.arrowright);

        editcoupon = findViewById(R.id.edittextcoupon);
        buttoncoupon = findViewById(R.id.buttoncoupon);
        Rupevalue = findViewById(R.id.rupeevalue);

        Intent intent = getIntent();
        driverUid = intent.getStringExtra("driverUid");
        pickupTimestamp = (Timestamp) getIntent().getSerializableExtra("pickupTimestamp");
        uid = mAuth.getUid();

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        linearLayout2.setVisibility(View.GONE);

        buttoncoupon.setOnClickListener(new View.OnClickListener() {
            private boolean hasCalculated = false; // Add a boolean flag

            @Override
            public void onClick(View view) {
                if (!hasCalculated && editcoupon.getText().toString().equals("citygo")) {
                    // Get the text content of the Rupevalue TextView
                    String rupeeText = Rupevalue.getText().toString();

                    // Convert the rupee text to an integer
                    int rupeeValue1 = Integer.parseInt(rupeeText);

                    // Calculate 20% of the rupee value
                    int twentyPercent = (int) (rupeeValue1 * 0.2);

                    // Calculate 80% of the rupee value
                    int eightyPercent = rupeeValue1 - twentyPercent;

                    // Set the remaining 80% value in the TextView
                    Rupevalue.setText(String.valueOf(eightyPercent));

                    hasCalculated = true; // Set the flag to true after calculation
                }
            }
        });


        // Call your setTheme function with colors and title
        setTheme("#000000", "My Title");


        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        button = findViewById(R.id.payment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment();


            }
        });
        linearLayoutcoupan = findViewById(R.id.linearcoupon);
        linearLayoutcoupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLinearLayoutVisibility();
            }
        });

        DatabaseReference requests = FirebaseDatabase.getInstance().getReference().child("preBook").child(driverUid).child("requests");
        approved = FirebaseDatabase.getInstance().getReference().child("preBook").child(driverUid).child("approved");

        requests.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(uid).exists()) {
                    preBookReqModel model = snapshot.child(uid).getValue(preBookReqModel.class);
                    Long pickupTimestamp = model.getPickupTimestamp();
                    String time = MyApplication.calculateHour(pickupTimestamp);
                    setTime(time, "Please wait for driver accept");
                } else {
                    fetchApprovedNodeData(approved);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void fetchApprovedNodeData(DatabaseReference approved) {
        approved.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                preBookReqModel model = snapshot.child(uid).getValue(preBookReqModel.class);
                Long pickupTimestamp = model.getPickupTimestamp();
                String time = MyApplication.calculateHour(pickupTimestamp);
                setTime(time, "Your Request is Approved");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTime(String time, String waitText) {
        waitingTimeTV.setText(time + " left");
        waitingTv.setText(waitText);
    }

    private void payment() {

        Checkout co = new Checkout();
        JSONObject object = new JSONObject();
        try {
            object.put("name", "Citygo");
            object.put("description", "Citygo");
            object.put("image", "Citygo");
            object.put("currency", "INR");
            object.put("amount", "100");
            JSONObject prefill = new JSONObject();
            prefill.put("contact", "8927645830");
            prefill.put("email", "souradeepsingha542@gmail.com");
            object.put("prefill", prefill);
            co.open(Advancebooktrack.this, object);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "PAYMENT SUCCESS", Toast.LENGTH_SHORT).show();
        String existingData = sharedPreferences.getString("data", "");


        String newData = existingData + "Mahishbathan\nKhagrabari\t";

        editor.putString("data", newData);
        editor.apply();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "PAYMENT FAILED", Toast.LENGTH_SHORT).show();


    }

    private void toggleLinearLayoutVisibility() {
        if (linearLayout2.getVisibility() == View.VISIBLE) {
            linearLayout2.setVisibility(View.GONE);
            imageView2.setVisibility(View.GONE);
            imageView3.setVisibility(View.VISIBLE);
        } else {
            linearLayout2.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.GONE);
        }
    }

    private void setTheme(String color, String title) {
        // Use resources for colors
        int parsedColor = Color.parseColor(color);


        // Set the status bar and navigation bar color
        getWindow().setStatusBarColor(parsedColor);
        getWindow().setNavigationBarColor(parsedColor);
    }

}