package org.partyPartyPlaylist.p3AndroidClient.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public final class SettingsReader {

    static public String getServerAddress(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return "http://" +
                sharedPref.getString("pref_serverAddr", "") +
                ":" +
                sharedPref.getString("pref_serverPort", "");
    }

    static public long getNetworkTimeout(Context context) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
        return Long.parseLong( sharedPref.getString("pref_networkTimeout", "1000") );
    }

}
