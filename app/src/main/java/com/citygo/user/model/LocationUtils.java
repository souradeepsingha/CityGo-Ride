package com.citygo.user.model;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;

public class LocationUtils {
    public static boolean isLocationEnabled(Context context) {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        boolean isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isLocationEnabled;
    }

    public static void showTurnOnLocationDialog(Context context) {
//        new KAlertDialog(context, WARNING_TYPE)
//                .setTitleText("Turn on location!!!")
//                .setContentText("Please Turn on Location from settings , click the below button to go to settings..")
//                .setConfirmClickListener("OK", new KAlertDialog.KAlertClickListener() {
//                    @Override
//                    public void onClick(KAlertDialog kAlertDialog) {
//                        Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                        context.startActivity(settingsIntent);
//                        kAlertDialog.dismiss();
//                    }
//                })
//                .show();

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Turn on location!!!");
        builder.setMessage("Please Turn on Location from settings , click the below button to go to settings..");

        // Create a custom button
        Button customButton = new Button(context);
        customButton.setText("Ok");
        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Do something when the custom button is clicked
                Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(settingsIntent);
            }
        });

        builder.setView(customButton);
        builder.setPositiveButton("OK", null);
        builder.show();


    }
}
