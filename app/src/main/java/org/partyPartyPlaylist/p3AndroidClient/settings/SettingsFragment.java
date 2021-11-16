package org.partyPartyPlaylist.p3AndroidClient.settings;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import org.partyPartyPlaylist.p3AndroidClient.R;


public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
    }

}
