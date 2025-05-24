package com.citygo.user.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.citygo.user.DataClass.nodeDataClass;
import com.citygo.user.R;
import com.citygo.user.databinding.ActivityMapsBinding;
import com.citygo.user.model.DriverModel;
import com.citygo.user.model.FirebaseModel;
import com.citygo.user.model.RoomModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, PaymentResultListener {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    String latLang;
    String fromwherelatlang,towherelatlang;
    String fromWhere;
    String toWhere;
    String email;
    String fcmToken;
    String name;
    String number;
    String profileImage;
    String driverId;
    String uid;
    private Marker otherUserMarker;
    Button button;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout linearLayoutcoupan;
    TextView text1;
    EditText editcoupon;
    Button buttoncoupon;
    TextView Rupevalue;



    LinearLayout linearLayout2;
    ImageView imageView2,imageView3;
    String lat,lang;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        latLang = intent.getStringExtra("latLang");
        fromwherelatlang=intent.getStringExtra("fromwherelatlang");
        towherelatlang=intent.getStringExtra("towherelatlang");
        fromWhere = intent.getStringExtra("fromWhere");
        toWhere = intent.getStringExtra("toWhere");
        email = intent.getStringExtra("email");
        fcmToken = intent.getStringExtra("fcmToken");
        name = intent.getStringExtra("name");
        number = intent.getStringExtra("number");
        profileImage = intent.getStringExtra("profileImage");
        driverId = intent.getStringExtra("driverId");
        uid = intent.getStringExtra("Uid");

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//      getDataRupee(fromwherelatlang,towherelatlang);
//        if(fromWhere!=null) {
//            Log.d("latlang12", String.valueOf(fromWhere));
//        }

        Log.d( "test211", String.valueOf(fromWhere));
        Log.d( "test2112", String.valueOf(toWhere));
        LatLng fromLatLng = geocodeLocation(fromWhere);
        LatLng toLatLng = geocodeLocation(toWhere);
        Log.d( "test2117", String.valueOf(fromLatLng));
        Log.d( "test2110", String.valueOf(toLatLng));

        saveDriverDetails(driverId);


        getDataRupee(fromLatLng,toLatLng);
        // Call your setTheme function with colors and title
        setTheme("#000000", "My Title");

        editcoupon=findViewById(R.id.edittextcoupon);
        buttoncoupon=findViewById(R.id.buttoncoupon);
//        Rupevalue=findViewById(R.id.rupeevalue);
        linearLayout2=findViewById(R.id.linear2);
        imageView2=findViewById(R.id.arrowdown);
        imageView3=findViewById(R.id.arrowright);
        button=findViewById(R.id.payment);
        linearLayout2.setVisibility(View.GONE);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payment();


            }
        });
        linearLayoutcoupan=findViewById(R.id.linearcoupon);
        linearLayoutcoupan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleLinearLayoutVisibility();
            }
        });

        buttoncoupon.setOnClickListener(new View.OnClickListener() {
            private boolean hasCalculated = false; // Add a boolean flag

            @Override
            public void onClick(View view) {
                if (!hasCalculated && editcoupon.getText().toString().equals("citygo")) {
                    // Get the text content of the Rupevalue TextView
                    String rupeeText = binding.textdistance.getText().toString();

                    // Convert the rupee text to an integer
                    int rupeeValue1 = Integer.parseInt(rupeeText);

                    // Calculate 20% of the rupee value
                    int twentyPercent = (int) (rupeeValue1 * 0.2);

                    // Calculate 80% of the rupee value
                    int eightyPercent = rupeeValue1 - twentyPercent;

                    // Set the remaining 80% value in the TextView
                    binding.textdistance.setText(String.valueOf(eightyPercent));

                    hasCalculated = true; // Set the flag to true after calculation
                }
            }
        });

        FirebaseDatabase.getInstance().getReference("Passengers")
                .child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        FirebaseModel model = snapshot.getValue(FirebaseModel.class);

                        if (model.getDriverId().equals("notMade")){
                            Intent intent1 = new Intent(getApplicationContext(), ReviewActivity.class);
                            intent1.putExtra("latLang",model.getLatLang());
                            intent1.putExtra("fromWhere",model.getFromWhere());
                            intent1.putExtra("toWhere",toString());
                            intent1.putExtra("email",model.getEmail());
                            intent1.putExtra("fcmToken",model.getFcmToken());
                            intent1.putExtra("name",model.getName());
                            intent1.putExtra("number",model.getNumber());
                            intent1.putExtra("profileImage",model.getProfileImage());
                            intent1.putExtra("driverId",model.getDriverId());
                            intent1.putExtra("Uid",model.getUid());
                            intent1.putExtra("ratingId",driverId);
                            SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit();
                            editor.putString("driverId","notMade");
                            editor.commit();
                            startActivity(intent1);
                            finishAffinity();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void saveDriverDetails(String driverId) {
        FirebaseDatabase.getInstance().getReference(nodeDataClass.DRIVER_NODE).child(driverId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DriverModel model = snapshot.getValue(DriverModel.class);
                String driverName = model.getName();
                SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("driverName",driverName);
                editor.commit();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void getData(String latLang, String driverLatLang) {

        Call<GetDistance> call = DistanceApi.getDistanceService()
                .getDistance(latLang, driverLatLang,
                        "driving", "metric", "AIzaSyAcrhT6GDVqfHQLKFmoMcFBWa0W-wFUCEg");
        Log.d("onRespons234", String.valueOf(latLang));
        call.enqueue(new Callback<GetDistance>() {
            @Override
            public void onResponse(Call<GetDistance> call, retrofit2.Response<GetDistance> response) {
                GetDistance details = response.body();

                if (details != null && details.getRows().size() > 0) {
                    List<Element> elements = details.getRows().get(0).getElements();

                    if (elements.size() > 0) {
                        Duration duration = elements.get(0).getDuration();
                        Distance distance = elements.get(0).getDistance();
                        Log.d("onRespons204", String.valueOf(duration));
                        if (duration != null && distance!=null) {
                            String timeHr = duration.getText();
                            Integer timeSec = duration.getValue();

                            String disKm = distance.getText();
                            int disM = distance.getValue();
                            if (disM>=1000){

                                int disKmInt = Integer.parseInt(disKm);

                                // Multiply disKm by 10
                                int result = disKmInt * 10;

//                                binding.textdistance.setText(result + "");
                            }else {
                                if (disM < 500) {
                                    int result2 = 10;
//                                    binding.textdistance.setText(result2+"");
                                } else {
                                    int result3=10;
//                                    binding.textdistance.setText(result3 + "");
                                }

                            }

                            // Use the text and value as needed
                            Log.d("sougata2",timeHr+"  "+timeSec);
                            binding.distanceFromDriver.setText(timeHr);

                        }
                    }
                }

            }

            @Override
            public void onFailure(Call<GetDistance> call, Throwable t) {

            }
        });
    }
    private void payment(){

        Checkout co=new Checkout();
        JSONObject object=new JSONObject();
        try {
            object.put("name","Citygo");
            object.put("description","Citygo");
            object.put("image","Citygo");
            object.put("currency","INR");
            object.put("amount","100");
            JSONObject prefill=new JSONObject();
            prefill.put("contact","8927645830");
            prefill.put("email","souradeepsingha542@gmail.com");
            object.put("prefill",prefill);
            co.open(MapsActivity.this,object);

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


    }

    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(this, "PAYMENT SUCCESS", Toast.LENGTH_SHORT).show();
        String existingData = sharedPreferences.getString("data", "");
        String newData = existingData + "\t\n From  TO \n Driver \n Phone number ";

        editor.putString("data", newData);
        editor.apply();

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "PAYMENT FAILED", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        FirebaseDatabase.getInstance().getReference("Room").child(driverId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                RoomModel model = snapshot.getValue(RoomModel.class);
                String driverLatLang = model.getLatLang();
                getData(latLang,driverLatLang);
                Log.d("sharedName",driverLatLang+latLang);
//                calculateDistance(latLang,driverLatLang);
                //Log.d("cannotfetch",latLang+driverLatLang);
                System.out.println(latLang+driverLatLang);
                Double PLatitude = divideLocationLatitudePass(latLang);
                Double PLongitude = divideLocationLongitudePass(latLang);
                Double DLatitude = divideLocationLatitudeDri(driverLatLang);
                Double DLongitude = divideLocationLongitudeDri(driverLatLang);


                // Create a LatLng object for the driver's location
                LatLng driverLatLng = new LatLng(DLatitude, DLongitude);

                // Define the camera position to zoom in on the driver's location
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(driverLatLng) // Set the target location (driver's location)
                        .zoom(15) // Set the desired zoom level (adjust as needed)
                        .build();

                // Move or animate the camera to the specified position
                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                calculateShortestPath(new LatLng(PLatitude,PLongitude),new LatLng(DLatitude,DLongitude));

                updateOtherUserMarker(new LatLng(DLatitude,DLongitude));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    private void updateOtherUserMarker(LatLng location) {
        if (otherUserMarker != null) {
            otherUserMarker.remove();
        }
        otherUserMarker = mMap.addMarker(new MarkerOptions().position(location).title("Other User"));
    }

    private Polyline shortestPath;



    private void calculateShortestPath(LatLng origin, LatLng destination) {
        // Replace with your own API key
        String apiKey = "AIzaSyAcrhT6GDVqfHQLKFmoMcFBWa0W-wFUCEg";

        String url = "https://maps.googleapis.com/maps/api/directions/json?origin=" +
                origin.latitude + "," + origin.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&key=" + apiKey;

        // Make a request to the Directions API using your preferred networking library
        // For example, you can use the Volley library for simplicity

        // Instantiate the RequestQueue
        RequestQueue queue = Volley.newRequestQueue(this);

        // Create the request
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Parse the JSON response to extract the shortest path information
                        List<LatLng> pathPoints = parsePathFromResponse(response);

                        if (pathPoints != null && pathPoints.size() > 0) {
                            if (shortestPath != null) {
                                shortestPath.remove();
                            }

                            // Draw the shortest path on the map using PolylineOptions
                            PolylineOptions polylineOptions = new PolylineOptions()
                                    .color(ContextCompat.getColor(getApplicationContext(), R.color.blue))
                                    .addAll(pathPoints);

                            // Add the Polyline to the GoogleMap instance
                            shortestPath = mMap.addPolyline(polylineOptions);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error
                    }
                });

        // Add the request to the request queue
        queue.add(jsonObjectRequest);
    }

    private List<LatLng> parsePathFromResponse(JSONObject response) {
        List<LatLng> pathPoints = new ArrayList<>();

        try {
            JSONArray routes = response.getJSONArray("routes");
            if (routes.length() > 0) {
                JSONObject route = routes.getJSONObject(0);
                JSONObject overviewPolyline = route.getJSONObject("overview_polyline");
                String encodedPoints = overviewPolyline.getString("points");

                pathPoints = decodePolyline(encodedPoints);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return pathPoints;
    }

    private List<LatLng> decodePolyline(String encodedPoints) {
        List<LatLng> polyPoints = new ArrayList<>();
        int index = 0, len = encodedPoints.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encodedPoints.charAt(index++) - 63;
                result |= (b & 0x1F) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            double latitude = lat * 1e-5;
            double longitude = lng * 1e-5;

            LatLng point = new LatLng(latitude, longitude);
            polyPoints.add(point);
        }

        return polyPoints;
    }

    private Double divideLocationLatitudePass(String passengerLatLang){
        String[] parts1 = passengerLatLang.split(",");
        Double Platitude =Double.parseDouble(parts1[0]);
        return Platitude;
    }
    private Double divideLocationLongitudePass(String passengerLatLang){
        String[] parts1 = latLang.split(",");
        Double PLongitude =Double.parseDouble(parts1[1]);
        return PLongitude;
    }
    private Double divideLocationLatitudeDri(String passengerLatLang){
        String[] parts1 = passengerLatLang.split(",");
        Double Platitude =Double.parseDouble(parts1[0]);
        return Platitude;
    }
    private Double divideLocationLongitudeDri(String passengerLatLang){
        String[] parts1 = latLang.split(",");
        Double PLongitude =Double.parseDouble(parts1[1]);
        return PLongitude;
    }



    private LatLng getLatLngFromAddress(String address) {

        Geocoder geocoder = new Geocoder(this);
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


    private Address getAddressFromLatLng(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this);
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 5);
            if (addresses != null) {
                Address address = addresses.get(0);
                return address;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    private void setTheme(String color, String title) {
        // Use resources for colors
        int parsedColor = Color.parseColor(color);



        // Set the status bar and navigation bar color
        getWindow().setStatusBarColor(parsedColor);
        getWindow().setNavigationBarColor(parsedColor);
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
    private LatLng geocodeLocation(String locationName) {
        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocationName(locationName, 1);
            if (addresses != null && !addresses.isEmpty()) {
                double latitude = addresses.get(0).getLatitude();
                double longitude = addresses.get(0).getLongitude();
                return new LatLng(latitude, longitude);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getDataRupee(LatLng fromLatLng, LatLng toLatLng) {
        Call<GetDistance> call = DistanceApi.getDistanceService()
                .getDistance(fromLatLng.latitude + "," + fromLatLng.longitude,
                        toLatLng.latitude + "," + toLatLng.longitude,
                        "driving",
                        "metric",
                        "AIzaSyAcrhT6GDVqfHQLKFmoMcFBWa0W-wFUCEg"); // Replace with your API key

        call.enqueue(new Callback<GetDistance>() {
            @Override
            public void onResponse(Call<GetDistance> call, retrofit2.Response<GetDistance> response) {
                GetDistance details = response.body();
                Log.d("onMapRead212", String.valueOf(details));
                if (details != null) {
                    int rowsSize = details.getRows() != null ? details.getRows().size() : 0;
                    Log.d("RowsSize", String.valueOf(rowsSize));
                    if (rowsSize > 0) {
                        List<Element> elements = details.getRows().get(0).getElements();
                        Log.d("onMapRead272", String.valueOf(elements));
                        if (elements.size() > 0) {
                            Duration duration = elements.get(0).getDuration();
                            Distance distance = elements.get(0).getDistance();
                            if (duration != null && distance != null) {
                                String timeHr = duration.getText();
                                Integer timeSec = duration.getValue();
//                                time.setText(timeHr+"");

                                String disKm = distance.getText();
                                Log.d("onMapRead000", String.valueOf(disKm));
// Extract the numeric part of the string (remove " km" from the end)
                                String numericValue = disKm.replace(" km", "");

                                try {
                                    // Parse the extracted numeric value as a double
                                    double disKmDouble = Double.parseDouble(numericValue);

                                    // Convert kilometers to meters and calculate the cost
                                    double disMeters = disKmDouble * 1000; // Convert kilometers to meters
                                    double cost = (disMeters / 1000) * 10; // Calculate the cost for 1000 meters

                                    // Cast the cost to an integer and set it as text
                                    int costAsInt = (int) cost;
                                    binding.textdistance.setText(costAsInt + "");
                                } catch (NumberFormatException e) {
                                    // Handle the case where the string cannot be parsed as a double
                                    e.printStackTrace();
                                    // You might want to show an error message to the user here
                                }




//                                int disM = distance.getValue();
//                                if (disM >= 1000) {
//                                    int disKmInt = Integer.parseInt(disKm);
//                                    int result = disKmInt * 10;
//                                   textdistance.setText(String.valueOf(result));
//                                } else {
//                                    if (disM < 500) {
//                                        int result2 = 10;
//                                        binding.textdistance.setText(String.valueOf(result2));
//                                    } else {
//                                        int result3 = 10;
//                                        binding.textdistance.setText(String.valueOf(result3));
//                                    }
                            }

                        }
                    }
                }
            }


            @Override
            public void onFailure(Call<GetDistance> call, Throwable t) {
                // Handle the failure case here
            }
        });
    }

}