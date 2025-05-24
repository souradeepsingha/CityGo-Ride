package com.citygo.user.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.citygo.user.R;

import java.util.ArrayList;

public class payment_option extends AppCompatActivity {

    EditText nametxt, upiIdtxt, msgtxt, amttxt, transIdtxt, refIdtxt;

    Button paybtn;

    final int PAY_REQUEST = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_option);

        nametxt = findViewById(R.id.edtname);
        upiIdtxt = findViewById(R.id.edtupiid);
        msgtxt = findViewById(R.id.edtmsg);
        amttxt = findViewById(R.id.edtamt);
        transIdtxt = findViewById(R.id.edttnid);
        refIdtxt = findViewById(R.id.edtrefid);

        paybtn = findViewById(R.id.btnpay);
        paybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nametxt.getText().toString();
                String upiId = upiIdtxt.getText().toString();
                String amt = amttxt.getText().toString();
                String msg = msgtxt.getText().toString();

                String tnid  = transIdtxt.getText().toString();
                String refId = refIdtxt.getText().toString();

                if(name.isEmpty() || upiId.isEmpty()){
                    Toast.makeText(payment_option.this, "Name and Upi Id is necessary", Toast.LENGTH_SHORT).show();
                }else PayUsingUpi(name,upiId,amt,msg,tnid,refId);
            }
        });
    }

    private void PayUsingUpi(String name,String upiId,String amt,String msg, String trnId, String refId){
        Uri uri = new Uri.Builder()
                .scheme("upi").authority("pay")
                .appendQueryParameter("pa",upiId)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",msg)
                .appendQueryParameter("am",amt)
                .appendQueryParameter("tid",trnId)
                .appendQueryParameter("tr",refId)
                .appendQueryParameter("cu","INR")
                .build();

        Intent upiIntent = new Intent(Intent.ACTION_VIEW);
        upiIntent.setData(uri);
        Intent chooser = Intent.createChooser(upiIntent,"Pay");
        if(chooser.resolveActivity(getPackageManager()) != null){
            startActivityForResult(chooser,PAY_REQUEST);
        }else{
            Toast.makeText(this, "No UPI app found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PAY_REQUEST){

            if(isInternetAvailabe(payment_option.this)){

                if (data == null) {
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add("nothing");
                    String temp = "nothing";
                    Toast.makeText(this, "Transaction not complete", Toast.LENGTH_SHORT).show();
                }else {
                    String text = data.getStringExtra("response");
                    ArrayList<String> dataList = new ArrayList<>();
                    dataList.add(text);

                    upiPaymentCheck(text);
                }
            }

        }
    }

    void upiPaymentCheck(String data){
        String str = data;

        String payment_cancel = "";
        String status = "";
        String response[] = str.split("&");

        for (int i = 0; i < response.length; i++)
        {
            String equalStr[] = response[i].split("=");
            if(equalStr.length >= 2)
            {
                if (equalStr[0].toLowerCase().equals("Status".toLowerCase()))
                {
                    status = equalStr[1].toLowerCase();
                }
            }
            else
            {
                payment_cancel = "Payment cancelled";
            }
        }
        if(status.equals("success")){
            Toast.makeText(this, "Transaction Successfull", Toast.LENGTH_SHORT).show();
            Intent myinent=new Intent(payment_option.this,MapsActivity.class);
            startActivity(myinent);
        }else if("Payment cancelled".equals(payment_cancel)){
            Toast.makeText(this, "payment cancelled by user", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Transaction failed", Toast.LENGTH_SHORT).show();
        }
    }
    public static boolean isInternetAvailabe(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
        if(connectivityManager != null){
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if(networkInfo.isConnected() && networkInfo.isConnectedOrConnecting() && networkInfo.isAvailable()){
                return true;
            }
        }
        return false;
    }
}