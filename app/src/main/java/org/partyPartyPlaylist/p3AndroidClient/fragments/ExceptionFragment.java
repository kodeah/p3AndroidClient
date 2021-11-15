package org.partyPartyPlaylist.p3AndroidClient.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

public class ExceptionFragment extends DialogFragment {

    private final Throwable throwable;

    public ExceptionFragment( final Throwable throwable ) {
        this.throwable = throwable;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage( "An error has occured:" + System.lineSeparator() + System.lineSeparator() + throwable.getMessage() )
                .setNeutralButton( "dismiss", (dialog, id)->{} );
        return builder.create();
    }
}