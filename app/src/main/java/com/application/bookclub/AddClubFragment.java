package com.application.bookclub;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.ProxyInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;


public class AddClubFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    public AddClubFragment() {
        // Required empty public constructor
    }




    private LocationManager locationManager;
    private LocationListener locationListener;
    private EditText genreeditext;
    private FirebaseAuth mauth;
    private LatLng locat;
    private String currentuid;
    private Button genrebutton;
    String genre;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {




        // Inflate the layout for this fragment
        View view =inflater.inflate( R.layout.fragment_add_club, container, false );
        mauth=FirebaseAuth.getInstance();
        genreeditext=view.findViewById( R.id.genreedittext );
        genrebutton =(Button) view.findViewById( R.id.genrebutton );

        Double latitude=getArguments().getDouble( "latitude" );
        Double longitude=getArguments().getDouble( "longitude" );
        locat = new LatLng( latitude, longitude );
        currentuid=mauth.getCurrentUser().getUid();
        genrebutton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genre=genreeditext.getText().toString();
                Random rand = new Random(  );
                int roomid =rand.nextInt(10000);
                //FirebaseDatabase.getInstance().getReference().child("clubs").child( genre ).child( currentuid ).setValue( locat );
                FirebaseDatabase.getInstance().getReference().child("clubs").child( genre ).child( "roomid" ).setValue( roomid );
                Toast.makeText( getContext(),"Club Added",Toast.LENGTH_SHORT ).show();
                Toast.makeText( getContext(),"Redirecting to Chat Session",Toast.LENGTH_SHORT ).show();
                Intent intent= new Intent( getContext(),ChatRoomActivity.class );
                StringBuffer buffer= new StringBuffer(  );
                String lol="https://book-club-chat.herokuapp.com/chat.html?username=";
                String user=MainActivity.username;
                buffer.append( lol );
                buffer.append( user );
                buffer.append( "&room=" );
                buffer.append( Integer.toString( roomid ) );
                intent.putExtra("link",buffer.toString() );
                startActivity( intent );

            }
        } );
        return view;




    }







    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

}
