package org.partyPartyPlaylist.p3AndroidClient.activities;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;

import org.partyPartyPlaylist.p3AndroidClient.fragments.EnqueueAudioDialogFragment;
import org.partyPartyPlaylist.p3AndroidClient.fragments.PlaylistNotSupportedInfoFragment;


public class YoutubeWebViewClient extends WebViewClient {

    private static final String LOG_TAG = "p3AndroidClient";

    private final FragmentActivity activity;

    private String returnUrl;

    public YoutubeWebViewClient(final String initialReturnUrl, FragmentActivity activity) {
        returnUrl = initialReturnUrl;
        this.activity = activity;
    }

    @Override
    public void onLoadResource( final WebView webView, final String url ) {
        Log.i(LOG_TAG, "onLoadResource:" + url);
        if (url.contains("youtu")) {
            if (url.contains("/watch?v=")) {
                Log.i(LOG_TAG, "Found video: " + url);
                webView.stopLoading();
                Log.i(LOG_TAG, "Loading return url: " + returnUrl);
                webView.loadUrl(returnUrl);
                DialogFragment f = new EnqueueAudioDialogFragment( activity );
                Bundle args = new Bundle();
                args.putString("videoUrl", url);
                f.setArguments(args);
                f.show( activity.getSupportFragmentManager(), "EnqueueAudioDialogFragment" );
            } else if (url.contains("/playlist?list=")) {
                Log.i(LOG_TAG, "Found playlist: " + url);
                webView.stopLoading();
                DialogFragment f = new PlaylistNotSupportedInfoFragment();
                f.show( activity.getSupportFragmentManager(), "PlaylistNotSupportedInfoFragment" );
                webView.loadUrl(returnUrl);
            } else if (url.contains("/results") && url.contains("search_query")) {
                final int indexSearchQueryParam = url.indexOf("search_query");
                if (url.substring(indexSearchQueryParam).contains("&")) {
                    // Cut additional url parameters:
                    returnUrl = url.substring(0, url.substring(indexSearchQueryParam).indexOf("&") + indexSearchQueryParam);
                } else {
                    returnUrl = url;
                }
                Log.i(LOG_TAG, "Return url captured: " + returnUrl);
            }
        }
        webView.evaluateJavascript(getDomCleanString(), null);
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
        String s =
                "setInterval(() => {" +
                getDomCleanString() +
                "}, 100);";
        view.evaluateJavascript(s, null);
    }

}