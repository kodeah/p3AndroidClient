package org.partyPartyPlaylist.p3AndroidClient.actions;

import android.content.Context;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;

public class EnqueueAction {

    public EnqueueAction(final Context context, final String videoUrl) {
        //Log.i(TAG, "Sending download and enqueue command with videoUrl: " + videoUrl);
        ClientResource resource = new ClientResource(
                SettingsReader.getServerAddress( context ) +
                        "/commands/downloadAndEnqueue" );
        resource.post(videoUrl);
    }

}
