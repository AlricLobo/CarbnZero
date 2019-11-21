package com.example.carbnzero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ClipData;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.widget.Toast;


import com.google.android.material.bottomnavigation.BottomNavigationView;

public class real_MainActivity extends AppCompatActivity implements LocationListener {

    public static Handler UIHandler;
    static int i;
    static float rSpeed = 0;
    static float tSpeed = 0;
    static boolean isFirst = false;
    static Location newLocation = new Location("");
    static Location stopLocation = new Location("");
    public static float totalEmissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real__main);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
        System.out.println("THIS IS THE USERS MPG: "+ MainActivity.userMPG);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            //start the program if the permission is granted
            doStuff();
        }
        this.updateSpeed(null);
        test();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;

                    switch (menuItem.getItemId()) {
                        case R.id.nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.nav_stats:
                            selectedFragment = new StatsFragment();
                            break;
                        case R.id.nav_settings:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.nav_info:
                            selectedFragment = new InfoFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }


            };


    public void onLocationChanged(Location location) {
        if (location != null) {
            CLocation myLocation = new CLocation(location);
            this.updateSpeed(myLocation);
        }
    }


    public void onStatusChanged(String s, int i, Bundle bundle) {

    }


    public void onProviderEnabled(String s) {

    }


    public void onProviderDisabled(String s) {

    }

    @SuppressLint("MissingPermission")
    private void doStuff() {

        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        Toast.makeText(this, "Waiting for GPS connection!", Toast.LENGTH_SHORT).show();


    }

    private static float calcEmissions(float distance, float mpg) {
        float carbFootprint = ((float)(distance*0.000621371) / mpg) * (float) 19.4 * (100 / 95);
        System.out.println("THIS IS THE CARBON FOOTPRINT:" + carbFootprint);
        return carbFootprint;


    }

    private static float distanceTo() {
        float distanceInMeters = newLocation.distanceTo(stopLocation);
        System.out.println("THIS IS THE DISTANCE:" + distanceInMeters);
        return distanceInMeters;
    }


    private void updateSpeed(CLocation location) {


        if (location != null) {


            rSpeed = location.getSpeed();
            driveTime();



        }


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doStuff();
            } else {
                finish();
            }
        }
    }

    static {
        UIHandler = new Handler(Looper.getMainLooper());
    }

    public static void runOnUI(Runnable runnable) {
        UIHandler.post(runnable);
    }

    public static void driveTime() {

        if (tSpeed >= 24 && isFirst == false) {

            newLocation.setLatitude(143);//newLocation.getLatitude());
            newLocation.setLongitude(143);//newLocation.getLongitude());
            isFirst = true;
        }
        if (isFirst == true && tSpeed < 24) {


            stopLocation.setLatitude(144);//stopLocation.getLatitude());
            stopLocation.setLongitude(144);//stopLocation.getLongitude());
            isFirst = false;
            totalEmissions = totalEmissions +(calcEmissions(distanceTo(),MainActivity.userMPG));
            System.out.println("THIS IS THE TOTAL CALC EMISSIONS: "+totalEmissions);
            gaugeAnimate();

        }

    }
    public static void gaugeAnimate()
    {
        new Thread() {
            public void run() {

                for (i = 0; i <= totalEmissions; i++) {


                    try {
                        runOnUI(new Runnable() {
                            @Override
                            public void run() {
                                HomeFragment.gauge3.setValue(i);

                                HomeFragment.text1.setText(String.valueOf(HomeFragment.gauge3.getValue()));

                            }
                        });
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    public static void test()
    {
        tSpeed = 30;
        driveTime();
        tSpeed = 10;
        driveTime();
        tSpeed = 40;
        driveTime();
        tSpeed = 15;
        driveTime();


    }
}
