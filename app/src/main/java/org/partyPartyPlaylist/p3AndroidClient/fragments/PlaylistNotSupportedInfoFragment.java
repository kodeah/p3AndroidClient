package org.partyPartyPlaylist.p3AndroidClient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class PlaylistNotSupportedInfoFragment extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Playlists are currently not supported. Please try again with a single video item.")
                .setNeutralButton("Ok", (dialog, id)->{});
        return builder.create();
    }
}