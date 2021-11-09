package org.partyPartyPlaylist.p3AndroidClient.activities;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsFragment;

public class SettingsActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }


}
