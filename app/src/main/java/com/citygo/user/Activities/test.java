package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.citygo.user.R;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;

public class test extends AppCompatActivity {
    private static final String DATA_URL = "https://brick-red-whistle.000webhostapp.com/app/cityname.php";
    private TextView dataTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        dataTextView = findViewById(R.id.dataTextView);
    }

    // Method called when the "Fetch Data" button is clicked
    public void fetchData(View view) {
        // Create a RequestQueue using Volley
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Fetch data using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                DATA_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON data here and set it to the TextView
                        StringBuilder dataBuilder = new StringBuilder();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String data = response.getString(i);
                                dataBuilder.append(data).append("\n");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        dataTextView.setText(dataBuilder.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error if data fetching fails
                        error.printStackTrace();
                    }
                }
        );

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }
}
