package seanzorWebGLdetect;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 *
 */
import android.util.Log;
import android.webkit.ValueCallback;


class DetectJsCallback implements ValueCallback<String> {
    private final OnReceiveDetectJsResult mResult;
    private final OnFinishListener mFinishListener;

    DetectJsCallback(OnReceiveDetectJsResult result, OnFinishListener finishListener) {
        this.mResult = result;
        this.mFinishListener = finishListener;
    }
    @Override
    public void onReceiveValue(String valueOfSupportCheck) {
        WebGLSupportLevel result = WebGLSupportLevel.UNKNOWN;
        if(valueOfSupportCheck != null) {


            Log.d("state", "code" + valueOfSupportCheck);
            switch(valueOfSupportCheck) {
                case "1":
                    result = WebGLSupportLevel.SUPPORTED;
                    break;
                case "0":
                    result = WebGLSupportLevel.SUPPORTED_DISABLED;
                    break;
                case "-1":
                    result = WebGLSupportLevel.NOT_SUPPORTED;
            }
        }

        mResult.onReceiveDetectJsResult(result);
        mFinishListener.finishedJsDetection();
    }
}
