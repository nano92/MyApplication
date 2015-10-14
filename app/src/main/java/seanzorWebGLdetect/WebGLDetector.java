package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 */
import android.content.Context;
import android.os.Build.VERSION;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.github.seanzor.webgl.detect.OnReceiveDetectJsResult;
import com.github.seanzor.webgl.detect.WebGLSupportLevel;

public class WebGLDetector {
    private static final String BLANK_HTML_PAGE = "<!DOCTYPE html><html><head></head></html>";

    public WebGLDetector() {
    }

    public static void detect(@NonNull Context activityContext, @NonNull OnReceiveDetectJsResult detectResult) {
        if(VERSION.SDK_INT < 21) {
            detectResult.onReceiveDetectJsResult(WebGLSupportLevel.NOT_SUPPORTED);
        } else {
            final WebView webView = new WebView(activityContext);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadData("<!DOCTYPE html><html><head></head></html>", "text/html; charset=UTF-8", (String)null);
            webView.setWebViewClient(new WebViewClientChecker(detectResult, new OnFinishListener() {
                public void finishedJsDetection() {
                    webView.clearHistory();
                    webView.clearCache(true);
                    webView.loadUrl("about:blank");
                    webView.pauseTimers();
                    webView.setWebViewClient((WebViewClient)null);
                }
            }));
        }

    }
}
