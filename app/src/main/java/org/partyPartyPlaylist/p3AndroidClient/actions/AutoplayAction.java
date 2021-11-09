package org.partyPartyPlaylist.p3AndroidClient.actions;

import android.content.Context;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;


public class AutoplayAction {

    public AutoplayAction(final Context context) {

        //Use a new thread to avoid network calls on the UI thread
        new Thread(() -> {
            //Log.i("nobody", "serveraddr string" + SettingsReader.getServerAddress(context));
            ClientResource postResource = new ClientResource(
                    SettingsReader.getServerAddress(context) +
                            "/commands/autoplayStart"
            );
            postResource.post("");
        }).start();

    }

}
