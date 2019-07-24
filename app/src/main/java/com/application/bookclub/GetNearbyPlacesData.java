package com.application.bookclub;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.*;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.util.HashMap;
import java.util.*;

public class GetNearbyPlacesData extends AsyncTask<Object,String,String> {
    String googlePlacesData;
    GoogleMap mMap;
    String url;
    static ArrayList<LatLng> placepos = new ArrayList<LatLng>(  );
    @Override
    protected String doInBackground(Object... params) {
        try {
            Log.d("GetNearbyPlacesData", "doInBackground entered");
            mMap = (GoogleMap) params[0];
            url = (String) params[1];
            DownloadUrl downloadUrl = new DownloadUrl();
            googlePlacesData = downloadUrl.readUrl(url);
            Log.d("GooglePlacesReadTask", "doInBackground Exit");
        } catch (Exception e) {
            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute( result );
        Log.d("GooglePlacesReadTask", "onPostExecute Entered");
        List< HashMap<String, String> > nearbyPlacesList = null;
        DataParser dataParser = new DataParser();
        nearbyPlacesList =  dataParser.parse(result);
        ShowNearbyPlaces(nearbyPlacesList);
        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }
    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {
        for (int i = 0; i < 5; i++) {
            Log.d( "onPostExecute", "Entered into showing locations" );
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap< String, String > googlePlace = nearbyPlacesList.get( i );
            double lat = Double.parseDouble( googlePlace.get( "lat" ) );
            double lng = Double.parseDouble( googlePlace.get( "lng" ) );
            String placeName = googlePlace.get( "place_name" );
            String vicinity = googlePlace.get( "vicinity" );
            LatLng latLng = new LatLng( lat, lng );
            markerOptions.position( latLng );
            markerOptions.title( placeName + " : " + vicinity );
            markerOptions.icon( BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_AZURE ) );
            mMap.addMarker( markerOptions ).setTag( i );
            placepos.add( latLng );

            mMap.moveCamera( CameraUpdateFactory.newLatLng( latLng ) );
            mMap.animateCamera( CameraUpdateFactory.zoomTo( 11 ) );
        }
    }
}
