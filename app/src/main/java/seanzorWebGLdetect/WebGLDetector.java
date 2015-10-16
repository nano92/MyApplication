package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 */

import android.os.Build;
import android.support.annotation.NonNull;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.lgallet.myapplication.WebViewActivity;


public class WebGLDetector {
    private static final String BLANK_HTML_PAGE = "<!doctype html><html><head></head>><body></body></html>";

    public WebGLDetector() {
    }

    public static void detect(@NonNull WebView webView, @NonNull OnReceiveDetectJsResult detectResult) {
        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            detectResult.onReceiveDetectJsResult(WebGLSupportLevel.NOT_SUPPORTED);
        } else {

            final WebView webView1 = webView;
            webView1.getSettings().setJavaScriptEnabled(true);

            //webView.loadDataWithBaseURL("https://get.webgl.org/","<!DOCTYPE html><html><head></head></html>", "text/html; charset=UTF-8", (String)null, null);
          // webView.loadData("<!DOCTYPE html><html><head></head><body><canvas id=\"canvas\" width=\"300\" height=\"300\"></canvas> </body></html>", "text/html; charset=UTF-8", (String)null);
           // webView1.loadData(BLANK_HTML_PAGE,"text/html; charset=UTF-8", null);
            webView.loadUrl("http://rnd.pelmorex.com/rasteradvection/");
           webView1.setWebViewClient(new WebViewClientChecker(detectResult, new OnFinishListener() {
               @Override
               public void finishedJsDetection() {
                  /* webView1.clearHistory();
                   webView1.clearCache(true);
                   webView1.loadUrl("about:blank");
                   webView1.pauseTimers();
                   webView1.setWebViewClient((WebViewClient) null);
               */}
           }));
        }

    }
}
