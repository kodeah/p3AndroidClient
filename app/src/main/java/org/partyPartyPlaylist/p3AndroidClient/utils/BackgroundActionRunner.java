package org.partyPartyPlaylist.p3AndroidClient.utils;

import android.content.Context;

import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;

import java.util.Timer;
import java.util.TimerTask;

public class BackgroundActionRunner {

    private static class TaskInfo {
        boolean finished = false;
    }

    public static void runBackgroundAction(
            final Runnable action,
            final long timeoutMs,
            final String timeoutMessage )
    {
        final TaskInfo taskInfo = new TaskInfo();

        final Thread tRun = new Thread(() -> {
            action.run();
            taskInfo.finished = true;
        });
        tRun.start();

        final Timer tTimeout = new Timer();
        tTimeout.schedule( new TimerTask() {
            @Override
            public void run() {
                if( !taskInfo.finished ){
                    throw new RuntimeException( timeoutMessage );
                }
            }
        }, timeoutMs );
    }

    public static void runNetworkAction( final Runnable action, final Context context ) {
        runBackgroundAction(
                action,
                SettingsReader.getNetworkTimeout( context ),
                "A network call has failed. Is the server address and port configured correctly and the network active?" );
    }

}
