package com.citygo.user.Activities;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class DistanceApi {

    //https://maps.googleapis.com/maps/api/distancematrix/json?origins=22.245152,87.468864&destinations=22.832625,87.120909&mode=driving&units=metric&key=AIzaSyAcrhT6GDVqfHQLKFmoMcFBWa0W-wFUCEg

    private static final String url ="https://maps.googleapis.com/maps/api/";


    public static distanceService DistanceService = null;

    public static distanceService getDistanceService(){
        if (DistanceService == null){
            Retrofit retrofit = new Retrofit.Builder().baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            DistanceService = retrofit.create(distanceService.class);
        }
        return DistanceService;
    }

    public interface distanceService {
        @GET("distancematrix/json")
        Call<GetDistance> getDistance(
                @Query("origins") String origins,
                @Query("destinations") String destinations,
                @Query("mode") String mode,
                @Query("units") String units,
                @Query("key") String apiKey
        );
    }

}
