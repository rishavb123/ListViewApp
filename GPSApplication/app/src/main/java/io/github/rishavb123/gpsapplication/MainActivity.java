package io.github.rishavb123.gpsapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private double distanceTraveled;

    private Location curLocation;

    public static final String TAG = "MainActivityTAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        distanceTraveled = 0;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        findViewById(R.id.reset_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                distanceTraveled = 0;
                ((TextView) findViewById(R.id.distance_text_view)).setText("You have travelled " + ((int) (distanceTraveled * 100000)) / 100000.0 + " miles");
            }
        });
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                distanceTraveled += curLocation.distanceTo(location)/ 1609.344;

                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);
                Address address = null;
                try {
                    address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
                } catch (IOException | IndexOutOfBoundsException e) {
                    e.printStackTrace();
                }
                ((TextView) findViewById(R.id.lat_lng_text_view)).setText("Lat: " + ((int) (lat * 1000000)) / 1000000.0 + "\nLong: " + ((int) (lng * 1000000)) / 1000000.0);
                ((TextView) findViewById(R.id.distance_text_view)).setText("You have travelled " + ((int) (distanceTraveled * 100000)) / 100000.0 + " miles");
                try {
                    ((TextView) findViewById(R.id.address_text_view)).setText(address.getAddressLine(0));
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Invalid Location", Toast.LENGTH_SHORT).show();
                }
                curLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        initLocationTextView();
    }

    public void initLocationTextView() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.ENGLISH);

        Address address = null;
        try {
            address = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1).get(0);
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        try {
            ((TextView) findViewById(R.id.address_text_view)).setText(address.getAddressLine(0));
        } catch (NullPointerException e) {
            Toast.makeText(getApplicationContext(), "Invalid Location", Toast.LENGTH_SHORT).show();
        }
        ((TextView) findViewById(R.id.lat_lng_text_view)).setText("Lat: "+lat+"\nLong: "+lng);
        ((TextView) findViewById(R.id.distance_text_view)).setText("You have travelled "+distanceTraveled+" meters");
        curLocation = location;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initLocationTextView();
                } else {
                    Toast.makeText(MainActivity.this, "Permission denied to access your location", Toast.LENGTH_SHORT).show();
                }
            }

        }
    }
}