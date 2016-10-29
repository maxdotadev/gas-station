package com.hackathon.unknown.gasstation.network;

import com.hackathon.unknown.gasstation.network.model.PlacesResult;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface PlacesService {
    @POST("json")
    Call<PlacesResult> getNearbyPlaces(@Query("location") String location,
                                       @Query("rankby") String rankBy,
                                       @Query("type") String type,
                                       @Query("key") String apiKey);
}
