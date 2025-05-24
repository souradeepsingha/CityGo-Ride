package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.citygo.user.R;

public class CopyCoupon extends AppCompatActivity {

TextView copytext;
ImageView copyimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_coupon);
copyimg=findViewById(R.id.copyimg);
copytext=findViewById(R.id.copytext);
copyimg.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        ClipboardManager clipboardManager=(ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clipData=ClipData.newPlainText("Copy",copytext.getText().toString());
        clipboardManager.setPrimaryClip(clipData);

    }
});



    }
}