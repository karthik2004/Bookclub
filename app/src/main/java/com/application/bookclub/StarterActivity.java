package com.application.bookclub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StarterActivity extends AppCompatActivity {

    ListView recommendbooks;
    ArrayAdapter arrayAdapter;
    ArrayList<String> genres= new ArrayList<String>(  );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_starter );
        recommendbooks = findViewById( R.id.choosegenrelistview );
        recommendbooks.setChoiceMode( ListView.CHOICE_MODE_MULTIPLE );
        recommendbooks.setSelector( android.R.color.darker_gray );
        arrayAdapter = new ArrayAdapter( this, android.R.layout.simple_list_item_1, genres );
        recommendbooks.setAdapter( arrayAdapter );
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child( "Books" );
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String name = ds.getKey();
                    genres.add( name );
                    Log.i( "FASAAAAK","CHECK" );
                    //Log.i("YOOO",FirebaseDatabase.getInstance().getReference().child( "clubs" ).child( name ));

                    arrayAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }

        };
        ref.addListenerForSingleValueEvent( eventListener );

        recommendbooks.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView< ? > parent, View view, int position, long id) {
                view.setBackgroundColor(getResources().getColor(  android.R.color.darker_gray ));
                FirebaseDatabase.getInstance().getReference().child( "Users" ).child( FirebaseAuth.getInstance().getUid() ).child( genres.get( position ) ).setValue( position );
            }
        } );
    }

    public void submitgenreclicked(View view)
    {
        Intent intent= new Intent( this,HomeActivity.class );
        startActivity( intent );
    }
}
