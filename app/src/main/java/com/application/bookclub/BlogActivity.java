package com.application.bookclub;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class BlogActivity extends AppCompatActivity {

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_blog );

        webView=findViewById( R.id.blogview );
        webView.getSettings().setJavaScriptEnabled( true );
        webView.loadUrl( "https://srikar0911.pythonanywhere.com/" );

    }
}
