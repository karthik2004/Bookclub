package com.application.bookclub;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;



public class ViewClubsFragment extends Fragment {


    ArrayList< String > al = new ArrayList< String >();
    ArrayList< Integer > roomid = new ArrayList< Integer >();
    ListView genress;
    ArrayAdapter arrayAdapter;
    FirebaseAuth mauth;

    public ViewClubsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate( R.layout.fragment_view_clubs, container, false );
        genress = (ListView) view.findViewById( R.id.grpslistview );
        arrayAdapter = new ArrayAdapter( getContext(), android.R.layout.simple_list_item_1, al );
        genress.setAdapter( arrayAdapter );
        Log.i( "CHEEEK",MainActivity.username );
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "clubs" );
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    al.add( name );
                    Log.i( "FASAAAK", name );
                    //Log.i("YOOO",FirebaseDatabase.getInstance().getReference().child( "clubs" ).child( name ));

                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
        ref.addListenerForSingleValueEvent( eventListener );

        genress.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference().child( "clubs" ).child( al.get( position ) ).child( "roomid" );
                ref1.addValueEventListener(   new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String room =  dataSnapshot.getValue().toString();

                        String link="https://book-club-chat.herokuapp.com/chat.html?username=";
                        StringBuffer buffer= new StringBuffer(  );
                        buffer.append( link );

                        buffer.append( MainActivity.username );
                        buffer.append( "&room=" );
                        buffer.append( room );
                        Intent intent= new Intent(getContext(),ChatRoomActivity.class );
                        intent.putExtra( "link",buffer.toString() );
                        startActivity( intent );

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

        } );
        return view;


    }
}
