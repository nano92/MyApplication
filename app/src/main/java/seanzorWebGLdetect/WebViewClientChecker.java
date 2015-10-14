package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 */
import android.annotation.TargetApi;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.github.seanzor.webgl.detect.OnReceiveDetectJsResult;
import com.github.seanzor.webgl.detect.WebGLSupportLevel;

class WebViewClientChecker extends WebViewClient {
    private final OnReceiveDetectJsResult mDetectJsResult;
    private final OnFinishListener mOnFinishListener;
    private boolean noErrorRaised = true;
    private final String JS_CHECKER = "function detectWebGL()\n{\n    // Check for the WebGL rendering context\n    if ( !! window.WebGLRenderingContext) {\n        var canvas = document.createElement(\"canvas\"),\n            names = [\"webgl\", \"experimental-webgl\", \"moz-webgl\", \"webkit-3d\"],\n            context = false;\n\n        for (var i in names) {\n            try {\n                context = canvas.getContext(names[i]);\n                if (context && typeof context.getParameter === \"function\") {\n                    // WebGL is enabled.\n                    return 1;\n                }\n            } catch (e) {}\n        }\n\n        // WebGL is supported, but disabled.\n        return 0;\n    }\n\n    // WebGL not supported.\n    return -1;\n};detectWebGL();";

    @TargetApi(19)
    WebViewClientChecker(OnReceiveDetectJsResult detectJsResult, OnFinishListener onFinishListener) {
        this.mDetectJsResult = detectJsResult;
        this.mOnFinishListener = onFinishListener;
    }

    @TargetApi(19)
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(this.noErrorRaised) {
            view.evaluateJavascript("function detectWebGL()\n{\n    // Check for the WebGL rendering context\n    if ( !! window.WebGLRenderingContext) {\n        var canvas = document.createElement(\"canvas\"),\n            names = [\"webgl\", \"experimental-webgl\", \"moz-webgl\", \"webkit-3d\"],\n            context = false;\n\n        for (var i in names) {\n            try {\n                context = canvas.getContext(names[i]);\n                if (context && typeof context.getParameter === \"function\") {\n                    // WebGL is enabled.\n                    return 1;\n                }\n            } catch (e) {}\n        }\n\n        // WebGL is supported, but disabled.\n        return 0;\n    }\n\n    // WebGL not supported.\n    return -1;\n};detectWebGL();", new DetectJsCallback(this.mDetectJsResult, this.mOnFinishListener));
        } else {
            this.mDetectJsResult.onReceiveDetectJsResult(WebGLSupportLevel.UNKNOWN);
        }

    }

    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        this.noErrorRaised = false;
    }
}

