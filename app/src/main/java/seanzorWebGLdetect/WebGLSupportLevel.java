package seanzorWebGLdetect;

import android.util.Log;

/**
 * From package com.github.seanzor.webgl.detect
 * Edited by LuisEnrique on 2015-10-14.
 */
public enum WebGLSupportLevel {
    UNKNOWN(-2),
    NOT_SUPPORTED(-1),
    SUPPORTED_DISABLED(0),
    SUPPORTED(1);

    private final int mStatusCode;

     WebGLSupportLevel(int statusCode) {
        this.mStatusCode = statusCode;
    }
    @SuppressWarnings("unused")
    public static WebGLSupportLevel findByCode(int code) {
        for (WebGLSupportLevel currEnumValue : values()) {
            if (currEnumValue.mStatusCode == code) {
                Log.d("state", "code" + currEnumValue);
                return currEnumValue;
            }
        }
        return null;
    }

    public int getCode() {
        return this.mStatusCode;
    }
}
