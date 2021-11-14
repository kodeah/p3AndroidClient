package org.partyPartyPlaylist.p3AndroidClient.activities;


import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.fragment.app.FragmentActivity;

import org.partyPartyPlaylist.p3AndroidClient.R;

public class YoutubeActivity extends FragmentActivity {

    private static final String INITIAL_SEARCH_QUERY_URL = "https://m.youtube.com";

    @Override
    protected void onCreate( final Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select_youtube_link );

        final WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.setWebViewClient( new YoutubeWebViewClient(INITIAL_SEARCH_QUERY_URL, this) );
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled( true );
        webSettings.setMediaPlaybackRequiresUserGesture( true );
        webView.loadUrl( INITIAL_SEARCH_QUERY_URL );
    }

}
