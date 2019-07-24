package com.application.bookclub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.net.Uri;

public class ChatRoomActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_chat_room);
        //WebView webView=findViewById( R.id.chatroom );

        Intent intent=getIntent();
        String link=intent.getStringExtra( "link" );
        Log.i("LINKKKK",link);
        //webView.getSettings().setJavaScriptEnabled(true);
        //webView.loadUrl( link );

        Intent intent1 = new Intent(Intent.ACTION_VIEW,Uri.parse(link));
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.setPackage("com.android.chrome");
        try {
            getApplicationContext().startActivity(intent1);
        } catch (ActivityNotFoundException ex) {
            // Chrome browser presumably not installed so allow user to choose instead
            intent1.setPackage(null);
            getApplicationContext().startActivity(intent1);
        }


        //String[] usersplit=user.split( "" );
        //buffer.append( user );

        //gullapalli.srikar%40gmail.com&room=1  "
        //webView.loadUrl(  );
    }

}
