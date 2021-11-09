package org.partyPartyPlaylist.p3AndroidClient.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import org.partyPartyPlaylist.p3AndroidClient.R;


public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
