package com.hackathon.unknown.gasstation.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {
    private static RetrofitHelper sInstance;

    public static RetrofitHelper getInstance() {
        if (sInstance == null) {
            sInstance = new RetrofitHelper();
        }
        return sInstance;
    }

    private PlacesService mPlacesService;

    private RetrofitHelper() {
        OkHttpClient client = new OkHttpClient.Builder()
                .build();

        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd HH:mm:ss")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        mPlacesService = retrofit.create(PlacesService.class);
    }

    public PlacesService getPlacesService() {
        return mPlacesService;
    }
}
