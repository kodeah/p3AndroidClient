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

        class MyWebViewClient extends WebViewClient {
            public MyWebViewClient() {
                super();
            }

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
                    } else if (url.contains("/playlist?list=")) {
                        Log.i(TAG, "Found playlist: " + url);
                        webView.stopLoading();
                        DialogFragment f = new PlaylistNotSupportedInfoFragment();
                        f.show(getSupportFragmentManager(), "PlaylistNotSupportedInfoFragment");
                        webView.loadUrl(lastReturnUrl);
                    } else if (url.contains("/results") && url.contains("search_query")) {
                        final int indexSearchQueryParam = url.indexOf("search_query");
                        if (url.substring(indexSearchQueryParam).contains("&")) {
                            // Cut additional url parameters:
                            lastReturnUrl = url.substring(0, url.substring(indexSearchQueryParam).indexOf("&") + indexSearchQueryParam);
                        } else {
                            lastReturnUrl = url;
                        }
                        Log.i(TAG, "Return url captured: " + lastReturnUrl);
                    }
                }
                view.evaluateJavascript(getDomCleanString(), null);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                cleanDomRepeatedly(view);
            }

            private String getDomCleanString() {
                StringBuilder sb = new StringBuilder();
                // Context menus (three dots):
                sb.append("  var iconButtons = Array.from( document.getElementsByClassName('icon-button') );");
                sb.append("  iconButtons = iconButtons.filter( element => !element.classList.contains('topbar-menu-button-avatar-button') );");
                sb.append("  while(iconButtons[0]) {");
                sb.append("    iconButtons[0].parentNode.removeChild(iconButtons[0]);");
                sb.append("  }");
                // Special cards on some search results (e.g. search for an exact band name):
                sb.append("  var watchCards = Array.from( document.getElementsByTagName('ytm-universal-watch-card-renderer') );");
                sb.append("  while(watchCards[0]) {");
                sb.append("    watchCards[0].parentNode.removeChild(watchCards[0]);");
                sb.append("  }");
                sb.append("  var horizontalCards = Array.from( document.getElementsByTagName('ytm-horizontal-card-list-renderer') );");
                sb.append("  while(horizontalCards[0]) {");
                sb.append("    horizontalCards[0].parentNode.removeChild(horizontalCards[0]);");
                sb.append("  }");
                // Bottom bar:
                sb.append("  var bottomBars = Array.from( document.getElementsByTagName('ytm-pivot-bar-renderer') );");
                sb.append("  while(bottomBars[0]) {");
                sb.append("    bottomBars[0].parentNode.removeChild(bottomBars[0]);");
                sb.append("  }");
                return sb.toString();
            }

            // Credits:
            // https://stackoverflow.com/questions/2219074/in-android-webview-am-i-able-to-modify-a-webpages-dom
            private void cleanDomRepeatedly(WebView view) {
                // Remove context menus of videos for convenience:
                StringBuilder sb = new StringBuilder();
                sb.append("setInterval(() => {");
                sb.append( getDomCleanString() );
                sb.append("}, 100);");
                view.evaluateJavascript(sb.toString(), null);
            }
            private void removeCardsFromDom(WebView view) {
                // Remove context menus of videos for convenience:
                StringBuilder sb = new StringBuilder();

                view.evaluateJavascript(sb.toString(), null);
            }
        }

        webView = findViewById(R.id.webview);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new MyWebViewClient());
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
