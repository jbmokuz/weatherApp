package com.example.weatherapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements LocationListener {
    public Networking n = null;
    LocationManager locationManager = null;
    double latitude = 0;
    double longitude = 0;

    WeatherAdapter adapter;

    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String [] weather = {"Rain", "Cloudy", "Snow"};

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new WeatherAdapter(weather);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        n = new Networking();

        tv = findViewById(R.id.title);
        locationManager = (LocationManager)  this.getSystemService(Context.LOCATION_SERVICE);

        tv.setText("Getting location");

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action != null && action.equals(Intent.ACTION_RUN)) {
            String sentText = intent.getStringExtra("sentText");
            tv.setText(sentText);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Criteria criteria = new Criteria();
        String bestProvider = String.valueOf(locationManager.getBestProvider(criteria, true)).toString();

        //You can still do this if you like, you might get lucky:
        Location location = locationManager.getLastKnownLocation(bestProvider);
        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();

            System.out.println(latitude);
            System.out.println(longitude);

            getCity();
        }
        else{
            locationManager.requestLocationUpdates(bestProvider, 1000, 0, (LocationListener) this);
        }
    }

    public void getCity(){

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            int i = 0;
            try {
                if (n != null) {
                    i = n.updateLocation(latitude,longitude);
                    n.updateWeather(i);
                }
            } catch (java.net.UnknownHostException e){
                i = -1;
            }
            catch (IOException | JSONException e) {
                System.out.println(e.toString());
                e.printStackTrace();
            }
            handler.post(() -> tv.setText(n.title));
        });

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

        locationManager.removeUpdates(this);

        System.out.println(latitude);
        System.out.println(longitude);

        getCity();
    }
}