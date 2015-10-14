package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 *
 */
import android.webkit.ValueCallback;
import com.github.seanzor.webgl.detect.OnReceiveDetectJsResult;
import com.github.seanzor.webgl.detect.WebGLSupportLevel;

class DetectJsCallback implements ValueCallback<String> {
    private final OnReceiveDetectJsResult mResult;
    private final OnFinishListener mFinishListener;

    DetectJsCallback(OnReceiveDetectJsResult result, OnFinishListener finishListener) {
        this.mResult = result;
        this.mFinishListener = finishListener;
    }

    public void onReceiveValue(String valueOfSupportCheck) {
        WebGLSupportLevel result = WebGLSupportLevel.UNKNOWN;
        if(valueOfSupportCheck != null) {
            byte var4 = -1;
            switch(valueOfSupportCheck.hashCode()) {
                case 48:
                    if(valueOfSupportCheck.equals("0")) {
                        var4 = 1;
                    }
                    break;
                case 49:
                    if(valueOfSupportCheck.equals("1")) {
                        var4 = 0;
                    }
                    break;
                case 1444:
                    if(valueOfSupportCheck.equals("-1")) {
                        var4 = 2;
                    }
            }

            switch(var4) {
                case 0:
                    result = WebGLSupportLevel.SUPPORTED;
                    break;
                case 1:
                    result = WebGLSupportLevel.SUPPORTED_DISABLED;
                    break;
                case 2:
                    result = WebGLSupportLevel.NOT_SUPPORTED;
            }
        }

        this.mResult.onReceiveDetectJsResult(result);
        this.mFinishListener.finishedJsDetection();
    }
}
