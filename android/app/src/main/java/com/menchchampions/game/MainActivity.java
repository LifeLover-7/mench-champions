package com.menchchampions.game;
import android.app.Activity;
import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import java.io.IOException;
public class MainActivity extends Activity {
    private WebView web;
    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(245, 247, 255));
        getWindow().setNavigationBarColor(Color.rgb(245, 247, 255));
        getWindow().getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        web = new WebView(this);
        web.setBackgroundColor(Color.rgb(245, 247, 255));
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setUserAgentString(web.getSettings().getUserAgentString() + " MenchChampionsAndroid/1.0");
        web.addJavascriptInterface(new NativeBridge(), "AndroidApp");
        web.getSettings().setDomStorageEnabled(true);
        web.getSettings().setMediaPlaybackRequiresUserGesture(true);
        web.getSettings().setAllowFileAccess(false);
        web.getSettings().setAllowContentAccess(false);
        web.setWebViewClient(new WebViewClient() {
            @Override public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if ("mench://exit".equals(request.getUrl().toString())) { finishAndRemoveTask(); return true; }
                return !"https://app.mench.local/index.html".equals(request.getUrl().toString());
            }
            @Override public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if ("mench://exit".equals(url)) { finishAndRemoveTask(); return true; }
                return !"https://app.mench.local/index.html".equals(url);
            }
            @Override public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
                if (request.getUrl().toString().equals("https://app.mench.local/index.html")) {
                    try { return new WebResourceResponse("text/html", "UTF-8", getAssets().open("index.html")); }
                    catch (IOException e) { return null; }
                }
                return new WebResourceResponse("text/plain", "UTF-8", null);
            }
        });
        setContentView(web);
        if (state != null) web.restoreState(state); else web.loadUrl("https://app.mench.local/index.html");
    }
    public final class NativeBridge {
        @JavascriptInterface public void exitGame() { runOnUiThread(() -> finishAndRemoveTask()); }
    }
    @Override protected void onSaveInstanceState(Bundle state) { web.saveState(state); super.onSaveInstanceState(state); }
    @Override protected void onPause() { web.onPause(); web.pauseTimers(); super.onPause(); }
    @Override protected void onResume() { super.onResume(); if (web != null) { web.onResume(); web.resumeTimers(); } }
    @Override public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("خروج از منچ قهرمانان؟").setMessage("پیشرفت بازی به‌طور خودکار روی همین دستگاه ذخیره می‌شود.")
            .setPositiveButton("خروج", (dialog, which) -> finishAndRemoveTask()).setNegativeButton("ادامه بازی", null).show();
    }
    @Override protected void onDestroy() { if (web != null) web.destroy(); super.onDestroy(); }
}
