package com.yunwltn98.googlemapapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.yunwltn98.googlemapapp.adapter.PlaceAdapter;
import com.yunwltn98.googlemapapp.api.NetworkClient;
import com.yunwltn98.googlemapapp.api.PlaceApi;
import com.yunwltn98.googlemapapp.config.Config;
import com.yunwltn98.googlemapapp.model.Place;
import com.yunwltn98.googlemapapp.model.PlaceList;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    EditText editSearch;
    Button btnSearch;
    RecyclerView recyclerView;
    PlaceAdapter adapter;
    ArrayList<Place> placeArrayList = new ArrayList<>();

    LocationManager locationManager;
    LocationListener locationListener;
    String myLocation;
    int radius = 1500;
    String type = "restaurant";
    String language = "ko";
    String nextPageToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editSearch = findViewById(R.id.editSearch);
        btnSearch = findViewById(R.id.btnSearch);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                location();
                String search = editSearch.getText().toString().trim();
                getNetworkData(search);


            }
        });
    }

    void location() {
        // 위치를 가져오기 위해서는 시스템 서비스로부터 로케이션 매니저를 받아온다
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // 로케이션 리스너를 만든다
        // 위치가 변할때마다 호출되는 함수 작성
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                myLocation = location.getLatitude()+","+location.getLongitude();
            }
        };

        // 권한요청
        if (ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(MainActivity.this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    100);
            return;
        }

        // 위치기반으로 GPS 정보 가져오는 코드를 실행하는 부분
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, -1, locationListener);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 100) {
            if(ActivityCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.checkSelfPermission(MainActivity.this,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[] {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        100);
                return;
            }

            // 위치기반으로 GPS 정보 가져오는 코드를 실행하는 부분
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 3000, -1, locationListener);
        }
    }

    void getNetworkData(String search) {

        Retrofit retrofit = NetworkClient.getRetrofitClient(MainActivity.this);
        PlaceApi api = retrofit.create(PlaceApi.class);

        Call<PlaceList> call = api.getPlacList(language, search, myLocation, radius, type, Config.API_KEY);

        call.enqueue(new Callback<PlaceList>() {
            @Override
            public void onResponse(Call<PlaceList> call, Response<PlaceList> response) {
                if (response.isSuccessful()) {
                    placeArrayList.clear();

                    nextPageToken = response.body().getNext_page_token();
                    placeArrayList.addAll(response.body().getResults());

                    Log.i("placeArrayList", String.valueOf(placeArrayList));

                    adapter = new PlaceAdapter(MainActivity.this, placeArrayList);
                    recyclerView.setAdapter(adapter);

                }
            }
            @Override
            public void onFailure(Call<PlaceList> call, Throwable t) {
            }
        });
    }
}