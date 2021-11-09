package org.partyPartyPlaylist.p3AndroidClient.activities;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.partyPartyPlaylist.p3AndroidClient.R;
import org.partyPartyPlaylist.p3AndroidClient.settings.SettingsReader;
import org.restlet.resource.ClientResource;

public class YoutubeActivity extends FragmentActivity {

    private static final String TAG = "PlaylistClient - YT";

    private WebView webView;

    static Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_youtube_link);

        context = this;

        webView = (WebView) findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource:" + url);
                if (url.contains("youtu") && url.contains("/watch?v=")) {
                    webView.stopLoading();
                    webView.goBack();
                    Log.i(TAG, "Found video: " + url);
                    DialogFragment f = new EnqueueAudioDialogFragment();
                    Bundle args = new Bundle();
                    args.putString("videoUrl", url);
                    f.setArguments(args);
                    f.show(getSupportFragmentManager(), "EnqueueAudioDialogFragment");
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

        });
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setMediaPlaybackRequiresUserGesture(true);
        webView.loadUrl("https://m.youtube.com");
    }

    public static class EnqueueAudioDialogFragment extends DialogFragment {
        //Credits:
        //https://developer.android.com/guide/topics/ui/dialogs

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Enqueue selected audio to playlist?")
                    .setPositiveButton("Yes", (dialog, id) -> new Thread(() -> {
                        final String videoUrl = (String) getArguments().get("videoUrl");
                        //Log.i(TAG, "Sending download and enqueue command with videoUrl: " + videoUrl);
                        ClientResource resource = new ClientResource(
                                SettingsReader.getServerAddress(context) +
                                        "/commands/downloadAndEnqueue" );
                        resource.post(videoUrl);
                    }).start())
                    .setNegativeButton("No", (dialog, id) -> {});
            return builder.create();
        }
    }

}
