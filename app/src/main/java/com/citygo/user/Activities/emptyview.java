package com.citygo.user.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.citygo.user.R;
import com.citygo.user.adapter.PlaceAutoSuggestAdapter;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class emptyview extends AppCompatActivity {
    public static ArrayList<String> selectedAddressesList = new ArrayList<>();
    private ArrayAdapter<String> recentSearchAdapter;
    private static final int MAX_RECENT_ADDRESSES = 4;
    private ListView listView;
    private ArrayList<String> driverDataList;
    private ArrayAdapter<String> adapter;
    private RequestQueue requestQueue;
    private static final String DATA_URL = "https://brick-red-whistle.000webhostapp.com/app/cityname.php";



    TextView textView;
    LinearLayout copypagelinearback;
    Button button1;
    Button button2;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emptyview);
        copypagelinearback=findViewById(R.id.copypagelinearback1);


        copypagelinearback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myintent=new Intent(emptyview.this,homeDashBoardActivity.class);
                startActivity(myintent);
            }
        });


//        button1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//            }
//        });



       textView=findViewById(R.id.textView);


        ArrayList<String> dataList = new ArrayList<>();
        dataList.add("CoochBehar");
        dataList.add("Siliguri");

        // Add more items to the dataList as needed
        ListView listView = findViewById(R.id.listView);
        MyAdapter adapter = new MyAdapter(this, dataList);
        listView.setAdapter(adapter);

        requestQueue = Volley.newRequestQueue(this);

        // Fetch data using Volley
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                DATA_URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        // Process the JSON data and add it to the driverDataList
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                String data = response.getString(i);
                                driverDataList.add(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        // Notify the adapter about the data change
                        adapter.notifyDataSetChanged();
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

        final AutoCompleteTextView autoCompleteTextView = findViewById(R.id.autocomplete);
        autoCompleteTextView.requestFocus();
        final ListView recentSearchListView = findViewById(R.id.recentSearchListView);

        // Initialize the ArrayAdapter for recent search list
        recentSearchAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, selectedAddressesList);
        recentSearchListView.setAdapter(recentSearchAdapter);
        if (selectedAddressesList.size() == 0) {
            textView.setVisibility(View.VISIBLE);
        }
        else{
            textView.setVisibility(View.INVISIBLE);
        }

        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(emptyview.this, android.R.layout.simple_list_item_1));

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAddress = autoCompleteTextView.getText().toString();
                Log.d("Selected Address: ", selectedAddress);

                LatLng userLatLang = getLatLngFromAddress(selectedAddress);

                // Add the selected address at the beginning of the list
                selectedAddressesList.add(0, selectedAddress);

                // Limit the list to only store the last four addresses
                if (selectedAddressesList.size() > MAX_RECENT_ADDRESSES) {
                    selectedAddressesList.remove(selectedAddressesList.size() - 1);
                }




// Update the adapter with the latest data


                // Send the selected address back to MainActivity using Intent
                Intent intent = new Intent();
                intent.putExtra("selected_address", selectedAddress);
                intent.putExtra("userLatLang",userLatLang);
                setResult(RESULT_OK, intent);
                finish(); // Close the emptyview activity and return to MainActivity

                // Update the recentSearchListView with the updated selectedAddressesList
                recentSearchAdapter.notifyDataSetChanged();
            }
        });

        // Retrieve the addresses from SharedPreferences when the activity is created

    }

    // Method to save the selectedAddressesList using SharedPreferences


    // Method to load the selectedAddressesList from SharedPreferences

    private LatLng getLatLngFromAddress(String address) {

        Geocoder geocoder = new Geocoder(getApplicationContext());
        List<Address> addressList;

        try {
            addressList = geocoder.getFromLocationName(address, 1);
            if (addressList != null) {
                Address singleaddress = addressList.get(0);
                LatLng latLng = new LatLng(singleaddress.getLatitude(), singleaddress.getLongitude());
                return latLng;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

}
