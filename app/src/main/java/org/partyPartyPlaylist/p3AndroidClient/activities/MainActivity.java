package org.partyPartyPlaylist.p3AndroidClient.activities;

// Credits:
// https://developer.android.com/guide/topics/ui/controls/button

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import org.partyPartyPlaylist.p3AndroidClient.R;
import org.partyPartyPlaylist.p3AndroidClient.actions.AutoplayAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.PullupAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.SkipAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.StopAction;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PlaylistClient";

    private boolean assumesAutoplay = false;


    @Override
    protected void
    onCreate (
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        showYoutubeView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_pullup:
                pullup();
                return true;
            case R.id.action_autoplay:
                toggleAutoplay();
                return true;
            case R.id.action_skip:
                skip();
                return true;
            case R.id.action_settings:
                final Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final String INITIAL_SEARCH_QUERY_URL = "https://m.youtube.com";
    private void showYoutubeView() {
        final WebView webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled( true );
        webView.setWebViewClient( new YoutubeWebViewClient(INITIAL_SEARCH_QUERY_URL, this) );
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled( true );
        webSettings.setMediaPlaybackRequiresUserGesture( true );
        webView.loadUrl( INITIAL_SEARCH_QUERY_URL );
    }

    public void
    pullup()
    {
        new PullupAction(this);
    }

    @SuppressLint("RestrictedApi")
    public void
    drawStateAutoplay() {
        ActionMenuItemView menuItemAutoplay = findViewById(R.id.action_autoplay);
        menuItemAutoplay.setIcon( ContextCompat.getDrawable( this, android.R.drawable.ic_media_pause) );
    }

    @SuppressLint("RestrictedApi")
    public void
    drawStateStopped() {
        ActionMenuItemView menuItemAutoplay = findViewById(R.id.action_autoplay);
        menuItemAutoplay.setIcon( ContextCompat.getDrawable( this, android.R.drawable.ic_media_play) );
    }

    public void
    toggleAutoplay()
    {
        assumesAutoplay = !assumesAutoplay;
        if (assumesAutoplay) {
            drawStateAutoplay();
            new AutoplayAction(this);
        } else {
            drawStateStopped();
            new StopAction(this);
        }
    }

    public void
    skip()
    {
        new SkipAction(this);
    }


/*
    public void
    updatePlaylist()
    {
        final MainActivity thisMainActivity = this;
        // Use a new thread to avoid network calls on the UI thread:
        new Thread(() -> {
            try {
                final String playlistString =
                        new ClientResource(SettingsReader.getServerAddress(thisMainActivity) + "/info/playlist")
                                .get().getText();

                // Credits:
                // https://stackoverflow.com/questions/11123621/running-code-in-main-thread-from-another-thread
                Handler mainHandler = new Handler(thisMainActivity.getMainLooper());
                Runnable myRunnable = () -> thisMainActivity.displayPlaylistFromString(playlistString);
                mainHandler.post(myRunnable);

            } catch (IOException e) {
                Log.i(TAG, "Exception in updatePlaylist(): " + e.getMessage());
            }
        }).start();
    }

    public void
    displayPlaylistFromString (
            String playlistString
    ) {
        LinkedList<String> stringList = new LinkedList<>();
        if (playlistString == null) {
            stringList.add("The playlist is currently empty.");
        } else {
            BufferedReader reader = new BufferedReader(new StringReader(playlistString));
            try {
                for (String nextItem = reader.readLine(); nextItem != null; nextItem = reader.readLine()) {
                    stringList.add(nextItem);
                }
            } catch (IOException e) {
                Log.i(TAG, "IOException in displayPlaylistFromString(...): " + e.getMessage());
                stringList.clear();
                stringList.add("There is a problem displaying the received playlist.");
            }
        }
        ListView listView = findViewById(R.id.playlistView);
        listView.setAdapter(new ArrayAdapter<>(this, R.layout.playlist_text_view, stringList));
    }*/

}
