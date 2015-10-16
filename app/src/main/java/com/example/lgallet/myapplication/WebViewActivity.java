package com.example.lgallet.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;



/**
 * Created by LGallet on 2015-10-16.
 */
public class WebViewActivity extends Activity {
    private WebView webView;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);

        webView = (WebView) findViewById(R.id.webView1);


        webView.getSettings().setJavaScriptEnabled(true);

        webView.loadUrl("http://rnd.pelmorex.com/rasteradvection/");

    }

}
