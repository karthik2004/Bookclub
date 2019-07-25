package com.application.bookclub;

import android.Manifest;
import android.app.ListActivity;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;

import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


public class HomeActivity extends  ListActivity {

    private RecommenderCustomAdapter recommenderCustomAdapter;
    ArrayList<String> getgenre= new ArrayList<String>();
    ArrayList<ArrayList<String>> getbooks= new ArrayList<ArrayList< String>>();
    ListView recommendedlistview;
    private LocationManager locationManager;
    private LocationListener locationListener;
    int i;
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
                   // getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();
                    return true;
                case R.id.navigation_joingrp:
                    fragment = new ViewClubsFragment();
                   // getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();

                    return true;
                case R.id.navigation_addgrp:


                    fragment = new AddClubFragment();
                    fragment.setArguments( bundle );
                   // getSupportFragmentManager().beginTransaction().replace( R.id.container, fragment ).commit();


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
        recommendedlistview= getListView();
        recommenderCustomAdapter= new RecommenderCustomAdapter( this );

        getgenres();
        getbooks();
        for(int i=0;i<getbooks.size();i++)
        {
            recommenderCustomAdapter.addSectionHeaderItem( Integer.toString(  i ));
            //recommenderCustomAdapter.addSectionHeaderItem( getgenre.get( i ) );
            for(int j=0;j<getbooks.get( i ).size();j++)
            {
                recommenderCustomAdapter.addItem( "zapak" );

                //recommenderCustomAdapter.addItem( getbooks.get( i ).get( j ) );
            }
        }

        setListAdapter(recommenderCustomAdapter);
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
    public void getgenres()
    {

        DatabaseReference ref =FirebaseDatabase.getInstance().getReference().child( "Users" ).child( FirebaseAuth.getInstance().getUid() );

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    getgenre.add( name );
                    Log.i( "getgenressrunning", name );
                    Log.i( "JAJAJA",getgenre.toString() );

                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        ref.addListenerForSingleValueEvent( valueEventListener );

        //while(getgenre.size()==0) {;}

    }
    public void getbooks()
    {
        for (i = 0; i < getgenre.size(); i++) {
            DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child( "Books" ).child( getgenre.get( i ) );
            ValueEventListener getbooksEventListener = new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.getKey();
                        getbooks.get( i ).add( name );
                        Log.i( "getbooksrunning", name );
                        //Log.i("YOOO",FirebaseDatabase.
                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };

            ref1.addListenerForSingleValueEvent( getbooksEventListener );

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
