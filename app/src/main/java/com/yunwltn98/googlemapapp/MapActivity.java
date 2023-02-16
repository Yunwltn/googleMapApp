package com.yunwltn98.googlemapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.yunwltn98.googlemapapp.model.Place;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener{

    Double Lat;
    Double Lng;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        place = (Place) getIntent().getSerializableExtra("place");
        Lat = place.getGeometry().getLocation().getLat();
        Lng = place.getGeometry().getLocation().getLng();
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        // 내 위치정보를 가져오기
        LatLng myLocation = new LatLng(Lat, Lng);
        // 지도의 중심을 내 위치로 셋팅
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17));
        // 내 위치를 마커 만들어서 지도에 표시
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLocation).title(place.getName());
        googleMap.addMarker(markerOptions).setTag(0);
        googleMap.setOnMarkerClickListener(this);

    }
}