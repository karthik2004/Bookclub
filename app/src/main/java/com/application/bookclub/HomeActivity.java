package com.application.bookclub;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.location.Criteria;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    Bundle bundle = new Bundle();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new MapFragment();
                    fragment.setArguments( bundle );
                    getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();
                    return true;
                case R.id.navigation_joingrp:
                    fragment = new ViewClubsFragment();
                    getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();

                    return true;
                case R.id.navigation_addgrp:


                    fragment = new AddClubFragment();
                    fragment.setArguments( bundle );
                    getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();


                    return true;
                case R.id.navigation_blog:
                    Intent intent = new Intent( getApplicationContext(), BlogActivity.class );
                    startActivity( intent );

            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        BottomNavigationView navView = findViewById( R.id.nav_view );
        navView.setOnNavigationItemSelectedListener( mOnNavigationItemSelectedListener );

        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE );
        locationListener = new android.location.LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();
                bundle.putDouble( "lat",latitude );
                bundle.putDouble( "longi",longitude );
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
        if(ContextCompat.checkSelfPermission( getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION )!= PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions( HomeActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );
        }
        else {

            locationManager.requestLocationUpdates( LocationManager.GPS_PROVIDER, 0, 0, locationListener );
            locationManager.requestLocationUpdates( LocationManager.NETWORK_PROVIDER, 0, 0, locationListener );
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        LocationManager locationManager =
                (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        final boolean gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {

            AlertDialog.Builder builder= new AlertDialog.Builder( this );
            builder.setMessage( "Enable Location" ).setCancelable( false ).setPositiveButton( "OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    enableLocationSettings();
                }
            } );
        }


    }
    private void enableLocationSettings()
    {
        Intent settingsIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(settingsIntent);
    }
}
