package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 */
import android.annotation.TargetApi;
import android.os.Build;
import android.webkit.WebView;
import android.webkit.WebViewClient;


class WebViewClientChecker extends WebViewClient {
    private final OnReceiveDetectJsResult mDetectJsResult;
    private final OnFinishListener mOnFinishListener;
    private boolean noErrorRaised = true;
    //private final String JS_CHECKER = "function detectWebGL()\n{\n    // Check for the WebGL rendering context\n    if ( (window.WebGLRenderingContext || window.WebGL2RenderingContext)) {\n        var canvas = document.createElement(\"canvas\"),\n            names = [\"webgl\", \"experimental-webgl\", \"moz-webgl\", \"webkit-3d\",\"webgl2\",\"experimental-webgl2\"],\n            context = false;\n\n        for (var i = 0; i < names.length; i++) {\n            try {\n                context = canvas.getContext(names[i]);\n                if ( typeof (context.getParameter) === \"function\") {\n                    // WebGL is enabled.\n                    return 1;\n                }\n            } catch (e) {}\n        }if (!context){\n        // WebGL is supported, but disabled.\n        return 0;\n  }\n  }\n\n    // WebGL not supported.\n    return -1;\n};detectWebGL();";
    final private String JS_CHECKER = "function detectWebGL()\n" +
            "{\n" +
            "    // Check for the WebGL rendering context\n" +
            "    if ( !! (window.WebGLRenderingContext|| window.WebGL2RenderingContext)) {\n" +
            "          var canvas,\n" +
            "            names = [\"webgl\", \"experimental-webgl\", \"moz-webgl\", \"webkit-3d\",\"webgl2\",\"experimental-webgl2\"],\n" +
            "            context; \n   "+
            "\n" +
            "        for (var i in names) {\n" +
            "            try {\n" +
            "                canvas = document.createElement('canvas');"+
            "                context = canvas.getContext(names[i]);\n" +
            "                if (context && typeof (context.getParameter) === \"function\") {\n" +
            "                    // WebGL is enabled.\n" +
            "                    return 1;\n" +
            "                }\n" +
            "            } catch (e) {}\n" +
            "        }if(!context){\n" +
            "\n" +
            "        // WebGL is supported, but disabled.\n" +
            "        return 0;\n" +
            "   } }\n" +
            "\n" +
            "    // WebGL not supported.\n" +
            "    return -1;\n" +
            "};"+
           "detectWebGL();";
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    WebViewClientChecker(OnReceiveDetectJsResult detectJsResult, OnFinishListener onFinishListener) {
        this.mDetectJsResult = detectJsResult;
        this.mOnFinishListener = onFinishListener;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        if(noErrorRaised) {
            //view.evaluateJavascript(" function detectWebGL()\n {\n    // Check for the WebGL rendering context\n    if ( (window.WebGLRenderingContext || window.WebGL2RenderingContext)) {\n        var canvas = document.createElement(\"canvas\"),\n      names = [\"webgl\", \"experimental-webgl\", \"moz-webgl\", \"webkit-3d\",\"webgl2\",\"experimental-webgl2\"],\n context = false;\n\n        for (var i = 0; i < names.length; i++) {\n            try {\n                context = canvas.getContext(names[i]);\n                if ( context && typeof (context.getParameter) === \"function\" ) {\n                // WebGL is enabled.\n                    return 1;\n                }\n            } catch (e) {}\n        }if (!context){\n        // WebGL is supported, but disabled.\n        return 0;\n   }\n }\n\n    // WebGL not supported.\n    return -1;\n};detectWebGL();", new DetectJsCallback(mDetectJsResult, mOnFinishListener));
            view.evaluateJavascript(JS_CHECKER,new DetectJsCallback(mDetectJsResult, mOnFinishListener));
        } else {
            mDetectJsResult.onReceiveDetectJsResult(WebGLSupportLevel.UNKNOWN);
        }

    }
    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        noErrorRaised = false;
    }
}

