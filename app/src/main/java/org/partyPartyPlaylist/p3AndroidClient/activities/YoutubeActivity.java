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

    private static final String INITIAL_SEARCH_QUERY_URL = "https://m.youtube.com";
    private String lastReturnUrl = INITIAL_SEARCH_QUERY_URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_youtube_link);

        context = this;

        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onLoadResource(WebView view, String url) {
                Log.i(TAG, "onLoadResource:" + url);
                if (url.contains("youtu")) {
                    if (url.contains("/watch?v=")) {
                        Log.i(TAG, "Found video: " + url);
                        webView.stopLoading();
                        Log.i(TAG, "Loading return url: " + lastReturnUrl);
                        webView.loadUrl(lastReturnUrl);
                        DialogFragment f = new EnqueueAudioDialogFragment();
                        Bundle args = new Bundle();
                        args.putString("videoUrl", url);
                        f.setArguments(args);
                        f.show(getSupportFragmentManager(), "EnqueueAudioDialogFragment");
                    } else if( url.contains("/playlist?list=") ) {
                        Log.i(TAG, "Found playlist: " + url);
                        webView.stopLoading();
                        DialogFragment f = new PlaylistNotSupportedInfoFragment();
                        f.show(getSupportFragmentManager(), "PlaylistNotSupportedInfoFragment");
                        webView.loadUrl(lastReturnUrl);
                    } else if( url.contains("/results") && url.contains("search_query")) {
                        final int indexSearchQueryParam = url.indexOf("search_query");
                        if(url.substring(indexSearchQueryParam).contains("&")) {
                            // Cut additional url parameters:
                            lastReturnUrl = url.substring(0, url.substring(indexSearchQueryParam).indexOf("&") + indexSearchQueryParam);
                        } else {
                            lastReturnUrl = url;
                        }
                        Log.i(TAG, "Return url captured: " + lastReturnUrl);
                    }
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
        webView.loadUrl(INITIAL_SEARCH_QUERY_URL);
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

    public static class PlaylistNotSupportedInfoFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Playlists are currently not supported. Please try again with a single video item.")
                    .setNeutralButton("Ok", (dialog, id)->{});
            return builder.create();
        }
    }

}
