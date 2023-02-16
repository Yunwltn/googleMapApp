package com.yunwltn98.googlemapapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.yunwltn98.googlemapapp.model.Place;

public class MapActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        Place place = (Place) getIntent().getSerializableExtra("place");
        LatLng myLocation = new LatLng(37.5429, 126.6772);

    }
}