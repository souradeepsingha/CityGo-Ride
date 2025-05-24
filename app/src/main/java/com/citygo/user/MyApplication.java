package com.citygo.user;

import android.app.Application;
import android.content.Context;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyApplication extends Application {

    private static FirebaseDatabase db;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    public static void deleteUser(String path, String uid, View view, Context context) {
        Log.d("sougata",path);

        FirebaseDatabase.getInstance().getReference(path).child(uid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                showSnackBar(view, "Successfully deleted the User, Please press back button", context, ContextCompat.getColor(context, R.color.green), ContextCompat.getColor(context, R.color.black));


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showSnackBar(view, "Cannot delete the User, Please try again", context, ContextCompat.getColor(context, R.color.red), ContextCompat.getColor(context, R.color.black));

            }
        });
    }

    public static void showSnackBar(View view, String msg, Context context, int snackbarBg, int textColor) {
        if (view != null) {
            Snackbar snackbar = Snackbar.make(view, msg, Snackbar.LENGTH_LONG);
            View snackbarView = snackbar.getView();
            snackbarView.setBackgroundColor(snackbarBg);
            snackbar.setTextColor(textColor);
            snackbar.show();
        }
    }

    public static String formatTimestamp(long timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(timestamp);
        return DateFormat.format("dd/MM/yyyy", cal).toString();
    }

    public static String calculateHour(long pickupTimestamp) {
        // Get the current time in milliseconds
        long currentMillis = System.currentTimeMillis();

        // Calculate the time difference in milliseconds
        long timeDifferenceMillis = pickupTimestamp - currentMillis;

        // Calculate the time difference in hours
        long hoursDifference = timeDifferenceMillis / (60 * 60 * 1000);

        if (hoursDifference >= 24) {
            // If the difference is 24 hours or more, display in days
            long daysDifference = hoursDifference / 24;
            return daysDifference + " days";
        } else {
            return hoursDifference + " hours";
        }
    }

    public static String[] parseTimeStamp(Long pickupTimestamp){
        Date date = new Date(pickupTimestamp);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
        String formattedDate = dateFormat.format(date);

        String[] parts = formattedDate.split(" ");
        return parts;
    }


}
