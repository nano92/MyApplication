package seanzorWebGLdetect;

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

    private WebGLSupportLevel(int statusCode) {
        this.mStatusCode = statusCode;
    }

    public static WebGLSupportLevel findByCode(int code) {
        WebGLSupportLevel[] var1 = values();
        int var2 = var1.length;

        for(int var3 = 0; var3 < var2; ++var3) {
            WebGLSupportLevel currEnumValue = var1[var3];
            if(currEnumValue.mStatusCode == code) {
                return currEnumValue;
            }
        }

        return null;
    }

    public int getCode() {
        return this.mStatusCode;
    }
}
