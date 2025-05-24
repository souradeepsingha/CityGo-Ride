package com.citygo.user;

import android.util.Log;

import org.json.JSONObject;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DeviceToDeviceNotification {

    private static final String FIREBASE_SERVER_KEY = "AAAAvxcylcQ:APA91bGbDNA1qCs7eShCWitYdZMUGvrOKhIjLbPZjPzTwXJ0liNby6SMSl1i-Zn32WCNT7JXLCbNLc123mB4SW_8FGDGE8zJ8vz0wf-1rAkZCErTovo7efAFwk-JwVXleB4m3d0DYBQf";

    public static void sendNotification(String fcmToken, String notificationTitle, String notificationBody) throws Exception {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://fcm.googleapis.com/fcm/send");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setRequestProperty("Authorization", "key=" + FIREBASE_SERVER_KEY);
                    connection.setRequestProperty("Content-Type", "application/json");

                    JSONObject jsonObject = new JSONObject();
                    jsonObject.put("to", fcmToken);
                    jsonObject.put("notification", new JSONObject().put("title", notificationTitle).put("body", notificationBody));

                    String jsonString = jsonObject.toString();
                    byte[] bytes = jsonString.getBytes("UTF-8");

                    connection.setRequestProperty("Content-Length", String.valueOf(bytes.length));
                    connection.setDoOutput(true);

                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(bytes);
                    outputStream.flush();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == 200) {
                        System.out.println("Notification sent successfully");
                        Log.d("sougata","Notification sent successfully");
                    } else {
                        System.out.println("Error sending notification: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }


}

