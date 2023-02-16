package com.yunwltn98.googlemapapp.api;

import com.yunwltn98.googlemapapp.config.Config;
import com.yunwltn98.googlemapapp.model.PlaceList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {

    @GET(Config.PATH)
    Call<PlaceList> getPlacList(@Query("language") String language,
                                @Query("keyword") String keyword,
                                @Query("location") String location,
                                @Query("radius") int radius,
                                @Query("type") String type,
                                @Query("key") String key);

}
