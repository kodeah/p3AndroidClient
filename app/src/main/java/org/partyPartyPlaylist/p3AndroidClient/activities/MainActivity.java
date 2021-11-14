package org.partyPartyPlaylist.p3AndroidClient.activities;

// Credits:
// https://developer.android.com/guide/topics/ui/controls/button

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.partyPartyPlaylist.p3AndroidClient.R;
import org.partyPartyPlaylist.p3AndroidClient.actions.AutoplayAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.PullupAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.SkipAction;
import org.partyPartyPlaylist.p3AndroidClient.actions.StopAction;
import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedList;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "PlaylistClient";

    private boolean assumesAutoplay = false;


    @Override
    protected void
    onCreate (
            Bundle savedInstanceState
    ) {
        super.onCreate(savedInstanceState);
        final Activity thisActivity = this;

        setContentView(R.layout.activity_main_activity);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        // Credits:
        // https://stackoverflow.com/questions/2719425/set-attributes-margin-gravity-etc-to-an-android-view-programmatically-wi/4594374#4594374

        Button buttonAddYoutube = findViewById(R.id.button_add_youtube);
        buttonAddYoutube.setOnClickListener(view -> {
            Intent intent = new Intent(thisActivity, YoutubeActivity.class);
            startActivity(intent);
        });

        FloatingActionButton fabGetPlaylist = findViewById(R.id.fabGetPlaylist);
        fabGetPlaylist.setOnClickListener(view -> updatePlaylist());

        FloatingActionButton fabPullup = findViewById(R.id.fabPullup);
        fabPullup.setOnClickListener(view -> post_pullup());

        FloatingActionButton fabAutoplay = findViewById(R.id.fabAutoplay);
        fabAutoplay.setOnClickListener(view -> post_toggleAutoplay());

        FloatingActionButton fabSkip = findViewById(R.id.fabSkip);
        fabSkip.setOnClickListener(view -> post_skip());

        FloatingActionButton fabSettings = findViewById(R.id.fabSettings);
        fabSettings.setOnClickListener(view -> {
            Intent intent = new Intent(thisActivity, SettingsActivity.class);
            startActivity(intent);
        });

    }


    // TODO REMOVE
    /** Called when the user touches the button */
    public void sendMessage(View view) {
        Log.i(TAG, "Button clicked.");
    }


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
    drawStateAutoplay() {
        FloatingActionButton fabAutoplay = findViewById(R.id.fabAutoplay);
        fabAutoplay.setImageResource(android.R.drawable.ic_media_pause);
    }

    public void
    drawStateStopped() {
        FloatingActionButton fabAutoplay = findViewById(R.id.fabAutoplay);
        fabAutoplay.setImageResource(android.R.drawable.ic_media_play);
    }

    public void
    post_pullup ()
    {
        //assumesAutoplay = true;
        //drawStateAutoplay();
        new PullupAction(this);
    }

    public void
    post_toggleAutoplay ()
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
    post_skip ()
    {
        new SkipAction(this);
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
    }

}
