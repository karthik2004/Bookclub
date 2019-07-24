package com.application.bookclub;

import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;

import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.common.GoogleApiAvailability;

import com.google.android.gms.location.LocationListener;
import android.Manifest;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.*;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.widget.Toast;


public class MapFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener , LocationListener, GoogleMap.OnMarkerClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    MapView mMapView;
    private GoogleMap googleMap;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );

    }

    private boolean CheckGooglePlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable( getContext() );
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError( result )) {
                googleAPI.getErrorDialog( getActivity(), result,
                        0 ).show();
            }
            return false;
        }
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate( R.layout.fragment_map, container, false );
        mMapView = (MapView) rootView.findViewById( R.id.map_fragment );
        mMapView.onCreate( savedInstanceState );

        /*Nearby places fasak check here */
        if (!CheckGooglePlayServices()) {
            Log.d( "onCreate", "Finishing test case since Google Play Services are not available" );
            Toast.makeText( getContext(), "Google Services not Available right now", Toast.LENGTH_SHORT ).show();
        } else {
            Log.d( "onCreate", "Google Play Services available." );
        }


        final Double latitude = getArguments().getDouble( "lat" );
        final Double longitude = getArguments().getDouble( "longi" );

        mMapView.onResume();// needed to get the map to display immediately

        try {
            MapsInitializer.initialize( getActivity().getApplicationContext() );
        } catch (Exception e) {
            e.printStackTrace();
        }


        mMapView.getMapAsync( new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;


                // For showing a move to my location button
                if (ContextCompat.checkSelfPermission( getActivity(),Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION ) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions( getActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1 );

                }
                googleMap.setMyLocationEnabled( true );
                buildGoogleApiClient();
                FloatingActionButton placesbutton = rootView.findViewById( R.id.placesbutton );
                placesbutton.setOnClickListener( new View.OnClickListener() {
                    String bookstores="McDonalds";
                    @Override
                    public void onClick(View v) {
                        Log.d("onClick", "Button is Clicked");
                        googleMap.clear();
                        String url = getUrl(latitude, longitude, bookstores);
                        Object[] DataTransfer = new Object[2];
                        DataTransfer[0] = googleMap;
                        DataTransfer[1] = url;
                        Log.d("onClick", url);
                        GetNearbyPlacesData getNearbyPlacesData = new GetNearbyPlacesData();
                        getNearbyPlacesData.execute(DataTransfer);
                        Toast.makeText(getContext(),"Searching Book stores...", Toast.LENGTH_LONG).show();

                    }
                } );
                // For dropping a marker at a point on the Map
                LatLng sydney = new LatLng(latitude, longitude);
                googleMap.addMarker(new MarkerOptions().position(sydney).title("Your Location").snippet("Marker Description"));

                // For zooming automatically to the location of the marker
                CameraPosition cameraPosition = new CameraPosition.Builder().target(sydney).zoom(12).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

            }
            protected synchronized void buildGoogleApiClient() {
                mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                        .addConnectionCallbacks(MapFragment.this)
                        .addOnConnectionFailedListener(MapFragment.this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }



        });



        // Perform any camera updates here


        return rootView;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // All good!
                } else {
                    Toast.makeText(getContext(), "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,this);

        }

    }
    private String getUrl(double latitude, double longitude, String nearbyPlace) {

        StringBuilder googlePlacesUrl = new StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json?");
        googlePlacesUrl.append("location=" + latitude + "," + longitude);
        googlePlacesUrl.append("&radius=" + 1000);
        googlePlacesUrl.append("&type=" + nearbyPlace);
        googlePlacesUrl.append("&sensor=true");
        googlePlacesUrl.append("&key=" + "AIzaSyCkeiF5PV5PzRz7-40yZ5v8Cb9xFJOGyW4");
        Log.d("getUrl", googlePlacesUrl.toString());
        return (googlePlacesUrl.toString());
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }




    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int i= (Integer) marker.getTag();
        LatLng locs=GetNearbyPlacesData.placepos.get( i );

        loadNavigationView( Double.toString( locs.latitude ),Double.toString( locs.longitude ) );
        return true;
    }
    public void loadNavigationView(String lat,String lng){
        Uri navigation = Uri.parse("google.navigation:q="+lat+","+lng+"");
        Intent navigationIntent = new Intent(Intent.ACTION_VIEW, navigation);
        navigationIntent.setPackage("com.google.android.apps.maps");
        startActivity(navigationIntent);
    }
}

