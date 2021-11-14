package org.partyPartyPlaylist.p3AndroidClient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;

public class EnqueueAudioDialogFragment extends DialogFragment {
    //Credits:
    //https://developer.android.com/guide/topics/ui/dialogs

    private final Context context;

    public EnqueueAudioDialogFragment( final Context context ) {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enqueue selected audio to playlist?")
                .setPositiveButton("Yes", (dialog, id) -> new Thread(() -> {
                    final String videoUrl = (String) getArguments().get("videoUrl");
                    //Log.i(TAG, "Sending download and enqueue command with videoUrl: " + videoUrl);
                    ClientResource resource = new ClientResource(
                            SettingsReader.getServerAddress( context ) +
                                    "/commands/downloadAndEnqueue" );
                    resource.post(videoUrl);
                }).start())
                .setNegativeButton("No", (dialog, id) -> {});
        return builder.create();
    }

}
