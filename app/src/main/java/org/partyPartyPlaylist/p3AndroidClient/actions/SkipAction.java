package org.partyPartyPlaylist.p3AndroidClient.actions;

import android.content.Context;

import org.restlet.resource.ClientResource;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;

public class SkipAction {

    public SkipAction(final Context context) {

        //Use a new thread to avoid network calls on the UI thread
        new Thread(() -> {
            ClientResource postResource = new ClientResource(
                    SettingsReader.getServerAddress(context) +
                            "/commands/skip"
            );
            postResource.post("");
        }).start();

    }

}
