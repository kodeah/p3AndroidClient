package org.partyPartyPlaylist.p3AndroidClient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import org.partyPartyPlaylist.p3AndroidClient.actions.EnqueueAction;
import org.partyPartyPlaylist.p3AndroidClient.utils.BackgroundActionRunner;

public class EnqueueAudioDialogFragment extends DialogFragment {
    //Credits:
    //https://developer.android.com/guide/topics/ui/dialogs

    private final Context context;

    public EnqueueAudioDialogFragment(
            final Context context )
    {
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Enqueue selected audio to playlist?")
                .setPositiveButton(
                        "Yes",
                        (dialog, id) ->
                                BackgroundActionRunner.runNetworkAction( () ->
                                        new EnqueueAction(context, (String)getArguments().get("videoUrl")),
                                        context
                                ) )
                .setNegativeButton(
                        "No",
                        (dialog, id) -> {} );
        return builder.create();
    }

}
