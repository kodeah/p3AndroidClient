package org.partyPartyPlaylist.p3AndroidClient.actions;

import android.content.Context;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;

public class PullupAction {

    public PullupAction(final Context context) {
        ClientResource postResource = new ClientResource(
                SettingsReader.getServerAddress(context) +
                        "/commands/pullup"
        );
        postResource.post("");
    }

}
